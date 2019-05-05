package com.example.pikoproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.VIewHolder> {

    private ArrayList<item> mDataset;

    public static class VIewHolder extends RecyclerView.ViewHolder{

        public ImageView mimageview;
        public TextView  mtextview;
        public TextView  ttextview;


        public VIewHolder(View view) {
            super(view);
            mimageview = (ImageView) view.findViewById(R.id.drama_image);
            mtextview = (TextView) view.findViewById(R.id.drama_gps);
            ttextview = (TextView) view.findViewById(R.id.drama_title);
        }
    }

    public adapter(ArrayList<item> myDataset){
        mDataset = myDataset;
    }

    @Override
    public adapter.VIewHolder onCreateViewHolder(ViewGroup parent, int viewType){ // 화면생성

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent , false);
        VIewHolder viewholder = new VIewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(VIewHolder holder , int position){ // 생성한거를 가지고 리사이클에다가 위치번호 나열

        Glide.with(holder.mimageview.getContext()).load(mDataset.get(position).getPicUrl()).into(holder.mimageview); // 글라이드 라이브러리 사용해서 이미지뷰 부르기

        holder.mtextview.setText(mDataset.get(position).gps);
        holder.ttextview.setText(mDataset.get(position).title);
    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }
}
