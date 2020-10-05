package com.example.learnit

import android.content.Intent
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.learnit.databinding.ActivityMainBinding
import com.example.learnit.signin.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.addAuthStateListener(authStateListener)
        mainBinding.tablayout.addTab(mainBinding.tablayout.newTab())
        mainBinding.tablayout.getTabAt(0)?.setIcon(R.drawable.ic_icon_metro_books)
        mainBinding.tablayout.addTab(mainBinding.tablayout.newTab())
        mainBinding.tablayout.getTabAt(1)?.setIcon(R.drawable.ic_quiz)
        mainBinding.tablayout.addTab(mainBinding.tablayout.newTab())
        mainBinding.tablayout.getTabAt(2)?.setIcon(R.drawable.ic_icon_awesome_user_circle)

        mainBinding.viewpager.adapter = TabAdapter(this,supportFragmentManager,mainBinding.tablayout.tabCount)
        mainBinding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mainBinding.tablayout))
       mainBinding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
               if(tab != null){
                   mainBinding.viewpager.currentItem = tab.position
               }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        mainBinding.darkToggler.setOnClickListener{
            darkModeToggler()
        }
    }
    val authStateListener = FirebaseAuth.AuthStateListener {
        firebaseAuth ->
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else if(firebaseUser != null){
            val name = firebaseUser.email
        }
    }
    fun darkModeToggler(){
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (isNightTheme) {
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }

        }
    }

}