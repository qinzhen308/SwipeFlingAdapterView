package com.paulz.audiotrack;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Paul Z on 2018/10/9.
 * Description:
 */

public class AudioTrack implements IIndicator{
    ArrayList<PointF> wavePoints=new ArrayList<>();

    Paint paint;
    Path path=new Path();

    RectF unitRect=new RectF(0,-1.5f,2,1.5f);

    private float waveWidth=2;

    public AudioTrack(){
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(waveWidth);
        wavePoints.add(new PointF(0,0));
        wavePoints.add(new PointF(0.1f,0.2f));
        wavePoints.add(new PointF(0.2f,-0.4f));
        wavePoints.add(new PointF(0.3f,0.7f));
        wavePoints.add(new PointF(0.4f,-0.99f));
        wavePoints.add(new PointF(0.5f,0.9f));
        wavePoints.add(new PointF(0.6f,-0.3f));
        wavePoints.add(new PointF(0.7f,0.8f));
        wavePoints.add(new PointF(0.8f,-0.9f));
        wavePoints.add(new PointF(0.9f,0.6f));
        wavePoints.add(new PointF(1,-0.8f));
        wavePoints.add(new PointF(1.1f,0.7f));
        wavePoints.add(new PointF(1.2f,-0.7f));
        wavePoints.add(new PointF(1.3f,0.75f));
        wavePoints.add(new PointF(1.4f,-0.85f));
        wavePoints.add(new PointF(1.5f,0.7f));
        wavePoints.add(new PointF(1.6f,-0.5f));
        wavePoints.add(new PointF(1.7f,0.3f));
        wavePoints.add(new PointF(1.8f,-0.31f));
        wavePoints.add(new PointF(1.9f,0.15f));
        wavePoints.add(new PointF(2,0f));

    }

    public void setLineWidth(float width){
        waveWidth=width;
        paint.setStrokeWidth(waveWidth);
    }

    public void drawSelf(View view,Canvas canvas, int color, List<RectF > destRects, int targetPosition){
        canvas.save();

        for(int i=0;i<destRects.size();i++){

            if(animating&&i==targetPosition){
                drawAnimateWave(canvas,color,destRects.get(i),wavePoints);
            }else {
                drawNormalWave(canvas,color,destRects.get(i),wavePoints);
            }
        }
        canvas.restore();


        canvas.save();
        Path clipPath=new Path();
        for(int i=0;i<destRects.size();i++){
            clipPath.addRect(destRects.get(i), Path.Direction.CCW);
        }
        canvas.clipPath(clipPath,Region.Op.DIFFERENCE);


        canvas.drawLine(0,destRects.get(0).centerY(),view.getWidth(),destRects.get(0).centerY(),paint);
        canvas.restore();

    }


    public void drawNormalWave(Canvas canvas, int color, RectF destRect,List<PointF> points){
        path.reset();
        paint.setColor(color);
        Matrix matrix=new Matrix();
        matrix.setRectToRect(unitRect,destRect, Matrix.ScaleToFit.FILL);
        PointF p0=points.get(0);
        PointF p;
        path.moveTo(p0.x,p0.y);
        for(int i=1;i<points.size();i++){
            p=points.get(i);
            path.quadTo(p0.x,p0.y,(p.x+p0.x)/2,(p.y+p0.y)/2);
            p0=p;
        }
        path.lineTo(p0.x,p0.y);
        matrix.postScale(1,-1,destRect.centerX(),destRect.centerY());
        path.transform(matrix);
        canvas.drawPath(path,paint);
//        canvas.drawLine(0,destRect.bottom,destRect.left,destRect.bottom,paint);
//        canvas.drawLine(destRect.right,destRect.bottom,1000,destRect.bottom,paint);
    }

    public void drawAnimateWave(Canvas canvas, int color, RectF destRect,List<PointF> points){
        path.reset();
        paint.setColor(color);
        Matrix matrix=new Matrix();
        matrix.setRectToRect(unitRect,destRect, Matrix.ScaleToFit.FILL);
        PointF p0=points.get(0);
        PointF p;
        path.moveTo(p0.x,p0.y);

        float[] scales=mScales;
        float scale0=scales[0];
        float scale1=1;
        for(int i=1;i<points.size();i++){
            scale1=scales[i%scales.length];
            p=points.get(i);
            path.quadTo(p0.x,p0.y*scale0,(p.x+p0.x)/2,(p.y*scale1+p0.y*scale0)/2);
            p0=p;
            scale0=scale1;
        }
        path.lineTo(p0.x,p0.y*scale0);
        matrix.postScale(1,-1,destRect.centerX(),destRect.centerY());
        path.transform(matrix);
        canvas.drawPath(path,paint);
//        canvas.drawLine(0,destRect.bottom,destRect.left,destRect.bottom,paint);
//        canvas.drawLine(destRect.right,destRect.bottom,1000,destRect.bottom,paint);
    }

    private void randomScales(){
        mScales=new float[new Random().nextInt(10)+10];
        for(int i=0;i<mScales.length;i++){
            mScales[i]= 2*((float)Math.random()-0.5f);
        }
    }
    float[] mScales;
    private boolean animating;
    Timer timer;
    int time=0;
    int delay=100;
    public void doWave(final View view){
        if(timer!=null){
            timer.cancel();
        }
        randomScales();
        animating=true;
        timer=new Timer();
        time=0;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                randomScales();
                if(time>=1000){
                    animating=false;
                    cancel();
                }else {
                    time+=delay;
                }
                view.postInvalidate();
            }
        },delay,delay);
    }

    public void stopWave(){
        if(timer!=null){
            timer.cancel();
            timer=null;
            animating=false;
        }
    }


}
