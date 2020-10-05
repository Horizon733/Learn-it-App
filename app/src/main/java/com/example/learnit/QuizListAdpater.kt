package com.example.learnit

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class QuizListAdpater(var context: Context, var quizList: List<QuizData>):
    RecyclerView.Adapter<QuizListAdpater.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_list_item, parent, false)
        return QuizListAdpater.QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizList[position]
        holder.quizName.text = quiz.name
        holder.startButton.setOnClickListener {
            val intent = Intent(context,QuizViewActivity::class.java)
            intent.putExtra("forms",quiz.quizLink)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        quizList.isEmpty() ?: return -1
        return quizList.size
    }
    class QuizViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        val quizName = itemview.findViewById<TextView>(R.id.quiz_name)
        val quizProgress = itemview.findViewById<ProgressBar>(R.id.progress_bar_quiz)
        val startButton = itemView.findViewById<Button>(R.id.start_button)
    }
}