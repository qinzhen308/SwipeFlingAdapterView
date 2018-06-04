package com.example.paulz.animdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paulz.animdemo.swipecard.SwipeFlingAdapterView;
import com.example.paulz.animdemo.swipecard.UndoManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView v;
    SwipeFlingAdapterView flingAdapterView;
    private ArrayList<String> datas=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v=findViewById(R.id.image);
        flingAdapterView=findViewById(R.id.frame);
        datas.add("第一个");
        datas.add("第二个");
        datas.add("第三个");
        datas.add("第四个");
        datas.add("第五个");
        datas.add("第六个");
        flingAdapterView.setUndoListener(new UndoManager.UndoLinstener() {
            @Override
            public void undo(Object data) {
                datas.add(0,(String) data);
            }
        });
        flingAdapterView.setAdapter(cardAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                datas.remove(0);
                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                nope(MainActivity.this,v);
                flingAdapterView.getTopCardListener().swipeLeft();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                nope(MainActivity.this,v);
                flingAdapterView.getTopCardListener().swipeRight();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingAdapterView.undo();
            }
        });
    }

    /**
     * 按钮抖动动画
     *
     * @param view
     * @return
     */
    public static Animation nope(Context mContext, View view) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.water_drop);
        view.startAnimation(animation);
        return animation;
    }

    private static class ViewHolder{
        TextView textView;
    }

    BaseAdapter cardAdapter=new BaseAdapter() {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_card,parent,false);
                holder.textView=convertView.findViewById(R.id.tv_page);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            holder.textView.setText(getItem(position).toString());
            return convertView;
        }
    };


}
