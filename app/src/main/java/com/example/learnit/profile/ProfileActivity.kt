package com.example.learnit.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.example.learnit.databinding.ActivityUserProfileBinding
import com.example.learnit.signin.LoginActivity
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.snapchat.kit.sdk.SnapLogin
import com.snapchat.kit.sdk.core.controller.LoginStateController
import com.snapchat.kit.sdk.core.controller.LoginStateController.OnLoginStateChangedListener
import com.snapchat.kit.sdk.login.models.UserDataResponse
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback
import java.io.IOException
import java.io.InputStream


class ProfileActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseStorage:FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var profileBinding: ActivityUserProfileBinding
    private val PICK_IMAGE = 123
    private lateinit var imagePath: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(profileBinding.root)
        auth=FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            finish()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        setSupportActionBar(profileBinding.toolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = ""
         }
        profileBinding.appbar.bringToFront()
        profileBinding.toolbar.getNavigationIcon()?.setColorFilter(
            getResources().getColor(R.color.common_google_signin_btn_text_light),
            PorterDuff.Mode.SRC_ATOP
        );

        profileBinding.profilePicUpdate.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE)
        }

        databaseReference = FirebaseDatabase.getInstance().getReference()
        val firebaseUser = auth.currentUser
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.getReference()
        getUserInformation()
        if (firebaseUser?.email != null) {
            profileBinding.emailEtProfile.setText(firebaseUser.email.toString())
        }
        profileBinding.updateProfileBtn.setOnClickListener {
            sendUserInformation()
        }
        profileBinding.snapChatLoginBtn.setOnClickListener {
            SnapLogin.getAuthTokenManager(this@ProfileActivity).startTokenGrant()
            getUserInfoSnapChat()
        }
        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(snapchatLoginState())
    }

    fun snapchatLoginState(): LoginStateController.OnLoginStateChangedListener{
        val loginStateController:LoginStateController.OnLoginStateChangedListener = object: OnLoginStateChangedListener{
            override fun onLoginSucceeded() {
                Toast.makeText(this@ProfileActivity,"Connected successfully",Toast.LENGTH_SHORT).show()
            }

            override fun onLoginFailed() {
                Toast.makeText(this@ProfileActivity,"Connecting Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onLogout() {
               profileBinding.avatar.visibility = View.GONE
                profileBinding.displayNameSnapchat.visibility = View.GONE
            }

        }
        return loginStateController

    }

    fun getUserInfoSnapChat(){
        val isLoggedIn =SnapLogin.isUserLoggedIn(this)
        if(isLoggedIn){
            val query = "{me{bitmoji{avatar,selfie,id},displayName,externalId}}"

            SnapLogin.fetchUserData(this,query,null,object:FetchUserDataCallback{
                override fun onSuccess(response: UserDataResponse?) {
                    if(response == null || response.data == null)
                        return
                    val meData = response.data.me ?: return
                    profileBinding.avatar.visibility = View.VISIBLE
                    profileBinding.displayNameSnapchat.visibility = View.VISIBLE
                    Glide.with(this@ProfileActivity)
                        .load(meData.bitmojiData.avatar)
                        .into(profileBinding.profilePicUpdate)
                    profileBinding.snapChatLoginBtn.visibility = View.GONE
                }

                override fun onFailure(isNetworkError: Boolean, statusCode: Int) {

                }

            })
        }
    }

    fun sendUserInformation(){
        val user = auth.getCurrentUser()
        val name = profileBinding.nameEtProfile.text.toString().trim()
        val username = profileBinding.usernameEtProfile.text.toString().trim()
        val phone = profileBinding.phoneEtProfile.text.toString().trim()
        if(name.isEmpty()){
            profileBinding.nameEtProfile.error = "Enter your name"
        }
        else if(username.isEmpty()){
            profileBinding.usernameEtProfile.error = "Enter your user Name"
        }
        else if(phone.isEmpty()){
            profileBinding.phoneEtProfile.error = "Enter your phone number"
        }
       else {
            val userInformation = UserInformation(name, username, phone, user?.email.toString())
            user?.uid?.let { databaseReference.child(it).setValue(userInformation) }
            val imageReference = auth.uid?.let { storageReference.child(it).child("images").child("profile_pic") }
            val uploadTask = imageReference?.putFile(imagePath)
            uploadTask?.addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(this@ProfileActivity, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            })?.addOnSuccessListener {
                Toast.makeText(this, "Profile picture uploaded", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(getApplicationContext(), "User information updated", Toast.LENGTH_LONG)
                .show()
            getUserInformation()
        }
    }
    fun getUserInformation(){
        val user = auth.getCurrentUser()
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference()
            databaseReference.child(user.uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    profileBinding.nameEtProfile.setText(snapshot.child("name").value.toString())
                    profileBinding.usernameEtProfile.setText(snapshot.child("username").value.toString())
                    profileBinding.phoneEtProfile.setText(snapshot.child("phoneNo").value.toString())
                    val imageReference = auth.uid?.let {
                        storageReference.child(it).child("images").child(
                            "profile_pic"
                        )
                    }
                    Glide.with(this@ProfileActivity)
                        .load(imageReference)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .into(profileBinding.profilePicUpdate)
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
                .into(profileBinding.profilePicUpdate)
            storageReference.child(user.uid)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataResult: Intent?) {

        if(requestCode.equals(PICK_IMAGE) && resultCode.equals(RESULT_OK) && dataResult != null && dataResult.data != null){
            imagePath = dataResult.data!!
            try{
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePath)
                profileBinding.profilePicUpdate.setImageBitmap(bitmap)
            }catch (e: IOException){
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, dataResult)
    }


}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

        // Register FirebaseImageLoader to handle StorageReference
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}