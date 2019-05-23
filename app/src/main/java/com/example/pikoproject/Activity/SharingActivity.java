package com.example.pikoproject.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.pikoproject.R;

public class SharingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);

    }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.floatingActionButton :
                    mstartActivity(AddsharingActivity.class);
                        break;
                }
            }
        };


    public void mstartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }


}
