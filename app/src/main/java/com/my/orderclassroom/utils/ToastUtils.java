package com.my.orderclassroom.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {
    static Toast toast;

    public static  void showShort(Context context,String tip) {
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        toast.setText(tip);
        toast.show();
    }

    public static  void showLong(Context context, String tip) {
        if(toast!=null){
            toast.cancel();
        }
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        toast.setText(tip);
        toast.show();
    }
}
