package com.example.pikoproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.pikoproject.Adapters.SharingAdapter;
import com.example.pikoproject.Data.Likeinfo;
import com.example.pikoproject.Data.Writeinfo;
import com.example.pikoproject.OnListener;
import com.example.pikoproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;

public class SharingActivity extends AppCompatActivity {
    private final static String TAG = "SharingActivity";
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;
    private SharingAdapter sharingAdapter;
    private ArrayList<Writeinfo> postList;
    private int successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        RecyclerView recyclerView =  findViewById(R.id.SharingrecycleView);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        postList = new ArrayList<>();
        ArrayList<Likeinfo> likelist = new ArrayList<>();
                sharingAdapter = new SharingAdapter(SharingActivity.this, postList, likelist);
        sharingAdapter.setOnListener(onListener);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SharingActivity.this));
        recyclerView.setAdapter(sharingAdapter);

    }


        @Override
    protected  void onResume(){
        super.onResume();
            postUpdate();
        }


        OnListener onListener = new OnListener() {
            @Override
            public void onDelete(int position) {
                final String id = postList.get(position).getId();

               ArrayList<String> contentsList = postList.get(position).getContents();
               for (int i = 0; i < contentsList.size() ; i ++){
                   String contents = contentsList.get(i);
                   if (Patterns.WEB_URL.matcher(contents).matches()&& contents.contains("https://firebasestorage.googleapis.com/v0/b/piko-mobile.appspot.com/o/posts%")) {
                       successCount++;
                        String[] list = contents.split("\\?");
                        String[] list2 = list[0].split("%2F");
                        String name = list2[list2.length -1];

                       StorageReference desertRef = storageRef.child("posts/" + id + "/" + name);
                       desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               successCount--;
                               storeUploader(id);
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(SharingActivity.this, " 문제가 발생하였습니다 ", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }
                storeUploader(id);
            }

            @Override
            public void onModify(int position ) {
                mstartActivity(AddsharingActivity.class, postList.get(position));


                Log.e("로그","수정");
            }
        };


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

        private void postUpdate(){
            firebaseFirestore = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = firebaseFirestore.collection("posts");
            collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).get() // query .....  사진 순서대로 나오기
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                postList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new Writeinfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>)document.getData().get("contents"),
                                            document.getData().get("publicsher").toString(),
                                            new Date(document.getDate("createdAt").getTime())
                                            ,document.getId()));
                                }
                                sharingAdapter.notifyDataSetChanged();// 삭제시 리사이클 초기화
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }


        private void storeUploader(String id){
            if(successCount == 0){
                firebaseFirestore.collection("posts").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SharingActivity.this,"삭제하였습니다.",Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                postUpdate();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SharingActivity.this,"삭제하지 못 하였습니다.",Toast.LENGTH_SHORT).show();
                                // Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        }


    public void mstartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    public void mstartActivity(Class c, Writeinfo writeinfo){
        Intent intent = new Intent(this, c);
        intent.putExtra("Info", writeinfo);
        startActivity(intent);
    }

}
