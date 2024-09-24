package com.example.chatflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import com.example.chatflow.Adapter.userAdapter
import com.example.chatflow.databinding.ActivityAllUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class allUserActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var binding : ActivityAllUserBinding
    lateinit var userData:MutableList<UserData>

    lateinit var allUserAdapter : userAdapter
    lateinit var realTimeDb : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



        auth = FirebaseAuth.getInstance()
        realTimeDb = FirebaseDatabase.getInstance().reference
        userData = mutableListOf()
        allUserAdapter = userAdapter(this, userData)



        binding.logOutIcon.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, loginActivity::class.java))
            finish()
        }

        realTimeDb.child("AllUser").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userData.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(UserData::class.java)
                    if (auth.currentUser?.uid != currentUser!!.uid) {
                        userData.add(currentUser)

                    }

                }
                binding.userListRecyclerView.adapter = userAdapter(this@allUserActivity, userData)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}