package com.fuadhev.mynotes.ui.view.note_detail

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.fuadhev.mynotes.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(private val repo:Repository) :ViewModel(){

    fun updateNote(note:com.fuadhev.mynotes.entity.Note){
        viewModelScope.launch(IO) {
            repo.insertNote(note)
        }
    }
    fun deleteNote(note:com.fuadhev.mynotes.entity.Note){
        viewModelScope.launch(IO) {
            repo.deleteNote(note)
        }
    }


}