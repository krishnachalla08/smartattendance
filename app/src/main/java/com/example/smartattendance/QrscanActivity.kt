package com.example.smartattendance

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.example.smartattendance.databinding.ActivityQrscanBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_qrscan.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val CAMERA_REQUEST_CODE = 101

class QrscanActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityQrscanBinding
    val fd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rno = intent.getStringExtra("rnumb")
        val y = rno.toString()
        val sub1 = intent.getStringExtra("subj")
        val sub = sub1.toString()
        val sem1 = intent.getStringExtra("sem")
        val sem = sem1.toString()
        val branch1 = intent.getStringExtra("brnch")
        val branch = branch1.toString()

        val intent = Intent(this, StudActivity::class.java)
        Scanner(intent, y, sub, sem, branch)


    }

    private fun Scanner(intent: Intent, y: String, sub: String, sm: String, brnch: String) {
        val scannerView: CodeScannerView = findViewById(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                tview.text = it.text
                val dat = it.text
                button(dat, intent, y, sub, sm, brnch)
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Log.e("Main", "camera initialization error: ${it.message}")
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    fun button(dat: String, intent: Intent, y: String, sub: String, sm: String, brnch: String) {
        binding.reg.setOnClickListener {
            val data1 = dat.subSequence(0, 10).toString()
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-")
            val format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val hr = DateTimeFormatter.ofPattern("HH")
            val hrs = current.format(hr)

            val formatted = current.format(formatter)
            val format4 = current.format(format2)
            val h = hrs.toInt()
            val data2 = (data1 + formatted + h).trim()
            if (dat == data2) {
                toasting1(data2)
                database1(y, data2, intent, format4, sub, sm, brnch)

            } else {
                toasting2()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }
    }


    fun toasting1(v: String) {
        Toast.makeText(this, v, Toast.LENGTH_SHORT).show()
    }

    fun toasting2() {
        Toast.makeText(this, "Invalid Qr code", Toast.LENGTH_SHORT).show()
    }


    fun database1(
        y: String,
        data2: String,
        intent: Intent,
        format: String,
        subject: String,
        semester: String,
        branch: String
    ) {
        val attendance = hashMapOf<String, Any>(
            "rollnumber" to y.toString()
        )


        fd.collection("codes")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {

                        if (document.data.getValue("code1") == data2 || document.data.getValue("code2") == data2) {

                            //
                            fd.collection(branch + "," + semester + "," + subject).document(format).collection("present").document(y).set(attendance)
                                .addOnSuccessListener {

                                    Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()

                                }.addOnFailureListener {
                                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

                                }


                            fd.collection("codes").get().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    for (document in it.result!!) {
                                        if (document.id == branch + "," + semester + "," + subject) {
                                            fd.collection("codes")
                                                .document(branch + "," + semester + "," + subject)
                                                .collection(y).document(format).set(attendance)

                                        }
                                    }
                                }

                            }

                            startActivity(intent)
                            toasting1("Registered successfully")

                        }
                    }
                }
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(
                        this,
                        "camera permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

