package com.example.chatflow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatflow.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class loginActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
      supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        binding.signupPage.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            var email = binding.Email.text.toString()
            var password = binding.password.text.toString()

            if (email.isEmpty()&&password.isEmpty()) {
                Toast.makeText(this,"Please Enter Your Detail",Toast.LENGTH_LONG).show()

            }
            else{
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    startActivity(Intent(this,allUserActivity::class.java))
                    Toast.makeText(this,"You are Successfully login",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Something is wrong",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


}