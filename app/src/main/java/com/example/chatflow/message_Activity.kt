package com.example.chatflow

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatflow.Adapter.imageShareAdapte
import com.example.chatflow.Adapter.messageAdapter
import com.example.chatflow.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.squareup.picasso.Picasso
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.UUID


class message_Activity : AppCompatActivity() {
    lateinit var binding: ActivityMessageBinding
    lateinit var myMessageAdapter: messageAdapter
    lateinit var imageSharedAdapter : imageShareAdapte
    lateinit var messageList: ArrayList<Message>
    lateinit var imageList : ArrayList<imageData>
    val  imageDatabaseRef=FirebaseDatabase.getInstance().getReference()

    private var imageUri : Uri? = null
    private lateinit var imageStorage : StorageReference
    companion object{
        private const val REQUEST_IMAGE_PICK=1
    }
    lateinit var mDbRef: DatabaseReference
    var reciverRoom: String? = null
    var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        messageList = ArrayList()
        imageList = ArrayList()
        myMessageAdapter = messageAdapter(this, messageList)
        binding.messageRecyclerView.adapter = myMessageAdapter



        supportActionBar?.hide()
        var currentUserName = intent.getStringExtra("NAME")
        var userEmail = intent.getStringExtra("USER_EMAIL")
        var userName = intent.getStringExtra("USER_NAME")
        var currentUserEmail = intent.getStringExtra("EMAIL_FOR_CALL")
        var currentUserImage = intent.getStringExtra("IMAGE")
        val emailForCall = intent.getStringExtra("CALLER_EMAIL")
        binding.toolbarUserName.text = currentUserName

        binding.toolbar.setOnClickListener {
            var i = Intent(this,userDetail_Activity::class.java)
            i.putExtra("CURRENT_NAME",currentUserName)
            i.putExtra("CURRENT_EMAIL",userEmail)
            i.putExtra("USEREMAIL",currentUserEmail)
            i.putExtra("USER_IMAGE",currentUserImage)
            i.putExtra("EMAIL_FOR_CALL",emailForCall)

            startActivity(i)



        }


        initializeZegoCloud(emailForCall.toString())
        // code for Audio Call
        var userID = intent.getStringExtra("USER_ID")
        binding.CallButton.setIsVideoCall(false)
        binding.CallButton.resourceID="zego_uikit_call"
        binding.CallButton.setInvitees(listOf(ZegoUIKitUser(currentUserEmail)))
        // end of audio call

        //Code for Video call

        // end of Video call

            var userImage = intent.getStringExtra("IMAGE")
            Picasso.get().load(userImage).into(binding.profileIcon)
            var reciverUid = intent.getStringExtra("UID")

            var senderUid = FirebaseAuth.getInstance().currentUser?.uid
            reciverRoom = senderUid + reciverUid
            senderRoom = reciverUid + senderUid

            mDbRef = FirebaseDatabase.getInstance().getReference() // chat storage

        imageStorage= FirebaseStorage.getInstance().reference

        binding.gallaryButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, REQUEST_IMAGE_PICK)




        }





            binding.backButton.setOnClickListener {
                startActivity(Intent(this@message_Activity, allUserActivity::class.java))
            }
        // send Message and Recived Message also store the message in recyclerView
            binding.sendButton.setOnClickListener {
                if (binding.messageBox.text.isEmpty()) {
                    Toast.makeText(this,"Please Enter Your Message",Toast.LENGTH_SHORT).show()

                }
                else {
                    val sendMessage = binding.messageBox.text.trim().toString()
                    var messageObject = Message(sendMessage, senderUid.toString(),null)

                    mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            mDbRef.child("chats").child(reciverRoom!!).child("messages").push()
                                .setValue(messageObject).addOnSuccessListener {

                                }.addOnFailureListener {
                                    Toast.makeText(this, "Server is not responding!!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            binding.messageBox.text.clear()
                        }

                }
            }
        // end of send message and recived message

        // start of showing messages to RecyclerView
            mDbRef.child("chats").child(senderRoom!!).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        messageList.clear()
                        for (postSnapShot in snapshot.children) {
                            var message = postSnapShot.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                        myMessageAdapter.notifyDataSetChanged()

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@message_Activity,
                            "Error in Database +${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        // end of Showing messages in RecyclerView





    }// end of onCreate Function

    private fun initializeZegoCloud(currentUserData: String) {
        val appID = 1140399219  // Get this from ZEGOCLOUD Console
        val appSign = "ec96b77ed99dd24d80a6f647b22a1bd9a5c7104569f5abcea501f0b8d5b0860a"

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()



    }
    private fun permission(){
        PermissionX.init(this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason(ExplainReasonCallback{scope, deniedList ->
                val message = "We need your permission"
                scope.showRequestReasonDialog(deniedList,message,"Allow","Denaied")

            }).request(RequestCallback{allGranted, grantedList, deniedList ->  })
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== REQUEST_IMAGE_PICK&&resultCode== Activity.RESULT_OK){
            imageUri=data?.data

            imageUri.let {
                uploadImageToFirebase(it)
            }




        }
    }

    private fun uploadImageToFirebase(it: Uri?) {
        var senderUid = FirebaseAuth.getInstance().currentUser?.uid

        var storedImageUri = imageStorage.child("images/${UUID.randomUUID()}")
        storedImageUri.putFile(imageUri!!).addOnSuccessListener { task ->
            task.storage.downloadUrl.addOnSuccessListener { uri ->
                var imageUrlLink = uri


                var messageObject = Message("image",senderUid.toString(), imageUrlLink.toString())

                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(reciverRoom!!).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener {

                            }.addOnFailureListener {
                                Toast.makeText(
                                    this,
                                    "Server is not responding!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                    }


            }

        }



    }


}

