package com.example.smartattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class StatusActivity : AppCompatActivity() {
    val fd = FirebaseFirestore.getInstance()
    lateinit var view1: TextView
    lateinit var view2: TextView
    lateinit var view3: TextView
    lateinit var btn :Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        val sub1 = intent.getStringExtra("subj")
        val sub = sub1.toString()
        val sem1 = intent.getStringExtra("sem")
        val sem = sem1.toString()
        val branch1 = intent.getStringExtra("brnch")
        val branch = branch1.toString()
        val rno1 = intent.getStringExtra("rnumb")
        val rno = rno1.toString()

        view1 = findViewById(R.id.t1)
        view2 = findViewById(R.id.t2)
        view3 = findViewById(R.id.t3)
        btn = findViewById(R.id.back)

        fd.collection("codes").get().addOnCompleteListener {

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    if (document.id == branch + "," + sem + "," + sub) {
                        val d = document.id


                        fd.collection("codes").document(d).collection(rno).get()
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    var n = 0
                                    for (document in it.result!!) {
                                        n += 1
                                    }
                                    view1.setText("    "+n.toString())

                                    fd.collection("codes").document(d).collection("total days").get()
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                var m = 0
                                                for (document in it.result!!) {
                                                    m += 1
                                                }
                                                view2.setText("    "+m.toString())
                                                view3.setText("    "+((n/m)*100).toString())
                                            }
                                        }
                                }
                            }


                    }
                }
            }
        }
        btn.setOnClickListener {
            val intent = Intent(this, QrActivity::class.java)
            startActivity(intent)
        }

    }
}