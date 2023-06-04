package com.fuadhev.mynotes.ui.view.todo_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fuadhev.mynotes.R
import com.fuadhev.mynotes.databinding.FragmentToDoListBinding


class ToDoListFragment : Fragment() {
    private lateinit var binding:FragmentToDoListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_to_do_list, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

}