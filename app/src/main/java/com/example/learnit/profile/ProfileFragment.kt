package com.example.learnit.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.learnit.R
import com.example.learnit.databinding.ActivityUserProfileBinding
import com.example.learnit.databinding.FragmentProfileBinding
import com.example.learnit.signin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)
        auth=FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            activity!!.finish()
            startActivity(Intent(context, LoginActivity::class.java))
        }
        databaseReference = FirebaseDatabase.getInstance().getReference()
        val firebaseUser = auth.currentUser
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReference()
        getUserInformation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = fragmentProfileBinding.root
        fragmentProfileBinding.profileActivityOpener.setOnClickListener {
            activity?.startActivity(Intent(context,ProfileActivity::class.java))
        }

        return view
    }
    fun getUserInformation(){
        val user = auth.getCurrentUser()
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference()
            databaseReference.child(user.uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    fragmentProfileBinding.nameTvProfileFrag.setText(snapshot.child("name").value.toString())
                    val imageReference = auth.uid?.let {
                        storageReference.child(it).child("images").child(
                            "profile_pic"
                        )
                    }
                    context?.let {
                        Glide.with(it)
                            .load(imageReference)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .into(fragmentProfileBinding.picProfileFrag)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Data", "${error.message}")
                }
            })
            val imageReference = auth.uid?.let { storageReference.child(it).child("images").child("profile_pic")}
            Glide.with(this)
                .load(imageReference)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(fragmentProfileBinding.picProfileFrag)
            storageReference.child(user.uid)

        }

    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {

            }
    }
}