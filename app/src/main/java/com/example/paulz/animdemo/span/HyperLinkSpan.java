package com.example.paulz.animdemo.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Paul Z on 2019/5/15.
 * Description:
 */
public class HyperLinkSpan extends MiddleImageSpan{
    public String describe;
    public int color=Color.parseColor("#4a4a4a");

    public HyperLinkSpan(Context context, int resourceId,String describe) {
        super(context, resourceId);
        this.describe=describe;
    }

    public HyperLinkSpan(Context context, int resourceId,String describe,int color) {
        super(context, resourceId);
        this.describe=describe;
        this.color=color;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        canvas.save();
        Drawable drawable=getDrawable();
        int drawableW=0;
        if(drawable!=null){
            drawableW=drawable.getBounds().right;
        }

        canvas.translate(x+drawableW, y);
        Paint tempPaint=new Paint(paint);
        tempPaint.setColor(color);
        canvas.drawText(describe,0,describe.length(),0,0,tempPaint);
        canvas.restore();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        int imageWidth=super.getSize(paint, text, start, end, fm);
        Paint tempPaint=new Paint(paint);
        Rect rect=new Rect();
        tempPaint.getTextBounds(describe,0,describe.length(),rect);
        return imageWidth+rect.right;
    }
}
