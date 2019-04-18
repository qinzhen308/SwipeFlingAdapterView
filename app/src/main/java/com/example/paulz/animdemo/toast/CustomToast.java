package com.example.paulz.animdemo.toast;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paulz.animdemo.R;

/**
 * Created by Paul Z on 2019/3/12.
 * Description:
 */
public class CustomToast  {


    public static void showToast(Context context,String text,int type){
        Toast toast=new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_kcwwc, null);
        TextView tv=v.findViewById(R.id.message);
        tv.setText(text);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

}
