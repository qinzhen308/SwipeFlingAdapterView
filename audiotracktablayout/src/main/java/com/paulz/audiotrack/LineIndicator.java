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

public class LineIndicator implements IIndicator{
    ArrayList<PointF> wavePoints=new ArrayList<>();

    Paint paint;
    Path path=new Path();

    RectF unitRect=new RectF(0,-1.5f,2,1.5f);

    private float waveWidth=2;

    public LineIndicator(){
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(waveWidth);
        wavePoints.add(new PointF(0,0));
        wavePoints.add(new PointF(2,0f));

    }

    public void setLineWidth(float width){
        waveWidth=width;
        paint.setStrokeWidth(waveWidth);
    }

    public void drawSelf(View view,Canvas canvas, int color, List<RectF > destRects, int targetPosition){
        canvas.save();

        drawNormalWave(canvas,color,destRects.get(targetPosition),wavePoints);
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
            path.lineTo(p.x,p.y);
        }
        matrix.postScale(1,-1,destRect.centerX(),destRect.centerY());
        path.transform(matrix);
        canvas.drawPath(path,paint);
    }

    public void doWave(final View view){

    }

    public void stopWave(){

    }


}
