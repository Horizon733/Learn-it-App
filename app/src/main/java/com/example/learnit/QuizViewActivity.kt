package com.example.learnit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learnit.databinding.ActivityQuizViewBinding

class QuizViewActivity : AppCompatActivity() {
    private lateinit var quizViewBinding: ActivityQuizViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_view)
        quizViewBinding = ActivityQuizViewBinding.inflate(layoutInflater)
        val intentData = intent.extras?.get("forms").toString()
        quizViewBinding.webview.loadUrl(intentData)
    }
}