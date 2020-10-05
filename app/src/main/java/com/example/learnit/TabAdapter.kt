package com.example.learnit

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.learnit.home.HomeFragment
import com.example.learnit.profile.ProfileFragment

class TabAdapter(context: Context,fm:FragmentManager,internal var totalTabs: Int): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0-> return HomeFragment()
            1-> return  QuizFragment()
            2 -> return ProfileFragment()
            else -> return HomeFragment()
        }
    }
}