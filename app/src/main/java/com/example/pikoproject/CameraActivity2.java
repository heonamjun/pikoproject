package com.example.pikoproject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.pikoproject.Camera.Camera2RawFragment;
import com.example.pikoproject.databinding.ActivityCamera2Binding;


public class CameraActivity2 extends AppCompatActivity {
    private ActivityCamera2Binding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera2);


        if (null == savedInstanceState) {
            initCameraPreview();
        }
    }

    private void initCameraPreview() {
        // Create an instance of Camera
        getFragmentManager().beginTransaction().replace(R.id.camera_preview, Camera2RawFragment.newInstance()).commit();
    }
}