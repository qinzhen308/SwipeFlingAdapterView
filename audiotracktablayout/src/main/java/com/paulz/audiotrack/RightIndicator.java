package com.paulz.audiotrack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paul Z on 2019/4/18.
 * Description:
 */
public abstract class RightIndicator {

    private int position;
    private Paint paint;

    private int leftPadding;
    private int dp3;
    private int dp6;


    public int getPosition() {
        return position;
    }

    public RightIndicator(int position) {
        this.position = position;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    private void initDimentions(Context context) {
        if (leftPadding > 0) return;
        leftPadding = context.getResources().getDimensionPixelOffset(R.dimen.dp4);
        dp6 = context.getResources().getDimensionPixelOffset(R.dimen.dp6);
        dp3 = context.getResources().getDimensionPixelOffset(R.dimen.dp3);

    }

    public void drawSelf(Canvas canvas, ViewGroup container, float zoomMax, int tabTextSize, int tabPadding, int color) {

        if (position >= container.getChildCount()) {
            return;
        }
        initDimentions(container.getContext());
        View textTab = container.getChildAt(position);
        RectF textRect = new RectF();
        textRect.right = (textTab.getWidth() - 2 * tabPadding) * (zoomMax + 1) + textTab.getLeft() + tabPadding + container.getPaddingLeft() + container.getLeft();
        textRect.bottom = textTab.getHeight();
        textRect.top = textRect.bottom - tabTextSize * (zoomMax + 1);
        Path path = new Path();
        path.moveTo(textRect.right + leftPadding + dp3, textRect.centerY() + dp3 / 2);
        path.lineTo(textRect.right + leftPadding, textRect.centerY() - dp3 / 2);
        path.lineTo(textRect.right + leftPadding + dp6, textRect.centerY() - dp3 / 2);
        path.lineTo(textRect.right + leftPadding + dp3, textRect.centerY() + dp3 / 2);
        paint.setColor(color);
        canvas.drawPath(path, paint);
    }


    public abstract boolean OnTabExcessClick();
}
