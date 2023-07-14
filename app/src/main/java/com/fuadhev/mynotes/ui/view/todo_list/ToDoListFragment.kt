package com.fuadhev.mynotes.ui.view.todo_list

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.fuadhev.mynotes.AlarmWorker

import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.AddTodoBottomsheetDialogBinding
import com.fuadhev.mynotes.databinding.FragmentToDoListBinding
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.receiver.AlarmReceiver
import com.fuadhev.mynotes.ui.adapter.ToDoClickListener
import com.fuadhev.mynotes.ui.adapter.TodoAdapter
import com.fuadhev.mynotes.ui.view.note_detail.NoteDetailFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ToDoListFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentToDoListBinding
    private val viewModel by viewModels<TodoListViewModel>()
    private val todoAdapter by lazy {
        TodoAdapter(object : ToDoClickListener {
            @RequiresApi(Build.VERSION_CODES.S)
            override fun todoClickListener(toDO: ToDo) {
                showBottomSheetDialog(toDO)

            }

            override fun changeisComplete(toDO: ToDo) {
                viewModel.updateToDo(toDO)
            }

        }, emptyList())
    }
    private var alarmTime: Calendar? = null
    private var isAlarm: Boolean = false
    private var time: String = ""
    private var requestCode: Int = 0
    val PERMISSION_REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_do_list, container, false)

        requestPermission()
        onBackPressed()
        setRecyclerView()
        observes()
        setItemTouchListener()

        binding.fab.setOnClickListener {

            showBottomSheetDialog(null)
        }


        // Inflate the layout for this fragment
        return binding.root
    }


    private fun setItemTouchListener() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            @RequiresApi(Build.VERSION_CODES.S)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val toDo = todoAdapter.getToDoByPosition(position)
                viewModel.deleteToDo(toDo)
                cancelAlarmWithAlarmManager(toDo.todo_txt, toDo.requestCode)
                Log.e("requestcodedelete", requestCode.toString())
                showDeleteSnackBar(toDo)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.delete)
                    val iconSize = 30.toPx() // dp to px conversion

                    val iconMargin = (itemView.height - deleteIcon?.intrinsicHeight!!) / 2
                    val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconBottom = iconTop + deleteIcon.intrinsicHeight

                    if (dX > 0) {
                        // Sağa kaydırma işlemi
                        val iconLeft = itemView.left + iconMargin
                        val iconRight = itemView.left + iconMargin + iconSize

                        // Delete simgesini çiz
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        deleteIcon.draw(c)
                    } else {
                        // Sola kaydırma işlemi
                        val iconLeft = itemView.right - iconMargin - iconSize
                        val iconRight = itemView.right - iconMargin

                        // Delete simgesini çiz
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        deleteIcon.draw(c)
                    }
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            private fun Int.toPx(): Int {
                val scale = Resources.getSystem().displayMetrics.density
                return (this * scale + 0.5f).toInt()
            }
        }





        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rv)
    }

    private fun showDeleteSnackBar(toDo: ToDo) {
        Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).apply {
            setAction(
                "Undo"
            ) {
                viewModel.insertToDo(toDo)

            }.show()
        }
    }


    private fun setRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.adapter = todoAdapter
    }

    private fun observes() {
        viewModel.getAllTodos.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.lottie.visibility = VISIBLE
            } else {
                binding.lottie.visibility = GONE
            }
            todoAdapter.updateNotes(it)
        }

    }

    private fun onBackPressed(){
        val activity = requireActivity()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (findNavController().currentDestination?.id == R.id.toDoListFragment) {
                  requireActivity().finish()
                }
            }
        }
        // Geri tuşu olayını etkinleştirin
        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showBottomSheetDialog(toDo: ToDo?) {

        val mDialog = Dialog(requireContext())
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        val dialogBinding = AddTodoBottomsheetDialogBinding.inflate(layoutInflater)
        mDialog.setContentView(dialogBinding.root)
        mDialog.setCancelable(true)
//        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (toDo != null) {
            dialogBinding.toDoTxt.setText(toDo.todo_txt)
            dialogBinding.dialogTxt.text = "Update task"
            dialogBinding.add.text = "Update"
            dialogBinding.time.text = toDo.time
            dialogBinding.alarm.isChecked = toDo.isAlarm
        }
        dialogBinding.calendar.setOnClickListener {
            setAlarmWithManager(dialogBinding.time)
        }
        dialogBinding.alarm.setOnCheckedChangeListener { buttonView, isChecked ->

            if (alarmTime == null && toDo == null) {
                Toast.makeText(requireContext(), "Please select alarm time", Toast.LENGTH_SHORT)
                    .show()
                dialogBinding.alarm.isChecked = false
                return@setOnCheckedChangeListener
            }

            isAlarm = isChecked
        }

        dialogBinding.add.setOnClickListener {

            if (dialogBinding.toDoTxt.text.trim()==""){
                Toast.makeText(requireContext(), "Please add task", Toast.LENGTH_SHORT).show()
            }else{
                if (dialogBinding.alarm.isChecked) {
                    if (alarmTime == null) {
                        Toast.makeText(requireContext(), "Please select time", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    } else {
                        setAlarmWithAlarmManager(alarmTime!!, dialogBinding.toDoTxt.text.toString())
                    }
                }
                if (toDo == null) {
                    val todo = ToDo(
                        0,
                        dialogBinding.toDoTxt.text.toString(),
                        isAlarm,
                        dialogBinding.time.text.toString(),
                        requestCode,
                        false
                    )
                    viewModel.insertToDo(todo)

                } else {
                    val todo = ToDo(
                        toDo.id,
                        dialogBinding.toDoTxt.text.toString(),
                        isAlarm,
                        dialogBinding.time.text.toString(),
                        requestCode,
                        false
                    )
                    viewModel.insertToDo(todo)
                }
                mDialog.dismiss()
                time = ""
                isAlarm = false
                alarmTime = null

            }


        }

        mDialog.create()
        mDialog.show();
        mDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        mDialog.window?.setGravity(Gravity.BOTTOM);
    }

    private fun setAlarmWithManager(btime: TextView) {
        val calendar = Calendar.getInstance()

        // Date Picker ile tarih seçimi
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH + 1, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                // Time Picker ile saat seçimi
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)


