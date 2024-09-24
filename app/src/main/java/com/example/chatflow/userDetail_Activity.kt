package com.example.chatflow

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatflow.databinding.ActivityUserDetailBinding
import com.squareup.picasso.Picasso
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

class userDetail_Activity : AppCompatActivity() {
    lateinit var binding : ActivityUserDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       supportActionBar?.hide()
        var currentUserName = intent.getStringExtra("CURRENT_NAME")
        var userEmail = intent.getStringExtra("USEREMAIL")
        var userDP = intent.getStringExtra("USER_IMAGE")
        var emailForCall= intent.getStringExtra("EMAIL_FOR_CALL")

        binding.userEmail.text = userEmail.toString()
        binding.textView2.text=currentUserName.toString()
       Picasso.get().load(userDP).into(binding.circleImageView)





        binding.videoCallButton.setIsVideoCall(true)
        binding.videoCallButton.resourceID="zego_uikit_call"
        binding.videoCallButton.setInvitees(listOf(ZegoUIKitUser(userEmail)))









    }
    private fun initializeZegoCloud(currentUserData: String) {
        val appID = 1140399219  // Get this from ZEGOCLOUD Console
        val appSign = "ec96b77ed99dd24d80a6f647b22a1bd9a5c7104569f5abcea501f0b8d5b0860a"

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()



    }
}