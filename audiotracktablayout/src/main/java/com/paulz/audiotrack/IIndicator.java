package com.paulz.audiotrack;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import java.util.List;

/**
 * Created by Paul Z on 2019/3/14.
 * Description:
 */
public interface IIndicator {
    public void drawSelf(View view, Canvas canvas, int color, List<RectF > destRects, int targetPosition);
    public void doWave(View view);
    public void stopWave();
    void setLineWidth(float width);
}
