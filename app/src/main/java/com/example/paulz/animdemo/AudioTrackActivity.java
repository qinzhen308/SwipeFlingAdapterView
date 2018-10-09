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

import com.paulz.audiotrack.AudioTrackTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul Z on 2018/10/9.
 * Description:
 */

public class AudioTrackActivity extends AppCompatActivity {
    AudioTrackTabLayout tabBar;
    ViewPager viewPager;
    String[] titles={"关注","推荐","附近","视频"};
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
        tabBar.setViewPager(viewPager);
        setTabsValue();
    }


    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabBar.setShouldExpand(false);

        // 设置Tab的分割线的颜色
        // tabBar.setDividerColor(getResources().getColor(R.color.color_80cbc4));
        // 设置分割线的上下的间距,传入的是dp
        tabBar.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        tabBar.setUnderlineHeight(0);
        //设置Tab底部线的颜色
        tabBar.setUnderlineColor(Color.GREEN);

        // 设置Tab 指示器Indicator的高度,传入的是dp
        tabBar.setIndicatorHeight(10);
        tabBar.setTabPaddingLeftRight(10);
        // 设置Tab Indicator的颜色
        tabBar.setIndicatorColorResource(R.color.colorAccent);

        // 设置Tab标题文字的大小,传入的是dp
        tabBar.setTextSize(16);
        // 设置选中Tab文字的颜色
        tabBar.setSelectedTextColorResource(R.color.colorAccent);
        //设置正常Tab文字的颜色
        tabBar.setTextColorResource(R.color.colorPrimaryDark);
    }
}
