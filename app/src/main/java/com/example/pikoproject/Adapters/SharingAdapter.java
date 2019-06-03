package com.example.pikoproject.Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pikoproject.Data.Writeinfo;
import com.example.pikoproject.OnListener;
import com.example.pikoproject.R;

import java.util.ArrayList;


public class SharingAdapter extends RecyclerView.Adapter<SharingAdapter.SharingViewHolder> {
    private ArrayList<Writeinfo> mDataset;
    private Activity activity;
    private OnListener onListener;


     static class SharingViewHolder extends RecyclerView.ViewHolder {

         CardView cardView;
         SharingViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public SharingAdapter(Activity activity , ArrayList<Writeinfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }

    public void setOnListener(OnListener onListener){
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
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 포스터 클릭시



            }
        });

        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, sharingViewHolder.getAdapterPosition());
            }
        });
        return sharingViewHolder;
    }


    @Override
    public void onBindViewHolder(final SharingViewHolder holder,  int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titletextView);
        titleTextView.setText(mDataset.get(position).getTitle());

        TextView createAtTextVIew = (TextView)cardView.findViewById(R.id.createAttextVIew);
        //createAtTextVIew.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));


        LinearLayout contentsLayout = cardView.findViewById(R.id.contentLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = mDataset.get(position).getContents();

        if(contentsLayout.getTag() == null || !contentsLayout.getTag().equals(contentsList)){
            contentsLayout.setTag(contentsList);
            contentsLayout.removeAllViews();
            final int MORE_INDEX = 2;
            for (int i = 0; i < contentsList.size(); i++) {
                if(i == MORE_INDEX){
                    TextView textView = new TextView(activity);
                    textView.setLayoutParams(layoutParams);
                    textView.setText("더보기...");
                    contentsLayout.addView(textView);
                    break;
                }

                String contents = contentsList.get(i);
                if (Patterns.WEB_URL.matcher(contents).matches() && contents.contains("https://firebasestorage.googleapis.com/v0/b/piko-mobile.appspot.com/o/posts%")) {
                    /*ImageView imageView = new ImageView(activity);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    contentsLayout.addView(imageView);
                    Glide.with(activity).load(contents).override(500,500).thumbnail(0.1f).into(imageView);*/
                } else {
                 /*   TextView textView = new TextView(activity);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(contents);
                    textView.setTextColor(Color.rgb(0,0,0));
                    contentsLayout.addView(textView);*/
                }
              }
           }
       }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    private void showPopup(View v ,final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String id = mDataset.get(position).getId();
                switch(menuItem.getItemId()){
                    case R.id.modify :
                        onListener.onModify(position);
                       return true;
                    case R.id.delete :
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


