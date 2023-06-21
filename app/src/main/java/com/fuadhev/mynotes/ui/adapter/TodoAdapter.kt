package com.fuadhev.mynotes.ui.adapter

import android.animation.Animator
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fuadhev.mynotes.databinding.NoteItemBinding
import com.fuadhev.mynotes.databinding.TodoItemBinding
import com.fuadhev.mynotes.entity.ToDo

class TodoAdapter(private var todosList: List<ToDo>) :
    RecyclerView.Adapter<TodoAdapter.ViewHolder>() {


    inner class ViewHolder(val view: TodoItemBinding) : RecyclerView.ViewHolder(view.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: TodoItemBinding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todosList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateNotes(newTodosList: List<ToDo>) {
        this.todosList = newTodosList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toDo = todosList[position]
        val b = holder.view
        var isAnimationForward = true
        b.lottieAnim.repeatCount = 0

        b.lottieAnim.setOnClickListener {
            val currentProgress = b.lottieAnim.progress
            if (currentProgress == 0.0f) {
                Log.e("totrue","totrue")

                // Animasyon başlangıcında veya geriye doğru ilerlerken
                b.lottieAnim.speed = 1.0f // İleri yönde oynatma hızını ayarla
                b.lottieAnim.resumeAnimation() // Animasyonu devam ettir
                b.lottieAnim.playAnimation() // Animasyonu başlat
                b.lottieAnim.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        b.lottieAnim.pauseAnimation() // Animasyonu duraklat
                        b.lottieAnim.progress = 1f // Progresi 0.5'e ayarla
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
            } else if (currentProgress == 1f) {
                Log.e("tofalse","tofalse")
                // Animasyon ileri yönde ilerlerken
                b.lottieAnim.speed = -1.0f // Geriye doğru oynatma hızını ayarla
                b.lottieAnim.resumeAnimation() // Animasyonu devam ettir
                b.lottieAnim.playAnimation() // Animasyonu başlat
                b.lottieAnim.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        b.lottieAnim.pauseAnimation() // Animasyonu duraklat
                        b.lottieAnim.progress = 0.0f // Progresi 0.0'a ayarla
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
            }
        }

    }


}
