package com.example.learnit.signin

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.learnit.MainActivity
import com.example.learnit.PasswordResetFragment
import com.example.learnit.R
import com.example.learnit.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.toggleDarkmode.setOnClickListener {
            darkModeToggler()
        }
        binding.registerLinkBtn.setOnClickListener {
            val registerIntent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(registerIntent)
            finish()
        }
        binding.signInBtn.setOnClickListener {
            authUser()
        }
        binding.forgotPassword.setOnClickListener {
            forgotPassword()
        }

    }

    fun authUser(){
        val email = binding.emailEtLogin.text.toString().trim()
        val pass = binding.passwordEtLogin.text.toString().trim()
        if(email.isEmpty()){
            binding.emailEtLogin.error = "Please enter your email"
        }
        else if(pass.isEmpty()){
            binding.passwordEtLogin.error = "Please enter password"
        }
        else if (pass.isNotEmpty() && email.isNotEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    Log.e("Firebase login", "${task.exception}")
                    Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show()
                } else if (task.isSuccessful) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
    fun forgotPassword(){
        val passwordResetFragment = PasswordResetFragment()
        supportFragmentManager.beginTransaction().add(passwordResetFragment,"Password Reset").commit()
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