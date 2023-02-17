package com.example.smartattendance;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import java.util.List;
import dmax.dialog.SpotsDialog;

public class FaceActivity2 extends AppCompatActivity{
    private Button imagedetectbutton;
    private GraphicOverlay graphicOverlay;
    private CameraView cameraview;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face2);
        imagedetectbutton = findViewById(R.id.image_detect_button);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        cameraview = findViewById(R.id.cameraview);
        cameraview.toggleFacing();

        alertDialog = new SpotsDialog.Builder()
                .setContext(this).setMessage("Please wait...")
                .setCancelable(false).build();
        imagedetectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraview.start();
                cameraview.captureImage();
                graphicOverlay.clear();
            }
        });
        cameraview.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                alertDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraview.getWidth(),cameraview.getHeight(),false);
                cameraview.stop();
                processFaceDetection(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

    }

    private void processFaceDetection(Bitmap bitmap) {
        InputImage inputImage=InputImage.fromBitmap(bitmap, 0);
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build();
        com.google.mlkit.vision.face.FaceDetector detector = FaceDetection.getClient(options);
        Task<List<Face>> result =
                detector.process(inputImage)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        getFaceresults(faces);
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(FaceActivity2.this,"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

    }

    private void getFaceresults(List<Face> faces) {
        int counter = 0;
        for (Face face:faces){
            Rect rect = face.getBoundingBox();
            RectOverlay rectOverlay =new RectOverlay(rect, graphicOverlay);
            graphicOverlay.add(rectOverlay);
            counter = counter+1;

        }
        alertDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraview.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraview.start();
    }
}