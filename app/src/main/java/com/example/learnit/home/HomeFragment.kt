package com.example.learnit.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.learnit.Data
import com.example.learnit.R
import com.example.learnit.SubjectListAdapter
import com.example.learnit.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = fragmentHomeBinding.root
        val list = arrayListOf<Data>(
           Data("Earth"
               ,R.drawable.ic_earth
               ,R.drawable.ic_comet
               ,R.drawable.ic_telescope
               ,"52287450875")
            ,Data("Sun"
            ,R.drawable.ic_earth
            ,R.drawable.ic_comet
            ,R.drawable.ic_telescope
            ,"52287450875")
            ,Data("Geography"
                ,R.drawable.ic_compass
                ,R.drawable.ic_big_compass
                ,R.drawable.ic_globe
                ,"52287450875")
            ,Data("Chemical"
                ,R.drawable.ic_chemical_bonds
                ,R.drawable.ic_flask
                ,R.drawable.ic_test_tubes
                ,"52287450875")
            ,Data("Mars"
                ,R.drawable.ic_earth
                ,R.drawable.ic_comet
                ,R.drawable.ic_telescope
                ,"52287450875")
            ,Data("Venus"
                ,R.drawable.ic_compass
                ,R.drawable.ic_comet
                ,R.drawable.ic_telescope
                ,"52287450875")
            ,Data("Venus"
                ,R.drawable.ic_compass
                ,R.drawable.ic_comet
                ,R.drawable.ic_telescope
                ,"52287450875")
        )
        val subjectListAdapter = context?.let { SubjectListAdapter(it,list) }
        fragmentHomeBinding.subjectList.adapter = subjectListAdapter
        val gridLayoutManager = GridLayoutManager(context!!.applicationContext,2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        fragmentHomeBinding.subjectList.layoutManager = gridLayoutManager
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }
}