package com.example.paulz.animdemo.centergridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.paulz.animdemo.R;

/**
 * Created by Paul Z on 2019/1/25.
 * Description:
 */
public class CenterGridView extends ConstraintLayout {
    private int columnNum;
    private int horizontalSpace;
    private int verticalSpace;
    private int childWidth;


    BaseAdapter adapter;

    public CenterGridView(Context context) {
        super(context);
    }

    public CenterGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CenterGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=View.MeasureSpec.getSize(widthMeasureSpec);
        childWidth=(width-getPaddingLeft()-getPaddingRight()-horizontalSpace*(columnNum-1))/columnNum;
        /*for(int i=0,size=getChildCount();i<size;i++){
            populate(getChildAt(i),i,size,false);
        }*/
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void init(Context context, AttributeSet attrs, int style){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterGridView, style, 0);

        columnNum=a.getInt(R.styleable.CenterGridView_cgv_columnNum,1);
        horizontalSpace=a.getDimensionPixelSize(R.styleable.CenterGridView_cgv_horizontalSpace,0);
        verticalSpace=a.getDimensionPixelSize(R.styleable.CenterGridView_cgv_verticalSpace,0);

    }


    void populate(){
        if(adapter==null){
            removeAllViews();
            return;
        }
        int count=adapter.getCount();
        if(count==0){
            removeAllViews();
            return;
        }
        removeAllViews();
        constraintSet=new ConstraintSet();

        for(int i=0;i<count;i++){
            populate(adapter.getView(i,null,this),i,count,true);
        }
        int[] ids=null;
        int rowSize=(count-1)/columnNum;
        for(int i=0;i<=(count-1)/columnNum;i++){
            if(i==rowSize){
                ids=new int[count%columnNum];
            }else {
                ids=new int[columnNum];
            }
            for(int j=0;j<ids.length;j++){
                ids[j]=getChildAt(i*columnNum+j).getId();
            }
//            constraintSet.createHorizontalChain(getChildAt(i*columnNum).getId(),ConstraintSet.LEFT,Math.min((i+1)*columnNum-1,count-1),ConstraintSet.RIGHT,ids,null,ConstraintWidget.CHAIN_PACKED);
            constraintSet.createHorizontalChain(ConstraintSet.PARENT_ID,ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,ids,null,ConstraintWidget.CHAIN_PACKED);
            constraintSet.connect(ConstraintSet.PARENT_ID,ConstraintSet.TOP,getChildAt(i*columnNum).getId(),ConstraintSet.TOP,verticalSpace);
        }


        post(new Runnable() {
            @Override
            public void run() {
                constraintSet.applyTo(CenterGridView.this);
            }
        });
    }


  /*  void populate(View view, int position, int count,boolean needAdd){
        LayoutParams destLp=null;
        if(!needAdd){
            destLp=(LayoutParams)view.getLayoutParams();
            destLp.width=childWidth;
            return;
        }
        view.setId(View.generateViewId());
        destLp=createLayoutParamter();
        //row [0,position/columnNum]
        int row=position/columnNum;
        //column [0,columnNum-1]
        int column=position%columnNum;
        boolean isLastInRow=columnNum==1||column==(count%columnNum)-1;
        if(column==0){
            //一排第一个
            destLp.leftToLeft=LayoutParams.PARENT_ID;
            destLp.horizontalChainStyle=LayoutParams.CHAIN_PACKED;
            if(row>0){
                destLp.topToBottom=getChildAt((row-1)*columnNum).getId();
                destLp.topMargin=verticalSpace;
            }else {
                destLp.topToTop=LayoutParams.PARENT_ID;
            }
            if(isLastInRow){
                //同时又是最后一个
                destLp.rightToRight=LayoutParams.PARENT_ID;
            }
        }else if(column==columnNum-1||position==count-1){
            //一排最后一个
            View pre=getChildAt(position-1);
            destLp.leftToRight=pre.getId();
            ((LayoutParams)pre.getLayoutParams()).rightToLeft=view.getId();
            destLp.rightToRight=LayoutParams.PARENT_ID;
            destLp.leftMargin=horizontalSpace;

            destLp.topToTop=getChildAt(position-column).getId();
        }else {
            //中间
            View pre=getChildAt(position-1);
            destLp.leftToRight=pre.getId();
            ((LayoutParams)pre.getLayoutParams()).rightToLeft=view.getId();
            destLp.leftMargin=horizontalSpace;

            destLp.topToTop=getChildAt(position-column).getId();
        }
        addViewInLayout(view,position,destLp);
    }*/


    ConstraintSet constraintSet;

    void populate(View view, int position, int count,boolean needAdd){
        LayoutParams destLp=null;
        view.setId(View.generateViewId());
        destLp=createLayoutParamter();
        //row [0,position/columnNum]
        int row=position/columnNum;
        //column [0,columnNum-1]
        int column=position%columnNum;
        boolean isLastInRow=columnNum==1||column==(count%columnNum)-1;
        if(column==0){
            //一排第一个
            if(row>0){
                destLp.topToBottom=getChildAt((row-1)*columnNum).getId();
                destLp.topMargin=verticalSpace;
            }else {
                destLp.topToTop=LayoutParams.PARENT_ID;
            }
            /*if(isLastInRow){
                //同时又是最后一个
                constraintSet.addToHorizontalChain(view.getId(),LayoutParams.PARENT_ID,LayoutParams.PARENT_ID);
            }else {
                constraintSet.addToHorizontalChain(view.getId(),LayoutParams.PARENT_ID,getChildAt(position+1).getId());
            }*/
        }else if(column==columnNum-1||position==count-1){
            //一排最后一个
            View pre=getChildAt(position-1);
//            constraintSet.addToHorizontalChain(view.getId(),pre.getId(),LayoutParams.PARENT_ID);

            destLp.leftMargin=horizontalSpace;

            destLp.topToTop=getChildAt(position-column).getId();
        }else {
            //中间
//            constraintSet.addToHorizontalChain(view.getId(),getChildAt(position-1).getId(),getChildAt(position+1).getId());
            destLp.leftMargin=horizontalSpace;
            destLp.topToTop=getChildAt(position-column).getId();
        }
        addView(view,position,destLp);

    }

    private LayoutParams createLayoutParamter(){
        LayoutParams lp=new LayoutParams(childWidth,ViewGroup.LayoutParams.WRAP_CONTENT);
        return lp;
    }


    public void setAdapter(BaseAdapter adapter){
        this.adapter=adapter;
        populate();
    }


}
