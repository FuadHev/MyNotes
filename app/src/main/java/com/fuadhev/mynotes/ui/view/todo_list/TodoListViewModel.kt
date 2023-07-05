package com.fuadhev.mynotes.ui.view.todo_list

import androidx.lifecycle.ViewModel
import com.fuadhev.mynotes.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(private val repo:Repository):ViewModel() {
}