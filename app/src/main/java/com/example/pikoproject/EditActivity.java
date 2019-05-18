package com.example.pikoproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {
    private final static int PICK_PHOTO = 1;

        Button openalbum;
        Button textbutton;
        Button savebutton;
        ImageView photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        openalbum = (Button) findViewById(R.id.openalbum);
        textbutton = (Button) findViewById(R.id.textbutton);
        photo = (ImageView) findViewById(R.id.photo_image);

        final DrawView drawview = (DrawView)findViewById(R.id.drawtouch);

        openalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PHOTO);
            }
        });


        textbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   drawview.mycolor(1);
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v = findViewById(R.id.drawtouch);
                v.setDrawingCacheEnabled(true);
                Bitmap bitmap = v.getDrawingCache();
                File root = Environment.getExternalStorageDirectory();
                File cachePath = new File(root.getAbsolutePath()  );
            }
        });













    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_PHOTO) {
            if(resultCode == Activity.RESULT_OK){
                try{
                    Log.d("Real path is: ",getImagePath(data.getData()));
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap imag = BitmapFactory.decodeStream(in);
                    in.close();
                    photo.setImageBitmap(imag);


                }catch(Exception e){
                    Toast.makeText(getBaseContext(),"사진불러오기실패",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            if (data == null) {
                return;
            }

            Uri photoUri;
            photoUri = data.getData();
        }
    }


    private String getImagePath(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this,data,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
