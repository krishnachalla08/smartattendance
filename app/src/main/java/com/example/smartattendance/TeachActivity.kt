package com.example.smartattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartattendance.databinding.ActivityStudBinding
import com.example.smartattendance.databinding.ActivityTeachBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeachActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeachBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeachBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonlgn.setOnClickListener {
            var email = binding.etemail.text.toString().trim()
            var pass = binding.etpswrd.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            fd.collection("teachers")
                                .get()
                                .addOnCompleteListener {

                                    if (it.isSuccessful) {
                                        for (document in it.result!!)
                                            if (email == document.data.getValue("email")) {

                                                val Intent =
                                                    Intent(
                                                        this,
                                                        TeachgenActivity::class.java
                                                    )
                                                startActivity(Intent)
                                            }
                                    }else {
                                        Toast.makeText(
                                            this,
                                            "Account does not exists!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                        } else {
                            Toast.makeText(this, "password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }

                //
            } else {
                Toast.makeText(this, "empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}