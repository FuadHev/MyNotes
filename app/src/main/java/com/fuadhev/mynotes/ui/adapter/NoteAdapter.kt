package com.fuadhev.mynotes.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fuadhev.mynotes.databinding.NoteItemBinding
import com.fuadhev.mynotes.entity.Note

class NoteAdapter(private val postClickListener: PostClickListener,private var notesList: List<Note>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val colors = listOf("#FF9E9E", "#91F48F", "#FFF599", "#9EFFFF", "#B69CFF")
    private var colorCounter = 0
    inner class ViewHolder(val view: NoteItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: NoteItemBinding = NoteItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(notesList: List<Note>){
        this.notesList=notesList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        val b = holder.view

        b.noteTitle.text = note.note_title


        val color = colors[colorCounter % colors.size]
        b.noteCard.setCardBackgroundColor(android.graphics.Color.parseColor(color))
        colorCounter++

        val monthNumber = note.month
        var month = ""
        when (monthNumber) {
            1 -> month = "January"
            2 -> month = "February"
            3 -> month = "March"
            4 -> month = "April"
            5 -> month = "May"
            6 -> month = "June"
            7 -> month = "July"
            8 -> month = "August"
            9 -> month = "September"
            10 -> month = "October"
            11 -> month = "November"
            12 -> month = "December"

        }

        b.noteCard.setOnClickListener {
            postClickListener.postClickListener(note)
        }

        b.createdTime.text = "${note.day} $month ${note.clock}"
    }


}

interface PostClickListener{
    fun postClickListener(currentNote:Note)
}