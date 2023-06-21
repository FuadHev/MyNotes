package com.fuadhev.mynotes.ui.view.note_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentNoteDetailBinding
import com.fuadhev.mynotes.databinding.FragmentNotesListBinding


class NoteDetailFragment : Fragment() {


    private lateinit var binding:FragmentNoteDetailBinding
    private val args by navArgs<NoteDetailFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_note_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val note=args.note

        binding.noteTitle.setText(note.note_title)
        binding.noteTxt.setText(note.note_txt)


//        binding.


    }




}