package com.yl.acoreui.common.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yl.core.base.mvp.IPresenter;
import com.yl.acoreui.R;
import com.yl.acoreui.app.IPermissionsCallBack;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 寻水的鱼 on 2018/8/28.
 */
public abstract class BaseFragment<P extends IPresenter> extends com.yl.core.base.fragment.BaseFragment<P> {
    private ZLoadingDialog dialog;
    protected ImageView toolBarIvMenu;
    protected ImageView toolBarIvBack;
    protected TextView toolBarTvTitle;
    private ViewGroup layoutContainer;
    protected P mPresenter;
    private Unbinder mUnbinder;
    protected View mRootView;
    private ViewGroup toolBar;
    RxPermissions rxPermissions;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = View.inflate(getActivity(), getLayoutId(savedInstanceState), null);
        toolBar = mRootView.findViewById(R.id.top_tool_bar);
        toolBarIvMenu = mRootView.findViewById(R.id.toolbar_iv_menu);
        toolBarIvBack = mRootView.findViewById(R.id.toolbar_iv_back);
        toolBarTvTitle = mRootView.findViewById(R.id.toolbar_tv_title);
        layoutContainer = mRootView.findViewById(R.id.layout_container);
        View inflate = View.inflate(getActivity(), getContainerLayout(savedInstanceState), null);
        layoutContainer.addView(inflate);
        if (toolBarIvBack != null) {
            toolBarIvBack.setOnClickListener(view -> onBackClicked());
        }

        if (toolBarIvMenu != null) {
            toolBarIvMenu.setOnClickListener(view -> onMenuClicked());
        }
        rxPermissions = new RxPermissions(this);
        mPresenter = initPresenter();
        mUnbinder = ButterKnife.bind(this, mRootView);
        initUI(savedInstanceState);
        initData(savedInstanceState);
        return mRootView;
    }


    @Override
    public int getLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_base_full;
    }


    public abstract @LayoutRes
    int getContainerLayout(@Nullable Bundle savedInstanceState);

    public void setToolBarTvTitle(String title) {
        toolBarTvTitle.setText(title);
    }

    public void isShowToolBar(boolean isShow) {
        toolBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void onBackClicked() {
        getActivity().finish();
    }

    public void onMenuClicked() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.with(this).pauseRequests();
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.BLACK)//颜色
                .setHintText("Loading...").setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.BLACK)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setCancelable(true)
//                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
    }

    @Override
    public void showLoading(String msg) {
        dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.BLACK)//颜色
                .setHintText(msg).setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.BLACK)// 设置字体颜色
                .setCancelable(true).setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
//                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showError(String code, String messaege) {
        Toast.makeText(getActivity(), messaege, Toast.LENGTH_SHORT).show();
    }

    public void gotoActivity(Class<?> cls) {
        startActivity(new Intent(getContext(), cls));
    }


    @SuppressLint("CheckResult")
    public void requestPermissions(@Nullable IPermissionsCallBack callBack, String... permissions) {
        rxPermissions.request(permissions).subscribe(granted -> {
            if (granted) { // Always true pre-M
                // I can control the camera now
                callBack.succeed();
            } else {
                // Oups permission denied
                callBack.onFailed();
            }
        });
    }
}
