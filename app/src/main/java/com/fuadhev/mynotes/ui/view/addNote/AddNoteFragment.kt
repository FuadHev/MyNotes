package com.fuadhev.mynotes.ui.view.addNote

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentAddNoteBinding
import com.fuadhev.mynotes.entity.Note
import java.util.Calendar


class AddNoteFragment : Fragment() {


    private lateinit var binding:FragmentAddNoteBinding
    private val viewModel by viewModels<AddNoteViewModel> {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_note, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val now=Calendar.getInstance()
//
//        @ColumnInfo(name = "day") val day: Int,
//        @ColumnInfo(name = "month") val month: String,
//        @ColumnInfo(name = "clock") val clock: Int,
//        @ColumnInfo(name = "year") val year: Int

        binding.save.setOnClickListener {

            val hour = now.get(Calendar.HOUR_OF_DAY)
            val second = now.get(Calendar.MINUTE)

            val noteTitle=binding.noteTitle.text.toString()
            val noteTxt=binding.noteTxt.text.toString()
            val day=now.get(Calendar.DAY_OF_MONTH)
            val month=now.get(Calendar.MONTH)+1
            val clock="$hour:$second"
            val year=now.get(Calendar.YEAR)

            val note=Note(0,noteTitle,noteTxt,day,month,clock,year)
            viewModel.insertNote(note)

        }

        binding.back.setOnClickListener {
            findNavController().navigate(AddNoteFragmentDirections.actionAddNoteFragmentToNotesListFragment())
        }
    }


}