package com.example.pikoproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {
    public ArrayList<Paint> paints = new ArrayList <Paint>();
    public ArrayList<Path> paths = new ArrayList <Path>();
    Paint paint1 = new Paint(); //black
    Paint paint2 = new Paint(); //red
    Paint paint3 = new Paint(); //green
    Path path = new Path ();
    Path path1 = new Path(); //black
    Path path2 = new Path(); //red
    Path path3 = new Path(); //green
    public int color;
    public int i=0;

    public void mycolor (int a ){
        switch (a){
            case 1: color = 0xFF000000;
                i=1; paint1.setColor(color);
                path = path1;
                break;
            case 2: color = 0xFFFF0000;
                i=2; paint2.setColor(color);
                path = path2;
                break;
            case 3: color = 0xFF00FF00;
                i=3; paint3.setColor(color);
                path = path3;
                break;
        }
    }

    public DrawView(Context context, AttributeSet attrs){
        super(context, attrs);

        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(10f);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeJoin(Paint.Join.ROUND);
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(10f);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        paint3.setAntiAlias(true);
        paint3.setStrokeWidth(10f);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeJoin(Paint.Join.ROUND);

        paths.add(path1);
        paths.add(path2);
        paths.add(path3);
        paints.add(paint1);
        paints.add(paint2);
        paints.add(paint3);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i =0;i<paths.size();i++){
            canvas.drawPath(paths.get(i),paints.get(i));

        }}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX , eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(eventX , eventY);
                break;
            case MotionEvent.ACTION_UP: break;
            default: return false;
        }
        invalidate();
        return true;
    }
}

