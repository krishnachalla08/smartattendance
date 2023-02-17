package com.example.smartattendance

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartattendance.databinding.ActivityFaceBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.wonderkiln.camerakit.*
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_face.*

class FaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFaceBinding
    private lateinit var alertDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraview.toggleFacing()
        alertDialog = SpotsDialog.Builder()
            .setContext(this).setMessage("Please wait...")
            .setCancelable(false).build()
        binding.imagedetectbutton.setOnClickListener(View.OnClickListener {
            binding.cameraview.start()
            binding.cameraview.captureImage()
            binding.graphicOverlay.clear()
        })
        binding.cameraview.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {}
            override fun onError(cameraKitError: CameraKitError) {}
            override fun onImage(cameraKitImage: CameraKitImage) {
                alertDialog.show()
                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    binding.cameraview.getWidth(),
                    binding.cameraview.getHeight(),
                    false
                )
                binding.cameraview.stop()
                processFaceDetection(bitmap)
            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {}
        })
    }

    private fun processFaceDetection(bitmap: Bitmap) {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()
        val detector = FaceDetection.getClient(options)
        val result = detector.process(inputImage)
            .addOnSuccessListener { faces -> getFaceresults(faces) }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@FaceActivity,
                    "Error:" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getFaceresults(faces: List<Face>) {
        var counter = 0
        for (face in faces) {
            val rect = face.boundingBox
            val rectOverlay = RectOverlay(rect, graphicOverlay)
            graphicOverlay.add(rectOverlay)
            counter = counter + 1
        }
        alertDialog!!.dismiss()
    }

    override fun onPause() {
        super.onPause()
        cameraview!!.stop()
    }

    override fun onResume() {
        super.onResume()
        cameraview!!.start()
    }
}