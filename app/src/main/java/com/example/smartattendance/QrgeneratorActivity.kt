package com.example.smartattendance

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.databinding.ActivityQrgeneratorBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QrgeneratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrgeneratorBinding

    val fd = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrgeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btngen.setOnClickListener {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-")
            val hr = DateTimeFormatter.ofPattern("HH")
            val hrs = current.format(hr)
            val formatted = current.format(formatter)
            val format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val form = current.format(format2)
            val h = hrs.toInt() + 1
            val x = getRandStr()

            binding.etdata.setText(x + formatted + hrs + "and" + h)
            val data1 = x + formatted + hrs
            val data2 = x + formatted + h

            val sub1 = intent.getStringExtra("subj")
            val sub = sub1.toString()
            val sem1 = intent.getStringExtra("sem")
            val sem = sem1.toString()
            val branch1 = intent.getStringExtra("brnch")
            val branch = branch1.toString()
            val codes = hashMapOf<String, Any>(
                "code1" to data1,
                "code2" to data2
            )

            fd.collection("codes").document(branch + "," + sem + "," + sub)
                .set(codes)
                .addOnSuccessListener {

                    Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

                }
            val attendance = hashMapOf<String, Any>(
                "status" to "present"
            )

            fd.collection("codes").get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        if (document.id == branch + "," + sem + "," + sub) {
                            fd.collection("codes")
                                .document(branch + "," + sem + "," + sub)
                                .collection("total days").document(form).set(attendance)
                        }
                    }
                }

            }


            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data1, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                binding.qrgen.setImageBitmap(bmp)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

    }

    private fun getRandStr(): String = List(10) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")


}