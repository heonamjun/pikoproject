package com.example.pikoproject.Activity;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public class shareFragment extends Fragment {
    private final static String TAG = "SharingActivity";
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;
    private RelativeLayout loaderLayout;
    private SharingAdapter sharingAdapter;
    private ArrayList<Writeinfo> postList;
    private ArrayList<Likeinfo> likeList;
    private FirebaseUser firebaseUser;
    private int successCount;
    private Context mContext ;
    protected String uesrid;
    protected String uesremail;
    static int likesCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        postUpdate();

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        postUpdate();
        View view = inflater.inflate(R.layout.activity_sharing,container,false);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.SharingrecycleView);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            Intent intent = new Intent(getActivity() , LoginActivity.class);
            startActivity(intent);

        }else{
            uesrid = firebaseUser.getUid();
            uesremail = firebaseUser.getEmail();

        }

        Button loginout= (Button) view.findViewById(R.id.Loginout2);
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity() , LoginActivity.class);
                startActivity(intent);
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        postList = new ArrayList<>();
        likeList = new ArrayList<>();
        sharingAdapter = new SharingAdapter(getActivity(), postList, likeList);
        sharingAdapter.setOnListener(onListener);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(sharingAdapter);
        return  view;
    }


    @Override
    public void onStart() {
        super.onStart();
        postUpdate();
    }

    @Override
    public void onResume(){
        super.onResume();
        postUpdate();
    }

    OnListener onListener = new OnListener() {
        @Override
        public void onDelete(int position) {
//            loaderLayout.setVisibility(View.VISIBLE);
            final String id = postList.get(position).getId();



                             /* StorageReference desertRef = storageRef.child("posts/" + writeinfo.getId() + "/" + name);
                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AddsharingActivity.this, " 문제가 발생하였습니다 ", Toast.LENGTH_SHORT).show();
                        }
                    });*/


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
                            // loaderLayout.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // loaderLayout.setVisibility(View.GONE);
                            //Toast.makeText(SharingActivity.this, " 문제가 발생하였습니다 ", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(getActivity(),AddsharingActivity.class);
                    startActivity(intent);

                    break;
            }
        }
    };





    private void postUpdate(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = firebaseFirestore.collection("posts");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).get() // query .....  사진 순서대로 나오기
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            postList.clear();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
/*                              postList.add(new Writeinfo(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>)document.getData().get("contents"),
                                        document.getData().get("publicsher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId()));*/

                                DocumentReference postRef = document.getReference();
                                final CollectionReference likeRef = postRef.collection("likes");
                                likeRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                        if(task1.isSuccessful()){
                                            //likes 콜렉션을 받아오는데 성공
                                            QuerySnapshot likesresult = task1.getResult();
                                            likesCount = likesresult.size();
                                        }
                                    }
                                });


                                Map<String, Object> data = new HashMap<>();// 보내기
                                data.put("likecount",likesCount);
                                firebaseFirestore.collection("posts").document(document.getId()).set(data, SetOptions.merge());



                                // likesCount += 1 ;
                                String likecount = Integer.toString(likesCount);

                                final Writeinfo writeinfo = new Writeinfo(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>)document.getData().get("contents"),
                                        document.getData().get("publicsher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId(),
                                        document.getData().get("email").toString(),
                                        likecount
                                );



                                postList.add(writeinfo);
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
                            Toast.makeText(getActivity(),"삭제하였습니다.",Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "DocumentSnapshot successfully deleted!");

                            postUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //      Toast.makeText(SharingActivity.this,"삭제하지 못 하였습니다.",Toast.LENGTH_SHORT).show();
                            // Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
    }



    public void mstartActivity(Class c){
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }
    public void mstartActivity(Class c,Writeinfo writeinfo){
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra("Info", writeinfo);
        startActivity(intent);
    }
}