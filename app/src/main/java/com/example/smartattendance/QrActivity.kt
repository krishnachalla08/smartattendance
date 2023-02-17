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
import com.example.smartattendance.databinding.ActivityQrBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_qr.*

class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rno = intent.getStringExtra("rnum")
        val res: Resources = resources
        val subs = res.getStringArray(com.example.smartattendance.R.array.subjects)
        val subadapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.simple_list_item_1, subs)
        val actv = binding.autoCompleteTextView2
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
                            arr2(brnch, yer,rno.toString())



                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    private fun arr2(b: String, c: String,rno:String) {


        binding.scan.setOnClickListener {
            val x = b + c + autoCompleteTextView2.text.toString()
            val ar = autoCompleteTextView2.text.toString()

            Toast.makeText(
                this@QrActivity,
                x.toString(),
                Toast.LENGTH_LONG
            )
                .show()
            val Intent =
                Intent(this, QrscanActivity::class.java)
            Intent.putExtra("brnch", b)
            Intent.putExtra("sem", c)
            Intent.putExtra("rnumb",rno)
            Intent.putExtra("subj",ar)

            startActivity(Intent)
        }
        binding.back.setOnClickListener {
            val a = autoCompleteTextView2.text.toString()


            val Intent = Intent(this, StatusActivity::class.java)
            Intent.putExtra("brnch", b)
            Intent.putExtra("sem", c)
            Intent.putExtra("rnumb",rno)
            Intent.putExtra("subj",a)
            startActivity(Intent)
        }




    }


}