package com.example.smartattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val studbtn1 = findViewById<Button>(R.id.stdBtnBtn)
        studbtn1.setOnClickListener{
            val intent = Intent(this,StudregActivity::class.java)
            startActivity(intent)
        }
        val teachbtn1 = findViewById<Button>(R.id.teachBtn)
        teachbtn1.setOnClickListener{
            val Intent = Intent(this,TeachregActivity::class.java)
            startActivity(Intent)
        }

    }
}