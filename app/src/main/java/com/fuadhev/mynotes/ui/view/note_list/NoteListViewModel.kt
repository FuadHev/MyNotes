package com.fuadhev.mynotes.ui.view.note_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.fuadhev.mynotes.repo.Repository

class NoteListViewModel(app: Application):AndroidViewModel(app) {

    private val repo= Repository(app.applicationContext)


    val allNoteLiveData=repo.getAllNotes()

}