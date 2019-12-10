package com.my.orderclassroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.my.orderclassroom.base.BaseActivity;
import com.my.orderclassroom.base.MyApplication;
import com.my.orderclassroom.http.ApiHelper;
import com.my.orderclassroom.http.BaseResponse;
import com.my.orderclassroom.http.HttpHelper;
import com.my.orderclassroom.http.MyRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    SharedPreferences sp;
    @Override
    public void initView() {
        sp = getSharedPreferences("user", 0);
        etUserName.setText(sp.getString("userName", ""));
        etPassword.setText(sp.getString("password", ""));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick({ R.id.btnLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if(!(etUserName.getText().toString()).equals(etPassword.getText().toString())){
                    showShortToast(getString(R.string.error_password));
                    break;
                }
                showDialog();
                Map<String, String> params = new HashMap<>();
                params.put("userName", etUserName.getText().toString());
                params.put("password", etPassword.getText().toString());
                
                Type type = new TypeToken<BaseResponse>() {
                }.getType();
                HttpHelper.getInstance().add(mContext, new MyRequest(Request.Method.POST, ApiHelper.userLogin(), params, type, new Response.Listener<BaseResponse>() {
                    @Override
                    public void onResponse(BaseResponse response) {
                        hideDialog();
                        if("200".equals(response.getCode())){
                            startActivity(new Intent(mContext, MainActivity.class));
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("userName", etUserName.getText().toString());
                            edit.putString("password", etPassword.getText().toString());
                            MyApplication.setStuNum(etUserName.getText().toString());
                            edit.commit();
                            finish();
                        }else{
                            showShortToast(response.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Log.e("http", error.toString());
                        showShortToast(error.toString());
                    }
                }));

                break;
        }
    }
}