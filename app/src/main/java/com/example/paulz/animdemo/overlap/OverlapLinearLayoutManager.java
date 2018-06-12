package com.example.paulz.animdemo.overlap;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paul Z on 2018/6/11.
 * Description:
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
        if(overlapMode==OverlapMode.FIRST_BELOW_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                x+=width*overlapOffset;
            }
        }else if (overlapMode==OverlapMode.FIRST_ABOVE_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child,0);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(height>maxHeight)maxHeight=height;
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                x+=width*overlapOffset;
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
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                x+=width*overlapOffset;
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
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                x+=width*overlapOffset;
            }
        }

        mTempRect.set(0,0,x+getPaddingRight(),y+maxHeight+getPaddingBottom());
    }

    private void verticalLayout(RecyclerView.Recycler recycler){
        int x=getPaddingLeft(),y=getPaddingTop(),maxWidth=0;
        if(overlapMode==OverlapMode.FIRST_BELOW_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(width>maxWidth)maxWidth=width;
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                y+=height*overlapOffset;
            }
        }else if (overlapMode==OverlapMode.FIRST_ABOVE_LAST){
            for(int i=0;i<getItemCount();i++){
                View child=recycler.getViewForPosition(i);
                addView(child,0);
                measureChildWithMargins(child,0,0);
                int width=getDecoratedMeasuredWidth(child);
                int height=getDecoratedMeasuredHeight(child);
                if(width>maxWidth)maxWidth=width;
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                y+=height*overlapOffset;
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
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                y+=height*overlapOffset;

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
                layoutDecoratedWithMargins(child,x,y,x+width,y+height);
                y+=height*overlapOffset;
            }
        }

        mTempRect.set(0,0,x+getPaddingRight(),y+maxWidth+getPaddingBottom());
    }



    @Override
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        // we don't like it to wrap content in our non-scroll direction.
        final int width, height;
        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int verticalPadding = getPaddingTop() + getPaddingBottom();
        final int usedWidth = childrenBounds.width() + horizontalPadding;
        width = chooseSize(wSpec, usedWidth, getMinimumWidth());
        height = chooseSize(hSpec, mTempRect.height(),
                getMinimumHeight());
        setMeasuredDimension(width, height);

    }



    public enum OverlapMode{
        FIRST_BELOW_LAST,FIRST_ABOVE_LAST,
        MIDDLE_ABOVE_OTHERS_INCLINED_FRONT, MIDDLE_ABOVE_OTHERS_INCLINED_BEHIND,
        MIDDLE_BELOW_OTHERS_INCLINED_FRONT, MIDDLE_BELOW_OTHERS_INCLINED_BEHIND,
    }
}
