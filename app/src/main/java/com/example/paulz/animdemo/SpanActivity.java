package com.example.paulz.animdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paulz.animdemo.span.HyperLinkSpan;
import com.example.paulz.animdemo.span.MiddleImageSpan;

/**
 * Created by Paul Z on 2019/5/15.
 * Description:
 */
public class SpanActivity extends AppCompatActivity {
    EditText textView;
    EditText edittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);
        textView=findViewById(R.id.textview);
        edittext=findViewById(R.id.edittext);
        String content="[img]";
//        String content="很快哈斯卡来哎哎哎是啊哈佛哈家乐福哈了健身房就阿斯达健身卡的哭声[img]阿什顿卡上的哈斯辽阔的哈斯勒啊实打实大师的";

        textView.setText(handleSpan(content));


        edittext.setText(handleSpan(content));
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("paulz","paulz-----beforeTextChanged----s="+s+"---start="+start+"---count="+count+"---after="+after+"---Selection="+edittext.getSelectionStart());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_DONE){
                    appendText(edittext.getText());
                    return true;
                }
                return false;
            }
        });*/
    }

    private CharSequence handleSpan(String s){
        SpannableString ss=new SpannableString(s);
        int index=s.indexOf("[img]");
        ss.setSpan(new HyperLinkSpan(this,R.drawable.icon_sy_ss_tp,"查看图片"),index,index+5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new HyperLinkSpan(this,R.drawable.icon_friend_dislike,"查看图片"),index,index+5,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    private void appendText(CharSequence s) {
//        textView.append(s);
        textView.getEditableText().insert(textView.getSelectionStart(),s);
    }

    public void submit(View view){

        appendText(handleSpan(edittext.getText().toString()));
//        appendText(edittext.getText());

    }

}
