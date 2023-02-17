package com.example.smartattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smartattendance.databinding.ActivityStudBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class StudActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("students")


        binding.buttonlgn.setOnClickListener {
            val rnum = binding.etrno.text.toString().trim()
            val email = binding.etemail.text.toString().trim()
            val pass = binding.etpswrd.text.toString().trim()

            if (rnum.isNotEmpty() && pass.isNotEmpty() && email.isNotEmpty()) {
                fd.collection("students")
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            for (document in it.result!!) {
                                if (document.data.getValue("roll number") == rnum) {
                                    firebaseAuth.signInWithEmailAndPassword(email, pass)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                val intent =
                                                    Intent(this, QrActivity::class.java).also {
                                                        it.putExtra("rnum", rnum)
                                                        startActivity(it)
                                                    }

                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    it.exception.toString(),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }

                            }
                        }
                    }

            } else {
                Toast.makeText(this, "empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}