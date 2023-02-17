package com.example.smartattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Startuppage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startuppage)

        val adminbtn = findViewById<Button>(R.id.firstBtn)
        adminbtn.setOnClickListener{
            val intent = Intent(this,AdpassActivity::class.java)
            startActivity(intent)
        }
        val studbtn = findViewById<Button>(R.id.secondBtn)
        studbtn.setOnClickListener{
            val Intent = Intent(this,StudActivity::class.java)
            startActivity(Intent)
        }
        val teachbtn = findViewById<Button>(R.id.thirdBtn)
        teachbtn.setOnClickListener{
            val Intent = Intent(this,TeachActivity::class.java)
            startActivity(Intent)
        }

    }
}