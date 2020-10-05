package com.example.learnit.signin

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.learnit.MainActivity
import com.example.learnit.R
import com.example.learnit.databinding.ActivityRegisterBinding
import com.example.learnit.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        auth = FirebaseAuth.getInstance()
        registerBinding.toggleDarkmode.setOnClickListener {
            val loginActivity = LoginActivity()
            darkModeToggler()
        }
        registerBinding.loginLinkBtn.setOnClickListener {
            val registerIntent = Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(registerIntent)
            finish()
        }
        registerBinding.signUpBtn.setOnClickListener {
            registerUser()
        }
    }

    fun darkModeToggler() {
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
    fun registerUser(){
        val email = registerBinding.emailEtRg.text.toString().trim()
        val pass1 = registerBinding.passwordEtRg.text.toString().trim()
        val pass2 = registerBinding.passwordConfirmEtRg.text.toString().trim()
        if(pass1.equals(pass2)){
            auth.createUserWithEmailAndPassword(email,pass2).addOnCompleteListener {
                    task ->
                if( !task.isSuccessful){
                    Log.e("Firebase","${task.result}")
                    Toast.makeText(this,"Error occured",Toast.LENGTH_SHORT).show()
                }else{
                    startActivity(Intent(this@RegisterActivity,ProfileActivity::class.java))
                    finish()
                }
            }
        }else{
            registerBinding.passwordConfirmTextInput.error = "Both Passwords Should Match"
        }

    }


}