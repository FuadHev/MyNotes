package com.fuadhev.mynotes.ui.view.note_list

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentNotesListBinding
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.ui.adapter.NoteAdapter
import com.fuadhev.mynotes.ui.adapter.PostClickListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesListFragment : Fragment() {


    private lateinit var binding: FragmentNotesListBinding
    private val noteAdapter by lazy {
        NoteAdapter(object : PostClickListener {
            override fun postClickListener(currentNote: Note) {
                findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToNoteDetailFragment(currentNote))
            }

        }, emptyList())
    }
    private val viewModel by viewModels<NoteListViewModel> ()
    private lateinit var allNoteList:List<Note>

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_list, container, false)
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setItemTouchListener()
        allNoteList=ArrayList()
        setSearchView()
        observes()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToAddNoteFragment())
        }

    }

    private fun observes() {

        viewModel.searchNoteList.observe(viewLifecycleOwner){
            noteAdapter.updateNotes(it)
            if (it.isNotEmpty()){
                binding.lottie.visibility= GONE
                binding.imgInfo.visibility=GONE
            }else{
                binding.lottie.visibility= VISIBLE
                binding.imgInfo.visibility=GONE
            }

        }
        viewModel.allNoteLiveData.observe(viewLifecycleOwner) {
            noteAdapter.updateNotes(it)

            if (it.isNotEmpty()){
                allNoteList=it
                binding.rv.visibility=VISIBLE
                binding.imgInfo.visibility= GONE
                binding.lottie.visibility= GONE
            }else{
                binding.rv.visibility=GONE
                binding.imgInfo.visibility= VISIBLE
                binding.lottie.visibility= GONE
            }

        }

    }

    private fun setSearchView(){

        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.trim() != "") {
                    binding.searchview.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.trim()!= "") {
                    viewModel.searhNote(newText)
                }else{
                    binding.imgInfo.visibility= GONE
                    binding.lottie.visibility= GONE
                    if(allNoteList.isEmpty()){
                        binding.imgInfo.visibility= VISIBLE
                    }
                    noteAdapter.updateNotes(allNoteList)
                }
                return true
            }


        })

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
                val note=noteAdapter.getNoteByPosition(position)
                viewModel.deleteNote(note)
                showDeleteSnackBar(note)
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
    private fun showDeleteSnackBar(note: Note) {
        Snackbar.make(requireView(), "Meal Deleted", Snackbar.LENGTH_LONG).apply {
            setAction(
                "Undo"
            ) {
                viewModel.updateNote(note)

            }.show()
        }
    }

    private fun setRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.adapter = noteAdapter
    }

}