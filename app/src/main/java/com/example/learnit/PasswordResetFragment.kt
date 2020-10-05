package com.example.learnit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.learnit.databinding.FragmentPasswordResetBinding
import com.google.firebase.auth.FirebaseAuth

class PasswordResetFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var passwordResetFragment: FragmentPasswordResetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        passwordResetFragment = FragmentPasswordResetBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  passwordResetFragment.root
        passwordResetFragment.submitBtn.setOnClickListener {
            firebaseAuth.sendPasswordResetEmail(passwordResetFragment.emailEtLogin.text.toString()).addOnCompleteListener {
                task ->
                if (task.isSuccessful){
                    Toast.makeText(context, "Mail sent", Toast.LENGTH_SHORT).show()
                }else if (!task.isSuccessful){
                    Toast.makeText(context, "Check mail id is it registered", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordResetFragment().apply {
            }
    }
}