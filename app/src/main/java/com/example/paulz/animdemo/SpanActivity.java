package com.example.paulz.animdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.paulz.animdemo.span.HyperLinkSpan;
import com.example.paulz.animdemo.span.MiddleImageSpan;

/**
 * Created by Paul Z on 2019/5/15.
 * Description:
 */
public class SpanActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        textView=findViewById(R.id.textview);
        String content="[img]";
//        String content="很快哈斯卡来哎哎哎是啊哈佛哈家乐福哈了健身房就阿斯达健身卡的哭声[img]阿什顿卡上的哈斯辽阔的哈斯勒啊实打实大师的";
        SpannableString ss=new SpannableString(content);
        int index=content.indexOf("[img]");
        ss.setSpan(new HyperLinkSpan(this,R.drawable.icon_sy_ss_tp,"查看图片"),index,index+5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new HyperLinkSpan(this,R.drawable.icon_friend_dislike,"查看图片"),index,index+5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
    }
}
