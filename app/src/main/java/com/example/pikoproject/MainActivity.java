package com.example.pikoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mrecyclerview;
    private RecyclerView.Adapter madapter;
    private RecyclerView.LayoutManager mlayoutmanager;
    private ArrayList<item> mydataset;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrecyclerview = (RecyclerView)findViewById(R.id.recyclerview);

        mrecyclerview.setHasFixedSize(true); // 카드뷰 사이즈 고정
        mlayoutmanager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(mlayoutmanager);

        mydataset = new ArrayList<>();
        madapter = new adapter(mydataset);
        mrecyclerview.setAdapter(madapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); // 파베 객체 생성
        DatabaseReference databaseReference= firebaseDatabase.getReference("drama_list"); //v파베에서 참조할 데이터 .ㄵ

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                for(DataSnapshot filesnapshot : dataSnapshot.getChildren()){
                    String str = filesnapshot.child("gps")
                    item ditem = filesnapshot.getValue(item.class);
                    mydataset.add(ditem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); // 참조 데이터 기준으로 콜벡리스너

        try {
            URL url = new URL("https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.bloter.net%2Fwp-content%2Fuploads%2F2016%2F08%2F%25EC%258A%25A4%25EB%25A7%2588%25ED%258A%25B8%25ED%258F%25B0-%25EC%2582%25AC%25EC%25A7%2584.jpg&imgrefurl=https%3A%2F%2Fwww.bloter.net%2Farchives%2F261967&docid=SCjFSWotEtT0mM&tbnid=T8OdXjRN_v8ZLM%3A&vet=10ahUKEwiQwfO03_7hAhWSwpQKHRxsCIUQMwh8KAEwAQ..i&w=960&h=651&bih=695&biw=767&q=%EC%82%AC%EC%A7%84&ved=0ahUKEwiQwfO03_7hAhWSwpQKHRxsCIUQMwh8KAEwAQ&iact=mrc&uact=8");
            URLConnection conn = url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (Exception e) {

        }
        System.out.println(mydataset);


    }
}