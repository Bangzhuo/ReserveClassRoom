package com.my.orderclassroom.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.tu.loadingdialog.LoadingDailog;
import com.my.orderclassroom.R;
import com.my.orderclassroom.utils.StatusBarUtil;
import com.my.orderclassroom.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity {


    protected Context mContext;


    private Unbinder unbinder;
    private LoadingDailog  mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        initWindow();
        initIntentValue();
        int layoutId = getLayoutId();
        if (layoutId > 0) {
            View contentView = LayoutInflater.from(mContext).inflate(layoutId, null, false);
            initWidget(contentView);
            setContentView(contentView);
            unbinder = ButterKnife.bind(this, contentView);
            initView();
        }
    }

    protected void initWidget(View contentView) {

    }




    protected void initIntentValue() {

    }


    public abstract void initView();

    public abstract int getLayoutId();

    public void initWindow() {
        setStatusBarTransparent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void showLongToast(String msg) {
        ToastUtils.showLong(mContext, msg);
    }

    protected void showShortToast(String msg) {
        ToastUtils.showShort(mContext, msg);
    }

    protected void showDialog() {
        if (mDialog == null) {
            LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                    .setMessage(getString(R.string.loading))
                    .setCancelable(true)
                    .setCancelOutside(true);
            mDialog=loadBuilder.create();
            mDialog.show();
        }else{
            mDialog.show();
        }
    }

    protected void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        hideDialog();
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }

    protected void setStatusBarTransparent() {
        StatusBarUtil.transparencyBar(this);

    }

}
