package com.fuadhev.mynotes.ui.view.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val repo:Repository):ViewModel() {

    val getAllTodos=repo.getAllToDos()

    fun insertToDo(toDo:ToDo){
        viewModelScope.launch(IO){
            repo.insertToDo(toDo)
        }
    }
    fun updateToDo(toDo:ToDo){
        viewModelScope.launch(IO){
            repo.updateToDo(toDo)
        }
    }
    fun deleteToDo(toDo: ToDo){
        viewModelScope.launch(IO) {
            repo.deleteToDo(toDo)
        }
    }

}