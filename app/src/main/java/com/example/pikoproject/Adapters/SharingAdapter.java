package com.example.pikoproject.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pikoproject.Activity.CameraActivity;
import com.example.pikoproject.Camera.Camera2BasicFragment;
import com.example.pikoproject.Data.Likeinfo;
import com.example.pikoproject.Data.Writeinfo;
import com.example.pikoproject.OnListener;
import com.example.pikoproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SharingAdapter extends RecyclerView.Adapter<SharingAdapter.SharingViewHolder> {
    private ArrayList<Writeinfo> mDataset;
    private ArrayList<Likeinfo> lDateset;
    private Activity activity;
    private OnListener onListener;
    static String imageUri;
    private Button chatsend;
    private CheckBox heart;
    private FirebaseUser user;
    private Context mcontext;
    private Button chatbtn;
    public static String userliked;


    static class SharingViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        SharingViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public SharingAdapter(ArrayList<Writeinfo> myDataset, ArrayList<Likeinfo> lDateset) {
        this.mDataset = myDataset;
        this.lDateset = lDateset;

    }

    public SharingAdapter(Activity activity, ArrayList<Writeinfo> myDataset, ArrayList<Likeinfo> lDateset) {
        this.mDataset = myDataset;
        this.lDateset = lDateset;
        this.activity = activity;

    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public SharingAdapter.SharingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // position 값은 viewType 으로 알수 있다
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sharing, parent, false);
        final SharingViewHolder sharingViewHolder = new SharingViewHolder(cardView);
        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, sharingViewHolder.getAdapterPosition());
            }
        });
        return sharingViewHolder;
    }


    @Override
    public void onBindViewHolder(final SharingViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Writeinfo name = mDataset.get(position);
                ArrayList<String> contentList = name.getContents();
                for (int i = 0; i < contentList.size(); i++) {
                    String content = contentList.get(i);
                    if (content.contains("http")) {
                        imageUri = content;

                    }
                }
                Camera2BasicFragment c2r = new Camera2BasicFragment();
                Messenger messenger = c2r.getMessenger();
                Message msg = Message.obtain();
                msg.obj = imageUri; // 사진 주소 보내기
                try {
                    messenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(v.getContext(), CameraActivity.class);

                v.getContext().startActivity(intent);

            }
        });

        final CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titletextView);
        titleTextView.setText(mDataset.get(position).getTitle());


        heart = cardView.findViewById(R.id.heart);
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                String path = mDataset.get(position).getId();//사용자 고유 아이디 getId() -> 문서 아이디
                final Writeinfo writeinfo = mDataset.get(position);
                String username = user.getUid();
                final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


                if (((CheckBox) v).isChecked()) {
                    DocumentReference docRef = firebaseFirestore.collection("posts").document(mDataset.get(position).getId().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    String count = document.getData().get("likecount").toString();
                                    int num = Integer.parseInt(count);
                                    num += 1;
                                    count = Integer.toString(num);

                                    DocumentReference db = firebaseFirestore.collection("posts").document(writeinfo.getId());
                                    Map<String, Object> Lcount = new HashMap<>();
                                    Lcount.put("likecount", count);
                                    Lcount.put("userliked", true);
                                    db.set(Lcount, SetOptions.merge());

                                } else {
                                    //Log.d(TAG, "No such document");
                                }
                            } else {
                                //Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });


                    // TODO : CheckBox is checked.
                } else {
                    // TODO : CheckBox is unchecked.


                    DocumentReference docRef = firebaseFirestore.collection("posts").document(mDataset.get(position).getId().toString());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    String count = document.getData().get("likecount").toString();
                                    int num = Integer.parseInt(count);
                                    num -= 1;
                                    count = Integer.toString(num);

                                    DocumentReference db = firebaseFirestore.collection("posts").document(writeinfo.getId());
                                    Map<String, Object> Lcount = new HashMap<>();
                                    Lcount.put("likecount", count);
                                    Lcount.put("userliked", false);
                                    db.set(Lcount, SetOptions.merge());

                                } else {
                                    //Log.d(TAG, "No such document");
                                }
                            } else {
                                //Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });

                }

                final DocumentReference documentReference = writeinfo == null ? firebaseFirestore.collection("posts").document() : firebaseFirestore.collection("posts").document(writeinfo.getId());
                CollectionReference likeRef = documentReference.collection("likes");


                //좋아요 추가
                Map<String, Object> likeMap = new HashMap<>();
                likeMap.put("publicsher", username);
                likeMap.put("userliked", true);
                likeMap.put("createAt", new Date());
                likeMap.put("id", likeRef.document().getId());

                Toast.makeText(activity, likeRef.document().getPath(),Toast.LENGTH_LONG).show();
                likeRef.add(likeMap);


