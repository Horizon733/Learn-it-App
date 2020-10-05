package com.example.learnit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnit.databinding.FragmentQuizBinding


class QuizFragment : Fragment() {
    private lateinit var fragmentQuizBinding: FragmentQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentQuizBinding = FragmentQuizBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = fragmentQuizBinding.root
        val quizList = ArrayList<QuizData>()
        quizList.add(QuizData("Earth Quiz","https://forms.gle/hrr6VGNNsqMAKrXo7"))
        quizList.add(QuizData("Sun Quiz","https://forms.gle/vvBa6t3ab94mSW418"))
        quizList.add(QuizData("Geography Quiz","https://forms.gle/WxNrh9jWpDkNcQ5g9"))
        val quizAdapter = QuizListAdpater(activity!!,quizList)
        fragmentQuizBinding.quizList.adapter = quizAdapter
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        fragmentQuizBinding.quizList.layoutManager = linearLayoutManager

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizFragment().apply {

            }
    }
}