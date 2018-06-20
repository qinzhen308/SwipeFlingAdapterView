package com.example.paulz.animdemo.overlap;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Paul Z on 2018/6/11.
 * Description:重叠布局
 * 支持横向或纵向排列
 * 关于gravity：
 *  1、纵向时子视图可按left、right、center排列
 *  2、横向时子视图可按top、bottom、center排列
 * 重叠顺序：
 *  1、前一项覆盖后一项
 *  2、后一项覆盖前一项
 *  3、中间的覆盖两边（包含中心偏左或偏右两种）
 *  4、两边的覆盖中间的（包含中心偏左或偏右两种）
 */
public class OverlapLinearLayoutManager extends RecyclerView.LayoutManager{
    private float overlapOffset=0.5f;//[0-1] 0完全重合，1相切

    private Rect mTempRect=new Rect();
    private int gravity= Gravity.TOP;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int orientation= HORIZONTAL;

    private OverlapMode overlapMode=OverlapMode.FIRST_BELOW_LAST;

    public OverlapLinearLayoutManager(int orientation,OverlapMode overlapMode){
        this.orientation=orientation;
        this.overlapMode=overlapMode;
        setAutoMeasureEnabled(true);
    }
    public OverlapLinearLayoutManager(int orientation,OverlapMode overlapMode,float offset){
        this.orientation=orientation;
        this.overlapMode=overlapMode;
        this.overlapOffset=offset;
        setAutoMeasureEnabled(true);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        removeAndRecycleAllViews(recycler);

        if(getItemCount()==0)return;
        if(orientation==VERTICAL){
            verticalLayout(recycler);
        }else {
            horizontalLayout(recycler);
        }

    }


    private void horizontalLayout(RecyclerView.Recycler recycler){
        int x=getPaddingLeft(),y=getPaddingTop(),maxHeight=0;
        final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        if(verticalGravity!=Gravity.TOP){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child,0,0);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
            }
        }

        if(overlapMode==OverlapMode.FIRST_BELOW_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
                switch (verticalGravity){
                    case Gravity.BOTTOM:
                        y=getPaddingTop()+maxHeight-height;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        y=getPaddingTop()+(maxHeight-height)/2;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    x+=width;
                }else {
                    x+=width*overlapOffset;
                }
            }
        }else if (overlapMode==OverlapMode.FIRST_ABOVE_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child,0);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
                switch (verticalGravity){
                    case Gravity.BOTTOM:
                        y=getPaddingTop()+maxHeight-height;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        y=getPaddingTop()+(maxHeight-height)/2;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    x+=width;
                }else {
                    x+=width*overlapOffset;
                }
            }
        }else if (overlapMode==OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_FRONT |overlapMode==OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_BEHIND){
            int middlePosition=overlapMode==OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_FRONT ?(getItemCount()-1)/2:(getItemCount()/2);
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                if(i<=middlePosition){
                    addView(child);
                }else if(i>middlePosition){
                    addView(child,middlePosition);
                }
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
                switch (verticalGravity){
                    case Gravity.BOTTOM:
                        y=getPaddingTop()+maxHeight-height;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        y=getPaddingTop()+(maxHeight-height)/2;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    x+=width;
                }else {
                    x+=width*overlapOffset;
                }
            }
        }else if(overlapMode==OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_FRONT |overlapMode==OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_BEHIND){
            int middlePosition=overlapMode==OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_FRONT ?(getItemCount()-1)/2:(getItemCount()/2);
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                if(i<=middlePosition){
                    addView(child,0);
                }else if(i>middlePosition){
                    addView(child);
                }
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
                switch (verticalGravity){
                    case Gravity.BOTTOM:
                        y=getPaddingTop()+maxHeight-height;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        y=getPaddingTop()+(maxHeight-height)/2;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    x+=width;
                }else {
                    x+=width*overlapOffset;
                }
            }
        }

        mTempRect.set(0,0,x+getPaddingRight(),getPaddingTop()+maxHeight+getPaddingBottom());
    }

    private void verticalLayout(RecyclerView.Recycler recycler){
        final int layoutDirection = getLayoutDirection();
        final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        int x=getPaddingLeft(),y=getPaddingTop(),maxWidth=0;
        if((absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK)!=Gravity.LEFT){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                if(width>maxWidth)maxWidth=width;
            }
        }
        if(overlapMode==OverlapMode.FIRST_BELOW_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(width>maxWidth)maxWidth=width;
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        x=getPaddingLeft()+(maxWidth-width)/2;
                        break;
                    case Gravity.RIGHT:
                        x=getPaddingLeft()+maxWidth-width;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    y+=height;
                }else {
                    y+=height*overlapOffset;
                }
            }
        }else if (overlapMode==OverlapMode.FIRST_ABOVE_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child,0);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(width>maxWidth)maxWidth=width;
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        x=getPaddingLeft()+(maxWidth-width)/2;
                        break;
                    case Gravity.RIGHT:
                        x=getPaddingLeft()+maxWidth-width;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    y+=height;
                }else {
                    y+=height*overlapOffset;
                }
            }
        }else if (overlapMode==OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_FRONT |overlapMode==OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_BEHIND){
            int middlePosition=overlapMode==OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_FRONT ?(getItemCount()-1)/2:(getItemCount()/2);
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                if(i<=middlePosition){
                    addView(child);
                }else if(i>middlePosition){
                    addView(child,middlePosition);
                }
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(width>maxWidth)maxWidth=width;
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        x=getPaddingLeft()+(maxWidth-width)/2;
                        break;
                    case Gravity.RIGHT:
                        x=getPaddingLeft()+maxWidth-width;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    y+=height;
                }else {
                    y+=height*overlapOffset;
                }

            }
        }else if(overlapMode==OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_FRONT |overlapMode==OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_BEHIND){
            int middlePosition=overlapMode==OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_FRONT ?(getItemCount()-1)/2:(getItemCount()/2);
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                if(i<=middlePosition){
                    addView(child,0);
                }else if(i>middlePosition){
                    addView(child);
                }
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(width>maxWidth)maxWidth=width;
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        x=getPaddingLeft()+(maxWidth-width)/2;
                        break;
                    case Gravity.RIGHT:
                        x=getPaddingLeft()+maxWidth-width;
                        break;
                    default:
                        break;
                }
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                if(i==getItemCount()-1){
                    y+=height;
                }else {
                    y+=height*overlapOffset;
                }
            }
        }
        mTempRect.set(0,0,getPaddingLeft()+maxWidth+getPaddingRight(),y+getPaddingBottom());
    }



    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        // we don't like it to wrap content in our non-scroll direction.
        final int width, height;
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        final int usedWidth = childrenBounds.width() + horizontalPadding;
        width = chooseSize(wSpec, mTempRect.width(), getMinimumWidth());
        height = chooseSize(hSpec, mTempRect.height(),
                getMinimumHeight());
        setMeasuredDimension(width, height);

    }

    public void setGravity(int gravity){
        this.gravity=gravity;
    }

    public void setOverlapOffset(int offset){
        this.overlapOffset=offset;
    }

    public enum OverlapMode{
        FIRST_BELOW_LAST,FIRST_ABOVE_LAST,
        MIDDLE_ABOVE_OTHERS_INCLINED_FRONT, MIDDLE_ABOVE_OTHERS_INCLINED_BEHIND,
        MIDDLE_BELOW_OTHERS_INCLINED_FRONT, MIDDLE_BELOW_OTHERS_INCLINED_BEHIND,
    }
}
