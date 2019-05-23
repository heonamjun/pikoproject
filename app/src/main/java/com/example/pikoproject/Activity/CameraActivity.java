package com.example.pikoproject.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pikoproject.Camera.Camera2BasicFragment;
import com.example.pikoproject.R;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        Camera2BasicFragment camera2BasicFragment = new Camera2BasicFragment();
        if (null == savedInstanceState) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.camera_preview, camera2BasicFragment)
                    .commit();
        }
    }

}
