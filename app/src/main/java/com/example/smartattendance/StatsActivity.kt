package com.example.smartattendance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StatsActivity : AppCompatActivity() {
    val fd = FirebaseFirestore.getInstance()
    lateinit var dateTV: TextView
    lateinit var calendarView1: CalendarView
    lateinit var btn :Button
    lateinit var view1:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val sub1 = intent.getStringExtra("subj")
        val sub = sub1.toString()
        val sem1 = intent.getStringExtra("sem")
        val sem = sem1.toString()
        val branch1 = intent.getStringExtra("brnch")
        val branch = branch1.toString()


        dateTV = findViewById(R.id.idTVDate)
        calendarView1 = findViewById(R.id.calendarView)
        view1 = findViewById(R.id.prsnt)

        calendarView1
            .setOnDateChangeListener { view, year, month, dayOfMonth ->

                if (dayOfMonth<10){
                    val d = 0
                    val d1 = d.toString()

                    val Date1 = (d1+dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)
                    dateTV.setText(Date1)
                    fn(Date1,branch,sem,sub)
                }
                else{
                    val Date1 = (dayOfMonth.toString() + "-"
                            + (month + 1) + "-" + year)
                    dateTV.setText(Date1)
                    fn(Date1,branch,sem,sub)
                }


            }


    }
    private fun fn(date1: String,branch:String,sem :String,sub:String) {
        btn = findViewById(R.id.btn2)
        btn.setOnClickListener {
            fd.collection(branch + "," + sem + "," + sub).document(date1).collection("present").get()
                .addOnCompleteListener {
                    var x = 0
                    for (document in it.result!!) {
                        x += 1
                    }
                    view1.setText(x.toString())
                }
        }

    }


}