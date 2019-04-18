package com.example.paulz.animdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.paulz.audiotrack.AudioTrackTabLayout;
import com.paulz.audiotrack.RightIndicator;
import com.paulz.audiotrack.ViewPagerInTabLayoutProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Paul Z on 2018/10/9.
 * Description:
 */

public class AudioTrackActivity extends AppCompatActivity {
    AudioTrackTabLayout tabBar;
    ViewPager viewPager;
//    String[] titles={"关注123123123123","推荐哈sss","附近2111222","视频1312312313","tab5","tab6","tab7","tab8","tab9","tab10","tab11"};
//    String[] titles={"关注","推荐哈","附近","视频","视频","视频","视频","视频","视频","视频","视频","视频","视频","视频","视频","视频end"};
    String[] titles={"关注","推荐哈","附近","视频","视频"};
    List<View> views=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_track);
        for(String title:titles){
            TextView v=new TextView(this);
            v.setText(title);
            v.setLayoutParams(new ViewPager.LayoutParams());
            v.setGravity(Gravity.CENTER);
            views.add(v);
        }
        tabBar=findViewById(R.id.tab_bar);
        viewPager=findViewById(R.id.content_container);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View v=views.get(position);
                container.addView(v);
                return v;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }
        });
        tabBar.setViewPager(new ViewPagerInTabLayoutProxy(viewPager));
        tabBar.addRightIndicator(new RightIndicator(0) {
            @Override
            public boolean OnTabExcessClick() {
                Toast.makeText(getApplicationContext(),"哈哈哈",Toast.LENGTH_LONG).show();
                titles[0]="关注"+(int)(Math.random()*100);
                viewPager.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
//        setTabsValue();
    }



}
