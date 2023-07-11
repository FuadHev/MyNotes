package com.fuadhev.mynotes.ui.view.todo_list

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*

import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.AddTodoBottomsheetDialogBinding
import com.fuadhev.mynotes.databinding.FragmentToDoListBinding
import com.fuadhev.mynotes.databinding.SaveChangesDialogBinding
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.ui.adapter.ToDoClickListener
import com.fuadhev.mynotes.ui.adapter.TodoAdapter
import com.fuadhev.mynotes.ui.view.note_detail.NoteDetailFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ToDoListFragment : Fragment() {
    private lateinit var binding: FragmentToDoListBinding
    private val viewModel by viewModels<TodoListViewModel>()
    private val todoAdapter by lazy {
        TodoAdapter(object : ToDoClickListener {
            override fun todoClickListener(toDO: ToDo) {
                showBottomSheetDialog(toDO)

            }

            override fun changeisComplete(toDO: ToDo) {
                viewModel.updateToDo(toDO)
            }

        }, emptyList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_to_do_list, container, false)

        setRecyclerView()
        observes()
        setItemTouchListener()

        binding.fab.setOnClickListener {

            showBottomSheetDialog(null)
        }

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun setItemTouchListener(){
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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val toDo=todoAdapter.getToDoByPosition(position)
                viewModel.deleteToDo(toDo)
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

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
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
            todoAdapter.updateNotes(it)
        }

    }

    private fun showBottomSheetDialog(toDo:ToDo?) {

        val mDialog = Dialog(requireContext())
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        val dialogBinding = AddTodoBottomsheetDialogBinding.inflate(layoutInflater)
        mDialog.setContentView(dialogBinding.root)
        mDialog.setCancelable(true)
//        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (toDo!=null){
            dialogBinding.toDoTxt.setText(toDo.todo_txt)
            dialogBinding.dialogTxt.text="Update task"
        }
        dialogBinding.calendar.setOnClickListener {
            setAlarmWithManager()
        }

        dialogBinding.add.setOnClickListener {

            if (toDo==null){
                val todo = ToDo(0, dialogBinding.toDoTxt.text.toString(), false)
                viewModel.insertToDo(todo)

            }else{
                val todo = ToDo(toDo.id, dialogBinding.toDoTxt.text.toString(), false)
                viewModel.insertToDo(todo)
            }
            mDialog.dismiss()


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

    private fun setAlarmWithManager(){
        val calendar = Calendar.getInstance()

        // Date Picker ile tarih seçimi
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH+1, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                Log.e("day year", "$year ${month+1} $day", )

                // Time Picker ile saat seçimi
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)

                        Log.e("time", "$hourOfDay $minute ", )

                        setAlarmWithWorkManager(calendar)
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

    fun setAlarmWithWorkManager(alarmTime: Calendar) {
//        val currentTime = Calendar.getInstance()
//        if (currentTime.after(alarmTime)) {
//            // Geçmiş bir zaman seçildi, alarmı ayarlamıyoruz.
//            return
//        }
//
//        val timeDiffInMillis = alarmTime.timeInMillis - currentTime.timeInMillis
//
//        val inputData = Data.Builder()
//            .putString("ALARM_MESSAGE_KEY", "alarmMessage")
//            .build()
//
//        val constraints = androidx.work.Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//
//        val alarmRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
//            .setInitialDelay(timeDiffInMillis, TimeUnit.MILLISECONDS)
//            .setInputData(inputData)
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance(requireContext()).enqueueUniqueWork(
//            "ALARM_WORK_TAG",
//            ExistingWorkPolicy.REPLACE,
//            alarmRequest
//        )
    }

}