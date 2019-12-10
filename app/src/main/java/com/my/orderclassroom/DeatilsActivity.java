package com.my.orderclassroom;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.my.orderclassroom.base.BaseActivity;
import com.my.orderclassroom.entity.OrderInfoEntity;
import com.my.orderclassroom.http.ApiHelper;
import com.my.orderclassroom.http.BaseResponse;
import com.my.orderclassroom.http.HttpHelper;
import com.my.orderclassroom.http.MyRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class DeatilsActivity extends BaseActivity {

    @BindView(R.id.ivActionLeft)
    ImageView ivActionLeft;
    @BindView(R.id.tvActionTitle)
    TextView tvActionTitle;
    @BindView(R.id.btnCancle)
    Button btnCancle;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvStuNum)
    TextView tvStuNum;
    @BindView(R.id.tvPeoNum)
    TextView tvPeoNum;
    @BindView(R.id.tvContent)
    TextView tvContent;

    OrderInfoEntity entity;
    @Override
    public void initView() {
        entity= (OrderInfoEntity) getIntent().getSerializableExtra("entity");
        ivActionLeft.setVisibility(View.VISIBLE);
        tvActionTitle.setText(getString(R.string.order_details));
        tvContent.setText(entity.getNote());
        tvStuNum.setText(entity.getStudentNum());
        tvPeoNum.setText(entity.getPersonNum()+"");
        tvRoom.setText(entity.getAddress()+entity.getClassroomName());
        tvTime.setText(entity.getBeginTime().substring(0,16)+"-"+entity.getEndTime().substring(11,16));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_deatils;
    }


    @OnClick({R.id.ivActionLeft, R.id.btnCancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivActionLeft:
                finish();
                break;
            case R.id.btnCancle:
                showDialog();
                Map<String, String> params = new HashMap<>();
                params.put("ids", entity.getId());
                params.put("flag", "2");

                Type type = new TypeToken<BaseResponse<List<OrderInfoEntity>>>() {
                }.getType();
                HttpHelper.getInstance().add(mContext, new MyRequest(Request.Method.POST, ApiHelper.addReserveClass(), params, type, new Response.Listener<BaseResponse<List<OrderInfoEntity>>>() {
                    @Override
                    public void onResponse(BaseResponse<List<OrderInfoEntity>> response) {
                        hideDialog();
                        if("200".equals(response.getCode())){
                            showShortToast(getString(R.string.cancle_success));
                            setResult(RESULT_OK);
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
