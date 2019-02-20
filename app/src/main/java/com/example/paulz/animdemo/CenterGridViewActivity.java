package com.example.paulz.animdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.paulz.animdemo.centergridview.CenterGridView;
import com.example.paulz.animdemo.centergridview.CenterGridView2;

import java.util.ArrayList;
import java.util.List;

public class CenterGridViewActivity extends Activity {
    CenterGridView2 centerGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_gridview);
        centerGridView=findViewById(R.id.center_grid);
        initView();

        centerGridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                initView2();
            }
        },2000);

        centerGridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                initView3();
            }
        },10000);
    }


    private void initView(){
        CenterGridAdapter<String> adapter=new CenterGridAdapter<>();
        List<String> datas=new ArrayList<>();
        for(int i=0;i<9;i++){
            datas.add("heheheh");
        }
        adapter.setList(datas);
        centerGridView.setAdapter(adapter);
    }

    private void initView2(){
        CenterGridAdapter<String> adapter=new CenterGridAdapter<>();
        List<String> datas=new ArrayList<>();
        for(int i=0;i<10;i++){
            datas.add("heheheh");
        }
        adapter.setList(datas);
        centerGridView.setAdapter(adapter);
    }

    private void initView3(){
        CenterGridAdapter<String> adapter=new CenterGridAdapter<>();
        List<String> datas=new ArrayList<>();
        for(int i=0;i<14;i++){
            datas.add("heheheh");
        }
        adapter.setList(datas);
        centerGridView.setAdapter(adapter);
    }


    class CenterGridAdapter<T> extends BaseAdapter{
        List<T> mList;

        public void setList(List<T> list){
            mList=list;
        }

        @Override
        public int getCount() {
            return mList==null?0:mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList!=null?mList.get(position):null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView=View.inflate(parent.getContext(),R.layout.item_center_grid,null);
            TextView tv=convertView.findViewById(R.id.text);
            tv.setText("第"+position+"项");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view =centerGridView.getChildAt(position);
                    ConstraintLayout.LayoutParams lp=(ConstraintLayout.LayoutParams)view.getLayoutParams();
                    Log.d("paulz","leftToLeft="+lp.leftToLeft+"---rightToRight="+lp.rightToRight+"---topToTop="+lp.topToTop+"---bottomToBottom="+lp.bottomToBottom+
                            "\nleftToRight="+lp.leftToRight+"---rightToLeft="+lp.rightToLeft+"---topToBottom="+lp.topToBottom+"---bottomToTop="+lp.bottomToTop);
                }
            });
            return convertView;
        }
    }
}
