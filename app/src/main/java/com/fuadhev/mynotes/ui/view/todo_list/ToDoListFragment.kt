package com.fuadhev.mynotes.ui.view.todo_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentToDoListBinding
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.ui.adapter.TodoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDoListFragment : Fragment() {
    private lateinit var binding:FragmentToDoListBinding
    private val todoAdapter by lazy {
        TodoAdapter(emptyList())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_to_do_list, container, false)

        setRecyclerView()
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun setRecyclerView(){
        binding.rv.layoutManager=LinearLayoutManager(requireActivity())
        val list=ArrayList<ToDo>()
        list.add(ToDo(0,"",false))
        todoAdapter.updateNotes(list)
        binding.rv.adapter=todoAdapter
    }

}