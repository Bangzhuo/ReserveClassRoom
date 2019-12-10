package com.my.orderclassroom;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.reflect.TypeToken;
import com.my.orderclassroom.base.BaseActivity;
import com.my.orderclassroom.base.MyApplication;
import com.my.orderclassroom.entity.ClassRoomEntity;
import com.my.orderclassroom.http.ApiHelper;
import com.my.orderclassroom.http.BaseResponse;
import com.my.orderclassroom.http.HttpHelper;
import com.my.orderclassroom.http.MyRequest;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddActivity extends BaseActivity {

    @BindView(R.id.ivActionLeft)
    ImageView ivActionLeft;
    @BindView(R.id.tvActionTitle)
    TextView tvActionTitle;
    @BindView(R.id.tvRoom)
    TextView tvRoom;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.etStuNum)
    EditText etStuNum;
    @BindView(R.id.etPeoNum)
    EditText etPeoNum;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.btnAdd)
    Button btnAdd;
    Date myStartDate;
    String classroomName;
    String address;
    List<String> listRoom=new ArrayList();
    List<ClassRoomEntity> list=new ArrayList();

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
    @Override
    public void initView() {
        ivActionLeft.setVisibility(View.VISIBLE);
        tvActionTitle.setText(getString(R.string.order_new));

        etStuNum.setText(MyApplication.getStuNum());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add;
    }



    @OnClick({R.id.ivActionLeft,R.id.tvRoom, R.id.tvStartTime, R.id.tvEndTime, R.id.btnAdd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivActionLeft:
                finish();
                break;
            case R.id.tvRoom:
                if(listRoom.size()==0){
                    Map<String, String> paramsRoom = new HashMap<>();
                    Type type1 = new TypeToken<BaseResponse<List<ClassRoomEntity>>>() {
                    }.getType();
                    HttpHelper.getInstance().add(mContext, new MyRequest(Request.Method.POST, ApiHelper.getClassroomPage(), paramsRoom, type1, new Response.Listener<BaseResponse<List<ClassRoomEntity>>>() {
                        @Override
                        public void onResponse(BaseResponse<List<ClassRoomEntity>> response) {
                            if("200".equals(response.getCode())){
                                list=response.getData();
                                for(int i=0;i<response.getData().size();i++){
                                    listRoom.add(response.getData().get(i).getAddress()+response.getData().get(i).getName());
                                }
                                ShowRoomName();
                            }else{
                                showShortToast(response.getMessage());
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("http", error.toString());
                            showShortToast(error.toString());
                        }
                    }));
                }else{
                    ShowRoomName();
                }


                break;
            case R.id.tvStartTime:
                showStartTimeDate(tvStartTime);
                break;
            case R.id.tvEndTime:
                if(myStartDate!=null){

                    showEndTimeDate(tvEndTime);
                }else{
                    showShortToast(getString(R.string.sel_start_time));
                }
                break;
            case R.id.btnAdd:
                if(checkInfo()){
                    showDialog();
                    Map<String, String> params = new HashMap<>();
                    params.put("studentNum", etStuNum.getText().toString());
                    params.put("address", address);
                    params.put("classroomName", classroomName);
                    params.put("personNum", etPeoNum.getText().toString());
                    params.put("note", etContent.getText().toString());
                    params.put("beginTime", tvStartTime.getText().toString());
                    params.put("endTime", tvEndTime.getText().toString());

                    Type type = new TypeToken<BaseResponse>() {
                    }.getType();
                    HttpHelper.getInstance().add(mContext, new MyRequest(Request.Method.POST, ApiHelper.addReserveClass(), params, type, new Response.Listener<BaseResponse>() {
                        @Override
                        public void onResponse(BaseResponse response) {
                            hideDialog();
                            if("200".equals(response.getCode())){
                                showShortToast(getString(R.string.order_success));
                                setResult(RESULT_OK);
                                finish();
                            }else{
                                showLongToast(response.getMessage());
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


                break;
        }
    }

    private boolean checkInfo() {
        if(TextUtils.isEmpty(tvRoom.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(tvStartTime.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(tvEndTime.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(etStuNum.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(etPeoNum.getText().toString())){
            return false;
        }
        if(TextUtils.isEmpty(etContent.getText().toString())){
            return false;
        }
        return true;
    }

    public void showEndTimeDate(TextView textView){
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(myStartDate);
        TimePickerView pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time=sdf.format(date);
                if(Integer.parseInt(time.substring(time.length()-2,time.length()))<8){
                    showShortToast(getString(R.string.sel_time_8));
                }else if(Integer.parseInt(time.substring(time.length()-2,time.length()))>22){
                    showShortToast(getString(R.string.sel_time_22));
                }else if(date.after(myStartDate)){
                    textView.setText(sdf.format(date)+":00");

                }else {
                    showShortToast(getString(R.string.before_start_time));
                }
            }
        })

                .setType(new boolean[]{true, true, true, true, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(getResources().getColor(R.color.text_bule))
                .setTextColorCenter(getResources().getColor(R.color.text_bule))
                .setTextColorOut(getResources().getColor(R.color.text_black))
                .setContentSize(21)
                .setRangDate(startDate, startDate)
                .setLineSpacingMultiplier(1.5f)
                .setTextXOffset(-10, 0, 10, 0, 0, 0)
                .setDecorView(null)
                .setSubmitText("sure")
                .setCancelText("cancle")
                .build();
        pvTime1.show();
    }
    public void showStartTimeDate(TextView textView){
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());
        endDate.add(Calendar.DATE, 2);
        TimePickerView pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time=sdf.format(date);
                if(date.before(new Date())){
                    showShortToast(getString(R.string.no_sel_time));
                }else
                if(Integer.parseInt(time.substring(time.length()-2,time.length()))<8){
                    showShortToast(getString(R.string.sel_time_8));
                }else if(Integer.parseInt(time.substring(time.length()-2,time.length()))>22){
                    showShortToast(getString(R.string.sel_time_22));
                }else{
                    myStartDate=date;
                    textView.setText(sdf.format(date)+":00");
                    tvEndTime.setText("");
                }
            }
        })

                .setType(new boolean[]{true, true, true, true, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(getResources().getColor(R.color.text_bule))
                .setTextColorCenter(getResources().getColor(R.color.text_bule))
                .setTextColorOut(getResources().getColor(R.color.text_black))
                .setContentSize(20)
                .setRangDate(startDate, endDate)
                .setLineSpacingMultiplier(1.5f)
                .setTextXOffset(-10, 0, 10, 0, 0, 0)
                .setDecorView(null)
                .setSubmitText("sure")
                .setCancelText("cancle")
                .build();
        pvTime1.show();
    }

        private void ShowRoomName() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                address = list.get(options1).getAddress();
                classroomName = list.get(options1).getName();
                tvRoom.setText(listRoom.get(options1));

            }
        })
                .setDividerColor(getResources().getColor(R.color.text_bule))
                .setTextColorCenter(getResources().getColor(R.color.text_bule))
                .setTextColorOut(getResources().getColor(R.color.text_black))
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .setSubmitText("sure")
                .setCancelText("cancle")
                .build();


        pvOptions.setPicker(listRoom);

        pvOptions.show();
    }
}
