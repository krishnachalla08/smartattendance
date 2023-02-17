package com.example.smartattendance

import android.R
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.databinding.ActivityTeachgenBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_qr.*
import kotlinx.android.synthetic.main.activity_teachgen.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TeachgenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeachgenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeachgenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val current = LocalDateTime.now()
        val format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val form = current.format(format2)
        val res: Resources = resources
        val subs = res.getStringArray(com.example.smartattendance.R.array.subjects)
        val subadapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.simple_list_item_1, subs)
        val actv = binding.autoCompleteTextView
        actv.threshold = 1

        actv.setAdapter(subadapter)

        actv.setTextColor(Color.BLACK)



        binding.branch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                val brnch = adapterView?.getItemAtPosition(position).toString()

                binding.semester.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            val yer = adapterView?.getItemAtPosition(position).toString()
                            arr2(brnch, yer)



                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }


    private fun arr2(b: String, c: String) {


        binding.gen.setOnClickListener {

            val a = autoCompleteTextView.text.toString()
            val x = b + c + a

            Toast.makeText(
                this@TeachgenActivity,
                x.toString(),
                Toast.LENGTH_LONG
            )
                .show()
            val Intent =
                Intent(this, QrgeneratorActivity::class.java)
            Intent.putExtra("brnch", b)
            Intent.putExtra("sem", c)
            Intent.putExtra("subj", a)
            startActivity(Intent)


        }

        binding.back.setOnClickListener {
            val a = autoCompleteTextView.text.toString()

            Toast.makeText(
                this@TeachgenActivity,
                a.toString(),
                Toast.LENGTH_LONG
            )
                .show()


            val Intent = Intent(this, StatsActivity::class.java)
            Intent.putExtra("subj",a)
            Intent.putExtra("brnch",b)
            Intent.putExtra("sem",c)
            startActivity(Intent)
        }


    }



}