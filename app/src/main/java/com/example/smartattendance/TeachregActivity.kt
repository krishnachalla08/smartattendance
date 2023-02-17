package com.example.smartattendance

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.databinding.ActivityTeachregBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_studreg.*
import kotlinx.android.synthetic.main.activity_teach.etemail
import kotlinx.android.synthetic.main.activity_teach.etpswrd

class TeachregActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeachregBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityTeachregBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonlgn.setOnClickListener {
            val email = binding.etemail.text.toString().trim()
            val pass = binding.etpswrd.text.toString().trim()
            val name = binding.etname.text.toString().trim()
            val confpass = binding.etconfpswrd.text.toString().trim()


            if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty() && confpass.isNotEmpty()) {
                if (pass == confpass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val temails = hashMapOf<String, Any>(
                                "email" to email,
                                "name" to name,
                            )
                            fd.collection("teachers").document("$email")
                                .set(temails)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "successfully saved!", Toast.LENGTH_SHORT)
                                        .show()

                                }.addOnFailureListener {
                                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

                                }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (name.isEmpty()) {
                    etname.error = "Enter user name"
                    etname.requestFocus()
                } else if (email.isEmpty()) {
                    etemail.error = "Enter email"
                    etemail.requestFocus()
                } else if (pass.isEmpty()) {
                    etpswrd.error = "Enter password"
                    etpswrd.requestFocus()
                } else {
                    etconfpswrd.error = "Enter correct password"
                    etconfpswrd.requestFocus()
                }
            }
        }

    }
}