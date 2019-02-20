package com.example.paulz.animdemo.centergridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.example.paulz.animdemo.R;

/**
 * Created by Paul Z on 2019/1/25.
 * Description:
 */
public class CenterGridView2 extends LinearLayout {
    private int columnNum;
    private int horizontalSpace;
    private int verticalSpace;
    private int childWidth;


    BaseAdapter adapter;

    public CenterGridView2(Context context) {
        super(context);
    }

    public CenterGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CenterGridView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        childWidth=(width-getPaddingLeft()-getPaddingRight()-horizontalSpace*(columnNum-1))/columnNum;
        if(adapter!=null&&adapter.getCount()!=0){
            int total=adapter.getCount();
            for(int i=0,size=getChildCount();i<size;i++){
                ViewGroup lineView=(ViewGroup)getChildAt(i);
                for(int j=0;j<lineView.getChildCount();j++){
                    populate(lineView.getChildAt(j),i*columnNum+j,total,false);
                }
            }
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

    }


    private void init(Context context, AttributeSet attrs, int style){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterGridView, style, 0);

        columnNum=a.getInt(R.styleable.CenterGridView_cgv_columnNum,1);
        horizontalSpace=a.getDimensionPixelSize(R.styleable.CenterGridView_cgv_horizontalSpace,0);
        verticalSpace=a.getDimensionPixelSize(R.styleable.CenterGridView_cgv_verticalSpace,0);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);
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

        for(int i=0;i<count;i++){
            populate(adapter.getView(i,null,this),i,count,true);
        }
    }


    void populate(View view, int position, int count,boolean needAdd){
        LayoutParams destLp=null;
        if(!needAdd){
            destLp=(LayoutParams)view.getLayoutParams();
            destLp.width=childWidth;
            return;
        }
        //row [0,position/columnNum]
        int row=position/columnNum;
        //column [0,columnNum-1]
        int column=position%columnNum;

        ViewGroup lineView=null;
        if(column==0){
            lineView=createEmptyLine(row);
            addView(lineView);
        }else {
            lineView=(ViewGroup) getChildAt(row);
        }
        destLp=createLayoutParamter();
        if(column!=0){
            destLp.leftMargin=horizontalSpace;
        }
        lineView.addView(view,destLp);

    }


    private LinearLayout createEmptyLine(int row){
        LinearLayout line=new LinearLayout(getContext());
        line.setLayoutParams(createLineLayoutParamter(row));
        return line;
    }

    private LayoutParams createLineLayoutParamter(int row){
        LayoutParams lp=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        if(row!=0){
            lp.topMargin=verticalSpace;
        }
        return lp;
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
