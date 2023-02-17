package com.example.smartattendance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.smartattendance.databinding.ActivityAdpassBinding
import java.util.concurrent.Executor


class AdpassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdpassBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdpassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = androidx.biometric.BiometricPrompt(
            this@AdpassActivity,
            executor,
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Authentication Error!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

            })
        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using FACE")
            .setNegativeButtonText("Cancel")
            .build()


        binding.buttonlgn.setOnClickListener {
            val code = binding.usrnm.text.toString().trim()
            val pass = binding.psd.text.toString().trim()

            if (code.isNotEmpty() && pass.isNotEmpty()) {
                if (code == "q" && pass == "q") {
                    //biometricPrompt.authenticate(promptInfo)
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "login credentials are wrong!!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "empty fields are not allowed!!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
