package com.example.paulz.animdemo.centergridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=View.MeasureSpec.getSize(widthMeasureSpec);
        childWidth=(width-getPaddingLeft()-getPaddingRight()-horizontalSpace*(columnNum-1))/columnNum;
        for(int i=0,size=getChildCount();i<size;i++){
            populate(getChildAt(i),i,size,false);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init(Context context,AttributeSet attrs,int style){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterGridView, style, 0);

        columnNum=a.getInt(R.styleable.CenterGridView_cgv_columnNum,1);
        horizontalSpace=a.getDimensionPixelSize(R.styleable.CenterGridView_cgv_horizontalSpace,0);
        verticalSpace=a.getDimensionPixelSize(R.styleable.CenterGridView_cgv_verticalSpace,0);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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

        removeAllViewsInLayout();

        for(int i=0;i<count;i++){
            populate(adapter.getView(i,null,this),i,count,true);
        }

        requestLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void populate(View view, int position, int count,boolean needAdd){
        view.setId(View.generateViewId());
        LayoutParams destLp=createLayoutParamter();
        /*ViewGroup.LayoutParams lp=view.getLayoutParams();
        if(lp instanceof RelativeLayout.LayoutParams){
            RelativeLayout.LayoutParams relp=(RelativeLayout.LayoutParams)lp;
        }*/
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
        }else if(column==columnNum-1||column==(count%columnNum)-1){
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

        if(needAdd){
            addViewInLayout(view,position,destLp);
        }
    }

    private LayoutParams createLayoutParamter(){
        LayoutParams lp=new LayoutParams(childWidth,ViewGroup.LayoutParams.WRAP_CONTENT);
        return lp;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setAdapter(BaseAdapter adapter){
        this.adapter=adapter;
        populate();
        requestLayout();
    }


}