/*                final DocumentReference documentReference = writeinfo == null ?firebaseFirestore.collection("posts").document() : firebaseFirestore.collection("posts").document(writeinfo.getId()) ;
                CollectionReference likeRef =  documentReference.collection("likes");*/



  /*                  //좋아요 취소
                    DocumentReference userlikeRef = likeRef.document(writeinfo.getLikeid());
                    userlikeRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(activity,"좋아요 취소",Toast.LENGTH_LONG).show();
                        }
                    });*/


            }
        });
        TextView createAt = cardView.findViewById(R.id.createAt);
        createAt.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        TextView userEmail = cardView.findViewById(R.id.emailView);
        userEmail.setText(mDataset.get(position).getEmail());

        TextView likecountView = cardView.findViewById(R.id.likecountView);


        final FirebaseFirestore dd = FirebaseFirestore.getInstance();
        DocumentReference docRef = dd.collection("posts").document(mDataset.get(position).getId().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String like = document.getData().get("userliked").toString();
                        String userlike = "true";
                        if (userlike.equals(like)) {
                            heart.setChecked(true);
                        } else {
                            heart.setChecked(false);

                        }

                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        String likecount = String.valueOf(mDataset.get(position).getLikecount());
        likecountView.setText("좋아요 "+likecount+"개");

/*        chatbtn = cardView.findViewById(R.id.chatsend);
       chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String chatcontents = ((TextView) cardView.findViewById(R.id.chatcontentText)).getText().toString();
                if (chatcontents.length() > 0) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    String path = mDataset.get(position).getId();
                    Toast.makeText(activity, "  " + path, Toast.LENGTH_LONG).show();


                    final ArrayList<String> chats = new ArrayList<>();
                    chats.add("test1");

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    firebaseFirestore.collection("posts").document(path).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot document = task1.getResult();
                                //   chats = (ArrayList<String>) document.getData().get("chat").toString();

                            } else {

                            }
                        }
                    });

                    chats.add(chatcontents);
                    Map<String, Object> data = new HashMap<>();// 보내기
                    data.put("chat", (ArrayList) chats);
                    firebaseFirestore.collection("posts").document(path).set(data, SetOptions.merge());


                }

            }
        });*/

        LinearLayout contentsLayout = cardView.findViewById(R.id.contentLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = mDataset.get(position).getContents();


        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(contentsList)) {
            contentsLayout.setTag(contentsList);
            contentsLayout.removeAllViews();
            final int MORE_INDEX = 2;
            for (int i = 0; i < contentsList.size(); i++) {
                if (i == MORE_INDEX) {
                    TextView textView = new TextView(activity);
                    textView.setLayoutParams(layoutParams);
                    textView.setText("더보기...");
                    contentsLayout.addView(textView);
                    break;
                }

                String contents = contentsList.get(i);
                if (Patterns.WEB_URL.matcher(contents).matches() && contents.contains("https://firebasestorage.googleapis.com/v0/b/piko-mobile.appspot.com/o/posts%")) {
                    ImageView imageView = new ImageView(activity);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    contentsLayout.addView(imageView);
                    Glide.with(activity).load(contents).override(250, 250).thumbnail(0.1f).into(imageView);
                    break;
                } else {
                    TextView textView = new TextView(activity);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(contents);
                    textView.setTextColor(Color.rgb(0, 0, 0));
                    contentsLayout.addView(textView);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String id = mDataset.get(position).getId();
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        onListener.onModify(position);
                        return true;
                    case R.id.delete:
                        onListener.onDelete(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }
}

