package com.fuadhev.mynotes.ui.adapter

import android.animation.Animator
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.fuadhev.mynotes.databinding.NoteItemBinding
import com.fuadhev.mynotes.databinding.TodoItemBinding
import com.fuadhev.mynotes.entity.ToDo

class TodoAdapter(private val toDoClickListener: ToDoClickListener,private var todosList: List<ToDo>) :
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
//        var isAnimationForward = true
        if (toDo.isComplete){
            b.lottieAnim.progress=1.0f
        }else{
            b.lottieAnim.progress=0.0f
        }
        b.textView2.text=toDo.todo_txt
        b.lottieAnim.repeatCount = 0

        if (toDo.time!=""){
            b.time.text=toDo.time
        }else{
            b.time.visibility=GONE
        }

        if(toDo.isAlarm){
            b.alarm.visibility=VISIBLE
        }else{
            b.alarm.visibility= GONE
        }

        b.todoCard.setOnClickListener {
            toDoClickListener.todoClickListener(toDo)
        }

        b.lottieAnim.setOnClickListener {
         setClickAnimation(b.lottieAnim,toDo)
        }

    }
    fun getToDoByPosition(position: Int): ToDo {
        return todosList[position]
    }

    private fun setClickAnimation(lottie:LottieAnimationView,toDo:ToDo){
        val currentProgress = lottie.progress
        if (currentProgress == 0.0f) {
            Log.e("totrue","totrue")

            // Animasyon başlangıcında veya geriye doğru ilerlerken
            lottie.speed = 1.0f // İleri yönde oynatma hızını ayarla
            lottie.resumeAnimation() // Animasyonu devam ettir
            lottie.playAnimation() // Animasyonu başlat
            lottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    lottie.pauseAnimation() // Animasyonu duraklat
                    lottie.progress = 1f // Progresi 0.5'e ayarla
                    toDo.isComplete=true
                    toDoClickListener.changeisComplete(toDo)
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        } else   {
            Log.e("tofalse","tofalse")
            // Animasyon ileri yönde ilerlerken
            lottie.speed = -1.0f // Geriye doğru oynatma hızını ayarla
            lottie.resumeAnimation() // Animasyonu devam ettir
            lottie.playAnimation() // Animasyonu başlat
            lottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    lottie.pauseAnimation() // Animasyonu duraklat
                    lottie.progress = 0.0f // Progresi 0.0'a ayarla
                    toDo.isComplete=false
                    toDoClickListener.changeisComplete(toDo)
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }
    }


}
interface ToDoClickListener{
   fun todoClickListener(toDO:ToDo)

   fun changeisComplete(toDO: ToDo)

}
