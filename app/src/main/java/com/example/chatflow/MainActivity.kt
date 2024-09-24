package com.example.chatflow

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract.Root
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.widget.TextView
import android.widget.Toast
import com.example.chatflow.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
import java.net.URI
import java.net.URL
import java.util.UUID

class MainActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var realtimeDb : DatabaseReference
    private var imageUri :Uri? = null
    private lateinit var binding: ActivityMainBinding
    private var storage =FirebaseStorage.getInstance().reference
    companion object{
        private const val REQUEST_IMAGE_PICK=0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,allUserActivity::class.java))

        }

     supportActionBar?.hide()
      binding.loginPage.setOnClickListener {
          startActivity(Intent(this,loginActivity::class.java))
      }
        binding.profilePicture.setOnClickListener {
            pickImageFromGallery()

        }

    binding.signupButton.setOnClickListener{
        var username = binding.username.text.toString()
        var email = binding.emailEdittext.text.toString()
        var password = binding.passwordEdittext.text.toString()
        auth = FirebaseAuth.getInstance()

        if (imageUri==null){
            Toast.makeText(this,"Please Upload Your Image",Toast.LENGTH_SHORT).show()
        }

       else{

            if (username.isEmpty()&& email.isEmpty()&& password.isEmpty()){
                Toast.makeText(this,"Please Create Your Account First😇",Toast.LENGTH_LONG).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email , password).addOnSuccessListener {
                    saveUserToRealtimeDb()

                    zegoCloudFuction()    // function for ZEGOCLOUD Video and Audio service

                    startActivity(Intent(this,loginActivity::class.java))

                }
            }
        }



    }


    }

    private fun pickImageFromGallery() {
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== REQUEST_IMAGE_PICK || resultCode==Activity.RESULT_OK) {

                imageUri = data?.data
                Picasso.get().load(imageUri).into(binding.profilePicture)


        }

    }


    fun saveUserToRealtimeDb(){
        var userUid = auth.currentUser!!.uid

        realtimeDb = FirebaseDatabase.getInstance().getReference("AllUser").child(userUid)

        var username = binding.username.text.toString()
        var email = binding.emailEdittext.text.toString()
        var storageRef = storage.child("images/${UUID.randomUUID()}")
        storageRef.putFile(imageUri!!).addOnSuccessListener {task->

             task.storage.downloadUrl.addOnSuccessListener {uri->
                 var imageUrlLink = uri.toString()
                 var userData = UserData(username,userUid,email,imageUrlLink)
                 realtimeDb.setValue(userData)

             }
        }.addOnFailureListener{

        }
    }
    fun zegoCloudFuction(){
       var userEmail : String= binding.emailEdittext.text.toString()
        var userName  = binding.username.text.toString()
        val config = ZegoUIKitPrebuiltCallInvitationConfig()
        val appID : Long = 1140399219   // yourAppID
        val appSign :String= "ec96b77ed99dd24d80a6f647b22a1bd9a5c7104569f5abcea501f0b8d5b0860a"// yourAppSign
        // yourUserID, userID should only contain numbers, English characters, and '_'.
        var i = Intent()
         i.putExtra("CALLER_EMAIL",userEmail)
        i.putExtra("USER_NAME",userName)
        ZegoUIKitPrebuiltCallService.init(application,appID,appSign,userEmail,userEmail,config)



    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallInvitationService.unInit()
    }
}