//                        time ="$day.$month.$year $hourOfDay:$minute"
                        alarmTime = calendar
                        if (month<10){

                        }

                        if (month<10&&minute<10){
                            btime.text="$day.0$month.$year $hourOfDay:0$minute"
                        }else if (month<10){
                           btime.text = "$day.0$month.$year $hourOfDay:$minute"
                        }else if (minute<10){
                            btime.text = "$day.$month.$year $hourOfDay:0$minute"
                        }

//                        setAlarmWithWorkManager(calendar)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()

    }


    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarmWithAlarmManager(alarmTime: Calendar, alarmTag: String) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(requireContext(), AlarmReceiver::class.java)
        notificationIntent.putExtra("ALARM_MESSAGE_KEY", alarmTag)

        requestCode =
            generateRequestCode() // Her bir alarm için benzersiz bir requestCode oluşturun
//        val pendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        // AlarmManager'ı kullanarak bildirimi ayarlayın
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis, pendingIntent)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarmWithAlarmManager(alarmTag: String, requestCode: Int) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(requireContext(), AlarmReceiver::class.java)
        notificationIntent.putExtra("ALARM_MESSAGE_KEY", alarmTag)

//        val pendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.cancel(pendingIntent)

    }

    private fun generateRequestCode(): Int {
        return (1..10000).random()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.POST_NOTIFICATIONS
        )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            requireActivity(),
            "These permission are required",
            PERMISSION_REQUEST_CODE,
            android.Manifest.permission.POST_NOTIFICATIONS

        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), "Permission garanted", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    /** bildirimde gecikme oldugu ucun workmanager ile islemedim **/

//    private fun setAlarmWithWorkManager(alarmTime: Calendar,alarmTag:String) {
//        val currentTime = Calendar.getInstance()
//        if (currentTime.after(alarmTime)) {
//            Toast.makeText(requireContext(),"A past time has been selected, we do not set the alarm.", Toast.LENGTH_SHORT).show()
//            // Geçmiş bir zaman seçildi, alarmı ayarlamıyoruz.
//            return
//        }
//
//        val timeDiffInMillis = alarmTime.timeInMillis - currentTime.timeInMillis
//
//        val inputData = Data.Builder()
//            .putString("ALARM_MESSAGE_KEY", alarmTag)
//            .build()
//
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val alarmRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
//            .setInitialDelay(timeDiffInMillis-60000, TimeUnit.MILLISECONDS)
//            .setInputData(inputData)
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance(requireContext()).enqueueUniqueWork(
//            "ALARM_WORK_TAG",
//            ExistingWorkPolicy.REPLACE,
//            alarmRequest
//        )
//    }

}