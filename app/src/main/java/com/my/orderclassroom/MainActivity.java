package com.my.orderclassroom;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.my.orderclassroom.adapter.MyListAdapter;
import com.my.orderclassroom.base.BaseActivity;
import com.my.orderclassroom.base.MyApplication;
import com.my.orderclassroom.entity.OrderInfoEntity;
import com.my.orderclassroom.http.ApiHelper;
import com.my.orderclassroom.http.BaseResponse;
import com.my.orderclassroom.http.HttpHelper;
import com.my.orderclassroom.http.MyRequest;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srf_list)
    SmartRefreshLayout srfList;
    @BindView(R.id.tvActionTitle)
    TextView tvActionTitle;

    MyListAdapter adapter;
    List<OrderInfoEntity> list = new ArrayList<>();

    boolean isExit = false;
    @Override
    public void initView() {
        tvActionTitle.setText(getString(R.string.order_my));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new MyListAdapter(list, mContext);
        adapter.setItemOnClick(new MyListAdapter.OnMyClickListener() {
            @Override
            public void onItemclick(OrderInfoEntity entity) {
                Intent intent=new Intent(mContext,DeatilsActivity.class);
                intent.putExtra("id","");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        srfList.setEnableLoadMore(false);
        srfList.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                srfList.finishLoadMore();

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        showDialog();
        getData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @OnClick({R.id.recyclerView, R.id.ivAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.recyclerView:

                break;
            case R.id.ivAdd:
                startActivityForResult(new Intent(mContext, AddActivity.class),2);
                break;
        }
    }

    public void getData(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
        Map<String, String> params = new HashMap<>();
        params.put("studentNum", MyApplication.getStuNum());
        params.put("beginTime",sdf.format(new Date())+":00");

        Type type = new TypeToken<BaseResponse<List<OrderInfoEntity>>>() {
        }.getType();
        HttpHelper.getInstance().add(mContext, new MyRequest(Request.Method.POST, ApiHelper.getMyReserveClassPage(), params, type, new Response.Listener<BaseResponse<List<OrderInfoEntity>>>() {
            @Override
            public void onResponse(BaseResponse<List<OrderInfoEntity>> response) {
                hideDialog();
                if("200".equals(response.getCode())){
                    list.clear();
                    list=response.getData();
                    srfList.finishRefresh();
                    adapter = new MyListAdapter(list, mContext);
                    adapter.setItemOnClick(new MyListAdapter.OnMyClickListener() {
                        @Override
                        public void onItemclick(OrderInfoEntity entity) {
                            Intent intent=new Intent(mContext,DeatilsActivity.class);
                            intent.putExtra("entity", (Serializable) entity);
                            startActivityForResult(intent,2);
                        }
                    });
                    recyclerView.setAdapter(adapter);
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



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK){
            showDialog();
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
        } else {
            showShortToast(getString(R.string.logout));
            isExit = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    isExit = false;
                }
            }, 2500);
        }
    }
}
