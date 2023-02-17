package com.example.smartattendance

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.databinding.ActivityStudregBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_stud.etemail
import kotlinx.android.synthetic.main.activity_stud.etpswrd
import kotlinx.android.synthetic.main.activity_studreg.*

class StudregActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudregBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    val fd = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityStudregBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonlgn.setOnClickListener {
            val email = binding.etemail.text.toString().trim()
            val pass = binding.etpswrd.text.toString().trim()
            val name = binding.etname.text.toString().trim()
            val confpass = binding.etconfpswrd.text.toString().trim()
            val rolnumber = binding.etrolno.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty() && confpass.isNotEmpty() && rolnumber.isNotEmpty()) {
                if (pass == confpass) {
                    database = FirebaseDatabase.getInstance().getReference("students")

                    val students = hashMapOf<String, Any>(
                        "name" to name,
                        "roll number" to rolnumber,
                        "email" to email
                    )
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            fd.collection("students").document("$email")
                                .set(students)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "successfully saved!", Toast.LENGTH_SHORT).show()

                                }.addOnFailureListener {
                                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

                                }

                            """database.child(rolnumber).setValue(student).addOnSuccessListener {
                                Toast.makeText(this, "successfully saved", Toast.LENGTH_SHORT)
                                    .show()

                            }.addOnFailureListener {
                                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                            }"""
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
                } else if (rolnumber.isEmpty()) {
                    etrolno.error = "Enter roll number"
                    etrolno.requestFocus()
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