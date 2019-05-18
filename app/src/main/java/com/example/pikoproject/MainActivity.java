package com.example.pikoproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    final int PickfromAlbum = 1;
    private static RecyclerView mrecyclerview;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutmanager;
    private ArrayList<item> mydataset;
    private Uri photouri;
    private ImageButton btn1;
    private Button loginout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true); // 카드뷰 사이즈 고정
        //mlayoutmanager = new GridLayoutManager(this, 2);// 사이클뷰 디자인부분 2열
        mlayoutmanager = new LinearLayoutManager(this , LinearLayout.HORIZONTAL,false); // 가로로 스크롤
        mrecyclerview.setLayoutManager(mlayoutmanager);

        mydataset = new ArrayList<>();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // 파베 객체 생성.ㄵ
        DatabaseReference databaseReference = firebaseDatabase.getReference("drama_list"); //v파베에서 참조할 데이터 .ㄵ

        databaseReference.addValueEventListener(new ValueEventListener() { // 파베 데이터참조 값이 변경되거나 부를때 쓰는 리스너.ㄵ
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mydataset.clear();
                for (DataSnapshot filesnapshot : dataSnapshot.getChildren()) {
                    item ditem = filesnapshot.getValue(item.class);                    //item.class 에다가 filesnapshot(데이터값) 넣기.ㄵ
                    mydataset.add(ditem);
                }
                madapter.notifyDataSetChanged(); //어댑터에 리스트가 바뀌엇다는걸 알림.ㄵ
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // 참조 데이터 기준으로 콜벡리스너
        madapter = new adapter(mydataset);
        mrecyclerview.setAdapter(madapter);



        btn1 = (ImageButton)findViewById(R.id.cameraselect);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CameraActivity2.class);
                startActivity(intent);
            }
        });

        loginout_btn = (Button) findViewById(R.id.Loginout);
        loginout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }



    }
