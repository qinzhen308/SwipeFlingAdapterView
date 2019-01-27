package com.example.paulz.animdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.paulz.animdemo.centergridview.CenterGridView;

import java.util.ArrayList;
import java.util.List;

public class CenterGridViewActivity extends Activity {
    CenterGridView centerGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_gridview);
        initView();
    }


    private void initView(){
        centerGridView=findViewById(R.id.center_grid);
        CenterGridAdapter<String> adapter=new CenterGridAdapter<>();
        List<String> datas=new ArrayList<>();
        for(int i=0;i<13;i++){
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=View.inflate(parent.getContext(),R.layout.item_center_grid,null);
            TextView tv=convertView.findViewById(R.id.text);
            tv.setText("第"+position+"项");
            return convertView;
        }
    }
}
