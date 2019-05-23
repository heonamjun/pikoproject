package com.example.pikoproject.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikoproject.Data.Writeinfo;
import com.example.pikoproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddsharingActivity extends AppCompatActivity {
    private static final String TAG = "AddsharingActivity";
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsharing);


        findViewById(R.id.add_photo).setOnClickListener(onClickListener);
        findViewById(R.id.load_image).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_photo:
                    update();
                    break;
                case R.id.load_image:

                    if (ContextCompat.checkSelfPermission(AddsharingActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddsharingActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


                        if(ActivityCompat.shouldShowRequestPermissionRationale(AddsharingActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        }else{
                            startToast("권한을 허용해주세요");
                        }
                    } else {
                        Intent intent = new Intent(AddsharingActivity.this, GalleryActiviy.class);
                        startActivityForResult(intent,0);
                    }
                    break;
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(AddsharingActivity.this, GalleryActiviy.class);
                    startActivity(intent);
                }else{
                    startToast("권한을 허용해주세요");
                }
            }
        }
    }

    private void update() {
            final String title = ((EditText) findViewById(R.id.title_edit)).getText().toString();
            final String contents = ((EditText) findViewById(R.id.contents_edit)).getText().toString();

            if (title.length() > 0 && contents.length() > 0) {

                Writeinfo writeinfo = new Writeinfo(title, contents,user.getUid());
                Uploder(writeinfo);

            } else {
                startToast("입력해주세요.");

            }
        }

        private void Uploder(Writeinfo writeinfo){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("photos")
                    .add(writeinfo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            startToast("성공");
                            Log.d(TAG,"DocumentSnapshot written with ID" + documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    startToast("실패");
                    Log.w(TAG,"Error adding document",e);
                }
            });

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0 : {
                if(resultCode == Activity.RESULT_OK){
                    String Path = data.getStringExtra("ImagePath");

                    LinearLayout parent = findViewById(R.id.contentLayout);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(AddsharingActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(Path).override(1000,1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(AddsharingActivity.this );
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);


                }
                break;
            }

        }
    }

    private void startToast(String msg){
        Toast.makeText(AddsharingActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
