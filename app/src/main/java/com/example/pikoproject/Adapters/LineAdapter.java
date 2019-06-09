package com.example.pikoproject.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pikoproject.Data.LineItem;
import com.example.pikoproject.R;

import java.util.ArrayList;

public class LineAdapter extends RecyclerView.Adapter<LineAdapter.VIewHolder>  {
    private ArrayList<LineItem> mDataset;  //adapter에 들어갈 list 선언.ㅎㅈ
    private OnItemClick2 mCallback;
    boolean isPageOpen = false;

    public static class VIewHolder extends RecyclerView.ViewHolder{


        public ImageView mimageview;

        public TextView ttextview;


        public VIewHolder(View view) {
            super(view);
            mimageview = (ImageView) view.findViewById(R.id.drama_image2);
        }
    }

    public LineAdapter(ArrayList<LineItem> myDataset, OnItemClick2 listener){
        mDataset = myDataset;
        this.mCallback = listener;

    }

    @Override
    public LineAdapter.VIewHolder onCreateViewHolder(ViewGroup parent, int viewType){ // 화면생성
        //context 와 viewgroup 생성자로 전달 받아 layoutinflater.from을 토행 view를 생성
        //layoutinflater를 이용하여 전 단계에서 만들었던 cardview.xml를 inflate 시킴 . 혁준
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview2, parent , false);
        VIewHolder viewholder = new VIewHolder(view);
        return viewholder;
    }


    @Override
    public void onBindViewHolder(VIewHolder holder , final int position){
        //position에서 데이터 요소 가져오기.혁준
        //cardview 하나하나 보여주는 함수.혁준
        Glide.with(holder.mimageview.getContext()).load(mDataset.get(position).getLineUrl()).into(holder.mimageview); // 글라이드 라이브러리 사용해서 이미지뷰 부르기.남준준        holder.mtextview.setText(mDataset.get(position).gps);

        holder.mimageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCallback.onClicked2(mDataset.get(position).getLineUrl());
            }
        });


    }

    @Override
    public int getItemCount(){
        return mDataset.size();
    }

}