package com.fuadhev.mynotes.ui.view.addNote

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(private val repo:Repository):ViewModel() {

// ViewModelProvider.AndroidViewModelFactory(requireActivity().application)






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