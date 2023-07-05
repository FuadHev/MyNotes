package com.fuadhev.mynotes.ui.view.note_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.fuadhev.mynotes.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class NoteListViewModel @Inject constructor(private val repo:Repository):ViewModel() {


    val allNoteLiveData=repo.getAllNotes()

}