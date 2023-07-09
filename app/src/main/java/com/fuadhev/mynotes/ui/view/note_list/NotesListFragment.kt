package com.fuadhev.mynotes.ui.view.note_list

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentNotesListBinding
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.ui.adapter.NoteAdapter
import com.fuadhev.mynotes.ui.adapter.PostClickListener
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
        allNoteList=ArrayList()
        setSearchView()
        viewModel.searchNoteList.observe(viewLifecycleOwner){
            noteAdapter.updateNotes(it)
        }
        viewModel.allNoteLiveData.observe(viewLifecycleOwner) {
            noteAdapter.updateNotes(it)
            allNoteList=it
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(NotesListFragmentDirections.actionNotesListFragmentToAddNoteFragment())
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
                    noteAdapter.updateNotes(allNoteList)
                }
                return true
            }


        })

    }

    private fun setRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.adapter = noteAdapter
    }

}