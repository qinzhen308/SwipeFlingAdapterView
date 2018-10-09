package com.paulz.audiotrack;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Paul Z on 2018/10/9.
 * Description:
 */

public class AudioTrack {
    ArrayList<PointF> wavePoints=new ArrayList<>();
    ArrayList<PointF> carPoints=new ArrayList<>();

    Paint paint;
    Path path=new Path();

    public AudioTrack(){
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        wavePoints.add(new PointF(0,0));
        wavePoints.add(new PointF(0.1f,0.2f));
        wavePoints.add(new PointF(0.2f,0.0f));
        wavePoints.add(new PointF(0.3f,0.4f));
        wavePoints.add(new PointF(0.4f,0.0f));
        wavePoints.add(new PointF(0.5f,0.3f));
        wavePoints.add(new PointF(0.6f,0.0f));
        wavePoints.add(new PointF(0.7f,0.6f));
        wavePoints.add(new PointF(0.8f,0.0f));
        wavePoints.add(new PointF(0.9f,0.3f));
        wavePoints.add(new PointF(1,0f));
    }

    public void drawSelf(Canvas canvas, int color, RectF destRect){
        path.reset();
        paint.setColor(color);
        Matrix matrix=new Matrix();
        matrix.setRectToRect(new RectF(0,0,1,1),destRect, Matrix.ScaleToFit.FILL);
        PointF p0=wavePoints.get(0);
        PointF p;
        path.moveTo(p0.x,p0.y);
        for(int i=1;i<wavePoints.size();i++){
            p=wavePoints.get(i);
            path.quadTo(p0.x,p0.y,(p.x+p0.x)/2,(p.y+p0.y)/2);
            p0=p;
        }
        path.transform(matrix);
        canvas.drawPath(path,paint);
    }


    private class TrackWave{
        ArrayList<PointF> points=new ArrayList<>();



    }
}
