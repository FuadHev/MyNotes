package com.fuadhev.mynotes.ui.view.addNote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentAddNoteBinding
import com.fuadhev.mynotes.entity.Note
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.math.min

@AndroidEntryPoint
class AddNoteFragment : Fragment() {


    private lateinit var binding: FragmentAddNoteBinding
    private val viewModel by viewModels<AddNoteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val now = Calendar.getInstance()

        binding.save.setOnClickListener {

            val noteTitle = binding.noteTitle.text.toString()
            val noteTxt = binding.noteTxt.text.toString()
            if(noteTitle.trim()!=""&& noteTxt.trim()!=""){
                val hour = now.get(Calendar.HOUR_OF_DAY)
                val minute = now.get(Calendar.MINUTE)


                val day = now.get(Calendar.DAY_OF_MONTH)
                val month = now.get(Calendar.MONTH) + 1

                val clock = if (minute < 10) {
                    Log.e("time", "$hour 0$minute")
                    "$hour:0$minute"
                } else {
                    "$hour:$minute"

                }

                val year = now.get(Calendar.YEAR)
                val note = Note(0, noteTitle, noteTxt, day, month, clock, year)
                viewModel.insertNote(note)
                findNavController().navigate(AddNoteFragmentDirections.actionAddNoteFragmentToNotesListFragment())
            }else{
                Toast.makeText(requireContext(), "title or note cannot be left blank", Toast.LENGTH_SHORT).show()
            }




        }

        binding.back.setOnClickListener {
            findNavController().navigate(AddNoteFragmentDirections.actionAddNoteFragmentToNotesListFragment())
        }
    }


}