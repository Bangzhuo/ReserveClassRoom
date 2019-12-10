package com.my.orderclassroom.http;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpHelper {
    private static HttpHelper httpHelper;

    private HttpHelper() {
    }

    public static HttpHelper getInstance() {
        if (httpHelper == null) {
            httpHelper = new HttpHelper();
        }
        return httpHelper;
    }

    public void add(Context context, MyRequest myRequest) {
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(myRequest);
    }



}
