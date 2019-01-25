package com.example.paulz.animdemo.centergridview;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        init();
    }

    public CenterGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenterGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=View.MeasureSpec.getSize(childWidth);
        childWidth=(width-getPaddingLeft()-getPaddingRight()-horizontalSpace*(columnNum-1))/columnNum;
    }

    private void init(){

    }


    void populate(){
        int count=adapter.getCount();
        if(count==0){
            return;
        }
        for(int i=0;i<count;i++){
            populate(adapter.getView(i,null,this),i,count);
        }
    }

    void populate(View view,int position,int count){
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
        }else if(column==columnNum-1){
            //一排最后一个
            View pre=getChildAt(position-1);
            destLp.leftToRight=pre.getId();
            ((LayoutParams)pre.getLayoutParams()).rightToLeft=view.getId();
            destLp.rightToRight=LayoutParams.PARENT_ID;
            destLp.leftMargin=horizontalSpace;
        }else {
            //中间
            View pre=getChildAt(position-1);
            destLp.leftToRight=pre.getId();
            ((LayoutParams)pre.getLayoutParams()).rightToLeft=view.getId();
            destLp.leftMargin=horizontalSpace;
        }

        addViewInLayout(view,position,destLp);
    }

    private LayoutParams createLayoutParamter(){
        LayoutParams lp=new LayoutParams(childWidth,ViewGroup.LayoutParams.WRAP_CONTENT);
        return lp;
    }
}
