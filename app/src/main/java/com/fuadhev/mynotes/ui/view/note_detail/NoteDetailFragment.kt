package com.fuadhev.mynotes.ui.view.note_detail

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentNoteDetailBinding
import com.fuadhev.mynotes.databinding.SaveChangesDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {


    private lateinit var binding: FragmentNoteDetailBinding
    private val viewModel by viewModels<NoteDetailViewModel>()
    private val args by navArgs<NoteDetailFragmentArgs>()
    private var noteTxtisChange = true
    private var noteTitleisChange = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (findNavController().currentDestination?.id == R.id.noteDetailFragment) {
                    if (!noteTxtisChange || !noteTitleisChange) {
                        showDialog()
                    } else {
                        findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesListFragment())
                    }
                }
            }
        }
        // Geri tuşu olayını etkinleştirin
        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        binding.save.visibility = GONE

        binding.save.setOnClickListener {
            if (!noteTxtisChange || !noteTitleisChange) {
                updateNote()
            } else {
                findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesListFragment())
            }

        }
        binding.delete.setOnClickListener {
            showDeleteDialog()
        }
        binding.noteTitle.setOnFocusChangeListener { _, hasFocus ->
            binding.save.visibility = if (hasFocus) VISIBLE else GONE
        }
        binding.noteTxt.setOnFocusChangeListener { _, hasFocus ->
            binding.save.visibility = if (hasFocus) VISIBLE else GONE
        }

        val note = args.note

        binding.noteTitle.setText(note.note_title)
        binding.noteTxt.setText(note.note_txt)

        binding.back.setOnClickListener {
            if (!noteTxtisChange || !noteTitleisChange) {
                showDialog()
            } else {
                findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesListFragment())
            }
        }

        textChangeListener()


    }

    private fun textChangeListener() {
        binding.noteTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newTitle = s.toString()
                noteTitleisChange = args.note.note_title == newTitle
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.noteTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newnotetxt = s.toString()

                noteTxtisChange = args.note.note_txt == newnotetxt
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    private fun showDeleteDialog() {

        val dialogBinding = SaveChangesDialogBinding.inflate(layoutInflater)
        val mDialog = Dialog(requireContext())
        mDialog.setContentView(dialogBinding.root)
        mDialog.setCancelable(false)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.dInfo.setText(R.string.delete)
        binding.noteDetail.alpha = 0.5f

        dialogBinding.yes.setOnClickListener {
            val currentNode=args.note
            val note = com.fuadhev.mynotes.entity.Note(
                args.note.id,
                binding.noteTitle.text.toString(),
                binding.noteTxt.text.toString(),
                currentNode.day,
                currentNode.month,
                currentNode.clock,
                currentNode.year
            )
            viewModel.deleteNote(note)
            binding.noteDetail.alpha = 1f
            findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesListFragment())
            mDialog.dismiss()
        }

        dialogBinding.no.setOnClickListener {
            binding.noteDetail.alpha = 1f
            mDialog.dismiss()
        }

        mDialog.create()
        mDialog.show()
    }


    private fun showDialog() {
        val dialogBinding = SaveChangesDialogBinding.inflate(layoutInflater)
        val mDialog = Dialog(requireContext())
        mDialog.setContentView(dialogBinding.root)
        mDialog.setCancelable(false)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.dInfo.setText(R.string.save_change)
        binding.noteDetail.alpha = 0.5f

        dialogBinding.yes.setOnClickListener {
            updateNote()
            binding.noteDetail.alpha = 1f
            mDialog.dismiss()
        }

        dialogBinding.no.setOnClickListener {
            binding.noteDetail.alpha = 1f
            mDialog.dismiss()
        }

        mDialog.create()
        mDialog.show()
    }

    private fun updateNote(){
        val currentNode = args.note
        val now = Calendar.getInstance()

        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)

        val day = now.get(Calendar.DAY_OF_MONTH)
        val month = now.get(Calendar.MONTH) + 1

        val clock = if (minute < 10) {
            "$hour:0$minute"
        } else {
            "$hour:$minute"

        }

        val year = now.get(Calendar.YEAR)
        val note = com.fuadhev.mynotes.entity.Note(
            currentNode.id,
            binding.noteTitle.text.toString(),
            binding.noteTxt.text.toString(),
            day,
            month,
            clock,
            year
        )
        viewModel.updateNote(note)
        findNavController().navigate(NoteDetailFragmentDirections.actionNoteDetailFragmentToNotesListFragment())
    }


}