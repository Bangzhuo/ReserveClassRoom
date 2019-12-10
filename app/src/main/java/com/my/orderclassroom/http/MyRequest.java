package com.my.orderclassroom.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;



public class MyRequest<T> extends Request<T> {
    private Map<String, String> params;
    private Response.Listener<T> callback;
    private Type type;


    public MyRequest(int method, String url, Map<String, String> params, Type type, Response.Listener<T> callback, Response.ErrorListener listener) {
        super(Method.POST, url, listener);
        setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.e("url",url);
        Log.e("params",params.toString());
        this.params = params;
        this.callback = callback;
        this.type = type;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, "UTF-8");
            Log.e("callback",jsonString);
            return Response.success((T) parseJsonWithGson(jsonString, type),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));

        }
    }

    @Override
    protected void deliverResponse(T response) {
        callback.onResponse(response);
    }

    //将Json数据解析成相应的映射对象
    public <T> T parseJsonWithGson(String jsonData, Type type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }
}
