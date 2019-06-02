package com.example.pikoproject.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.pikoproject.Adapters.GalleryAdapter;
import com.example.pikoproject.R;

import java.util.ArrayList;

public class GalleryActiviy extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_gallery_activiy);

            if(ContextCompat.checkSelfPermission(GalleryActiviy.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(GalleryActiviy.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                if(ActivityCompat.shouldShowRequestPermissionRationale(GalleryActiviy.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)){

                }else{
                        startToast("권한을 허용해주세요");
                }
                }else{
                    recyclerInit();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1 :{
                if(grantResults.length > 0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    recyclerInit();
                }else{
                    finish();
                    startToast("권한을 허용해 주세요");
                }
            }
        }
    }

    private void recyclerInit(){
        final int numberOfColumns = 3;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.galleryrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,numberOfColumns));


        RecyclerView.Adapter mAdapter = new GalleryAdapter(this, getImagesPath(this));
        recyclerView.setAdapter(mAdapter);
    }


    public static ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data;

        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);


        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(PathOfImage);
        }
        return listOfAllImages;
    }


    private void startToast(String msg){
        Toast.makeText(GalleryActiviy.this,msg,Toast.LENGTH_SHORT).show();
    }
    }

