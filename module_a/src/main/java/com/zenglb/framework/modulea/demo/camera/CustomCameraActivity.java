package com.zenglb.framework.modulea.demo.camera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zenglb.framework.modulea.R;

public class CustomCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);


        findViewById(R.id.system_camera).setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomCameraActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.cameraView).setOnClickListener(v -> {
//            Intent intent = new Intent(this, CameraViewActivity.class);
//            startActivity(intent);
        });


        findViewById(R.id.cameraKit).setOnClickListener(v -> {
            Intent intent = new Intent(this, CameraKitActivity.class);
            startActivity(intent);
        });


    }
}
