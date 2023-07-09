package com.fuadhev.mynotes.ui.view.note_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class NoteListViewModel @Inject constructor(private val repo:Repository):ViewModel() {


    val allNoteLiveData=repo.getAllNotes()
    val searchNoteList=MutableLiveData<List<Note>>()


    fun searhNote(title:String){
        viewModelScope.launch(IO) {
           val notelist= repo.getNotesByTitle(title)
            searchNoteList.postValue(notelist)
        }

    }

}