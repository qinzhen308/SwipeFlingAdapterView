package com.example.paulz.animdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paulz.animdemo.overlap.OverlapLinearLayoutManager;
import com.example.paulz.animdemo.swipecard.SwipeFlingAdapterView;
import com.example.paulz.animdemo.swipecard.UndoManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView v;
    SwipeFlingAdapterView flingAdapterView;
    private ArrayList<CardBean> datas=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v=findViewById(R.id.image);
        flingAdapterView=findViewById(R.id.frame);
        datas.add(new CardBean("第一个", OverlapLinearLayoutManager.OverlapMode.FIRST_BELOW_LAST));
        datas.add(new CardBean("第二个", OverlapLinearLayoutManager.OverlapMode.FIRST_ABOVE_LAST));
        datas.add(new CardBean("第三个", OverlapLinearLayoutManager.OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_FRONT));
        datas.add(new CardBean("第四个", OverlapLinearLayoutManager.OverlapMode.MIDDLE_ABOVE_OTHERS_INCLINED_BEHIND));
        datas.add(new CardBean("第五个", OverlapLinearLayoutManager.OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_FRONT));
        datas.add(new CardBean("第六个", OverlapLinearLayoutManager.OverlapMode.MIDDLE_BELOW_OTHERS_INCLINED_BEHIND));
        flingAdapterView.setUndoListener(new UndoManager.UndoLinstener() {
            @Override
            public void undo(Object data) {
                datas.add(0,(CardBean) data);
            }

            @Override
            public void onStackChanged(int stackSize) {

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
        flingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                if(itemPosition==0){
                    startActivity(new Intent(MainActivity.this ,CenterGridViewActivity.class));
                }
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
        RecyclerView recyclerview;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_card,parent,false);
                holder.textView=convertView.findViewById(R.id.tv_page);
                holder.recyclerview=convertView.findViewById(R.id.recyclerview);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }

            CardBean cardBean=(CardBean) getItem(position);
            OverlapLinearLayoutManager manager=new OverlapLinearLayoutManager(OverlapLinearLayoutManager.VERTICAL,
                    cardBean.overlapMode);
            manager.setGravity(Gravity.TOP);
            holder.recyclerview.setLayoutManager(manager);
            holder.textView.setText(cardBean.title);
            holder.recyclerview.setAdapter(overlapAdapter);
            return convertView;
        }
    };


    RecyclerView.Adapter overlapAdapter=new RecyclerView.Adapter<CircleViewHolder>() {
        @Override
        public int getItemCount() {
            return 6;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(CircleViewHolder holder, int position) {
            if(position==1){
                holder.imageView2.setVisibility(View.VISIBLE);
            }else {
                holder.imageView2.setVisibility(View.GONE);
            }
        }

        @Override
        public CircleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_circle,parent,false);
            return new CircleViewHolder(v);
        }

    };


    public static class CircleViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView imageView2;

        public CircleViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            imageView2=itemView.findViewById(R.id.image2);
        }
    }

    public static class CardBean{
        String title;
        OverlapLinearLayoutManager.OverlapMode overlapMode;
        public CardBean(String title,OverlapLinearLayoutManager.OverlapMode overlapMode){
            this.title=title;
            this.overlapMode=overlapMode;
        }
    }

}
