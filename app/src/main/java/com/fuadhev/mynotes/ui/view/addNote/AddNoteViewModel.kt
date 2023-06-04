package com.fuadhev.mynotes.ui.view.addNote

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.repo.Repository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AddNoteViewModel(app:Application):AndroidViewModel(app) {

// ViewModelProvider.AndroidViewModelFactory(requireActivity().application)

    private val repo=Repository(app.applicationContext)




    fun insertNote(note:Note){
        viewModelScope.launch(IO) {
            repo.insertNote(note)
        }
    }
//
//    fun deleteNote(note:Note){
//        viewModelScope.launch(IO) {
//            repo.deleteNote(note)
//        }
//    }


}