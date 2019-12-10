package com.my.orderclassroom.base;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Context mContext;

    private static MyApplication instance;
    private static String stuNum;

    public static MyApplication getInstance() {

        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;
    }

    public static String getStuNum() {
        return stuNum;
    }

    public static void setStuNum(String num) {
        stuNum = num;
    }
}
