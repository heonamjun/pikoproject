package com.example.pikoproject.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikoproject.Data.Writeinfo;
import com.example.pikoproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddsharingActivity extends AppCompatActivity {
    private static final String TAG = "AddsharingActivity";
    FirebaseUser user;
    private ArrayList<String> pathlist = new ArrayList<>();
    private LinearLayout parent;
    private RelativeLayout buttonsbackgroundlayout;
    private RelativeLayout loaderLayout;
    private ImageView selectedimageView;
    private EditText selectedEditText;
    private EditText contentEiditText;
    private EditText title_edit;
    private int pathCount;
    private int successCount;
    private Writeinfo writeinfo;
    private StorageReference storageRef;

    protected String username;
    protected String useremail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsharing);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Intent intent = new Intent(AddsharingActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            username = user.getUid();
            useremail = user.getEmail();

        }

        parent = findViewById(R.id.contentLayout);
        buttonsbackgroundlayout = findViewById(R.id.buttonsbackgroundlayout);
        buttonsbackgroundlayout.setOnClickListener(onClickListener);
        loaderLayout = findViewById(R.id.loaderLayout);

        findViewById(R.id.add_photo).setOnClickListener(onClickListener);
        findViewById(R.id.load_image).setOnClickListener(onClickListener);
        findViewById(R.id.imagemodify).setOnClickListener(onClickListener);
        findViewById(R.id.imagedelete).setOnClickListener(onClickListener);


        contentEiditText = findViewById(R.id.contentEiditText);
        contentEiditText.setOnFocusChangeListener(onFocusChangeListener);
        title_edit = findViewById(R.id.title_edit);
        title_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(hasFocus){
                   selectedEditText =  null;
               }
            }
        });
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        writeinfo = (Writeinfo)getIntent().getSerializableExtra("Info");
        postinit();


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_photo:
                    storeupdate();
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

                case R.id.buttonsbackgroundlayout:
                    if(buttonsbackgroundlayout.getVisibility() == View.VISIBLE){
                        buttonsbackgroundlayout.setVisibility(View.GONE);
                    }
                    break;
                case R.id.imagemodify:
                    Intent intent = new Intent(AddsharingActivity.this,GalleryActiviy.class);
                    startActivityForResult(intent,1);
                    buttonsbackgroundlayout.setVisibility(View.GONE);
                    break;
                case R.id.imagedelete:
                    View selectedView = (View)selectedimageView.getParent();
                    String[] list = pathlist.get(parent.indexOfChild(selectedView) -1).split("\\?");
                    String[] list2 = list[0].split("%2F");
                    String name = list2[list2.length -1];


                    pathlist.remove(parent.indexOfChild(selectedView)-1);
                    parent.removeView(selectedView);
                    buttonsbackgroundlayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                selectedEditText = (EditText)v;
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

    private void storeupdate() {//데이터베이스에 사진및 텍스트 올리기
            final String title = ((EditText) findViewById(R.id.title_edit)).getText().toString();
            if (title.length() > 0 ) {
                loaderLayout.setVisibility(View.VISIBLE);
                final ArrayList<String> contentsList = new ArrayList<>();
                user= FirebaseAuth.getInstance().getCurrentUser();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                final DocumentReference documentReference = writeinfo == null ?firebaseFirestore.collection("posts").document() : firebaseFirestore.collection("posts").document(writeinfo.getId()) ;
                final Date date = writeinfo == null ? new Date() : writeinfo.getCreatedAt();

                for(int i = 0 ; i < parent.getChildCount(); i++){
                    LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);
                        for(int j = 0 ; j < linearLayout.getChildCount();j++){
                            View view = linearLayout.getChildAt(j);

                            if(view instanceof EditText){
                                String text = ((EditText)view).getText().toString();
                                if(text.length() > 0 ){
                                    contentsList.add(text);
                                }
                            }else if (!Patterns.WEB_URL.matcher(pathlist.get(pathCount)).matches()){
                                String path = pathlist.get(pathCount);
                                successCount++;
                                contentsList.add(path);
                                String[] pathArray = path.split("\\.");
                                final StorageReference mountainlmagesRef = storageRef.child("posts/" + documentReference.getId() + "/" + pathCount + pathArray[pathArray.length -1]);
                                try{
                                    InputStream stream = new FileInputStream(new File(pathlist.get(pathCount)));
                                    StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index","" + (contentsList.size()-1)).build();
                                    UploadTask uploadTask = mountainlmagesRef.putStream(stream,metadata);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                            mountainlmagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    successCount--;
                                                    Log.e("로그","uri " + uri);
                                                    contentsList.set(index,uri.toString());

                                                    if(successCount == 0){
                                                        Writeinfo writeinfo = new Writeinfo(title, contentsList ,user.getUid(), date , documentReference.getId(), user.getEmail(),"0","false");
                                                        Uploder(documentReference ,writeinfo);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }catch(FileNotFoundException e){

                                }
                                pathCount++;
                            }
                        }
                }
                if(successCount == 0){
                    Uploder(documentReference ,new Writeinfo(title, contentsList ,user.getUid(), date , documentReference.getId(), user.getEmail(),"0","false"));
                }



            } else {
                startToast("제목을 입력해주세요.");

            }
        }

        private void Uploder(DocumentReference documentReference, Writeinfo writeinfo){
        documentReference.set(writeinfo.getWriteinfo())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    loaderLayout.setVisibility(View.GONE);
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.w(TAG, "Error writing document", e);
                    loaderLayout.setVisibility(View.GONE);
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
                    pathlist.add(Path);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout linearLayout = new LinearLayout(AddsharingActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    if(selectedEditText == null){
                        parent.addView(linearLayout);
                    }else{

                    for(int i = 0 ;i < parent.getChildCount(); i++){
                        if(parent.getChildAt(i) == selectedEditText.getParent()){
                            parent.addView(linearLayout, i+1);
                        }
                    }

                    }


                    final ImageView imageView = new ImageView(AddsharingActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//갤러리에 올려진 이미지를 선택할때
                            buttonsbackgroundlayout.setVisibility(View.VISIBLE);
                            selectedimageView = (ImageView)v;
                        }
                    });
                    Glide.with(this).load(Path).override(500,500).into(imageView);
                    linearLayout.addView(imageView);

                    EditText editText = new EditText(AddsharingActivity.this );
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("내용");
                    editText.setOnFocusChangeListener(onFocusChangeListener);
                    linearLayout.addView(editText);
                }
                break;
            }

            case 1: {
                if(resultCode == Activity.RESULT_OK){
                    String ImagePath = data.getStringExtra("ImagePath");
                    View selectedView = (View)selectedimageView.getParent();
                    pathlist.set(parent.indexOfChild(selectedView)-1,ImagePath);
                    Glide.with(this).load(ImagePath).override(500,500).into(selectedimageView);
                }
                break;
            }
        }
    }


    private void postinit(){//수정
        if(writeinfo != null){
            title_edit.setText(writeinfo.getTitle());
            ArrayList<String> contentsList = writeinfo.getContents();
            for (int i = 0 ; i < contentsList.size() ; i ++){
                String contents = contentsList.get(i);
                if(Patterns.WEB_URL.matcher(contents).matches() && contents.contains("https://firebasestorage.googleapis.com/v0/b/piko-mobile.appspot.com/o/posts%")){
                    pathlist.add(contents);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout linearLayout = new LinearLayout(AddsharingActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    parent.addView(linearLayout);


                    final ImageView imageView = new ImageView(AddsharingActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//갤러리에 올려진 이미지를 선택할때
                            buttonsbackgroundlayout.setVisibility(View.VISIBLE);
                            selectedimageView = (ImageView)v;
                        }
                    });
                    Glide.with(this).load(contents).override(500,500).into(imageView);
                    linearLayout.addView(imageView);

                    EditText editText = new EditText(AddsharingActivity.this );
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("내용");

                    if (i < contentsList.size() - 1 ) {
                        String nextContents = contentsList.get(i+1);
                        if(!Patterns.WEB_URL.matcher(nextContents).matches() || nextContents.contains("https://firebasestorage.googleapis.com/v0/b/piko-mobile.appspot.com/o/posts%")){
                                editText.setText(nextContents);
                        }
                    }
                    editText.setOnFocusChangeListener(onFocusChangeListener);
                    linearLayout.addView(editText);
                }else if (i == 0){
                    contentEiditText.setText(contents);
                }
            }
        }
    }




    private void startToast(String msg){
        Toast.makeText(AddsharingActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
