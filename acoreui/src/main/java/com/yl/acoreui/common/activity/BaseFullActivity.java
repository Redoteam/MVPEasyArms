package com.yl.acoreui.common.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yl.acoreui.R;
import com.yl.acoreui.app.IPermissionsCallBack;
import com.yl.core.base.mvp.IPresenter;
import com.yl.core.base.mvp.IView;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.delegate.IActivity;
import com.yl.core.integration.AppManager;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 寻水的鱼 on 2018/8/15.
 */
public abstract class BaseFullActivity<P extends IPresenter> extends AppCompatActivity implements IView, IActivity {
    protected P mPresenter;
    private Unbinder mUnbinder;
    private ZLoadingDialog dialog;
    protected ImageView toolBarIvMenu;
    protected ImageView toolBarIvBack;
    protected TextView toolBarTvTitle;
    protected ViewGroup layoutContainer;
    private ViewGroup toolBar;
    private View toolBartoolDevideLine;
    final RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        try {
            int layoutResID = getLayoutId(savedInstanceState);
            if (layoutResID != 0) {
                setContentView(layoutResID);
                toolBar = findViewById(R.id.top_tool_bar);
                toolBarIvMenu = findViewById(R.id.toolbar_iv_menu);
                toolBarIvBack = findViewById(R.id.toolbar_iv_back);
                toolBarTvTitle = findViewById(R.id.toolbar_tv_title);
                toolBartoolDevideLine = findViewById(R.id.devide_line);
                layoutContainer = findViewById(R.id.layout_container);
                View.inflate(getActivity(), getContainerLayout(savedInstanceState), layoutContainer);
                if (toolBarIvBack != null) {
                    toolBarIvBack.setOnClickListener(view -> onBackClicked());
                }

                if (toolBarIvMenu != null) {
                    toolBarIvMenu.setOnClickListener(view -> onMenuClicked());
                }
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            if (e instanceof InflateException)
                throw e;
            e.printStackTrace();
        }
        mPresenter = initPresenter();
        initUI(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }


    @Override
    public int getLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_base_full;
    }

    public abstract int getContainerLayout(@Nullable Bundle savedInstanceState);

    public void setToolBarTvTitle(String title) {
        toolBarTvTitle.setText(title);
    }

    public void isShowToolBar(boolean isShow) {
        toolBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void onBackClicked() {
        finish();
    }

    public void onMenuClicked() {

    }

    public float getDimension(int id) {
        return getResources().getDimension(id);
    }

    public void gotoActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    public void showLoading() {
        if (dialog == null) {
            dialog = new ZLoadingDialog(this);
            dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                    .setLoadingColor(Color.BLACK)//颜色
                    .setHintText("Loading...").setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.BLACK)  // 设置字体颜色
                    .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                   //.setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                    .show();
        } else {
            dialog.setHintText("Loading...").show();
        }

    }

    @Override
    public void showLoading(String msg) {
        if (dialog == null) {
            dialog = new ZLoadingDialog(this);
            dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                    .setLoadingColor(Color.BLACK)//颜色
                    .setHintText(msg).setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.BLACK)  // 设置字体颜色
                    .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
//                .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                    .show();
        } else {
            dialog.setHintText(msg).show();
        }
    }


    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void onStop() {
        super.onStop();
        Glide.with(getActivity()).pauseRequests();
    }

    @Override
    public abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context requireContext()
    {
        return getActivity();
    }

    @Override
    public void showError(String code, String messaege) {
        Toast.makeText(this, messaege, Toast.LENGTH_SHORT).show();
    }

    public void toast(String msg) {
        Toast.makeText(AppDelegate.getInstance().getmApplication(), msg, Toast.LENGTH_SHORT).show();
    }


    public @ColorInt
    int getColorInt(@ColorRes int colorId) {
        return ContextCompat.getColor(this, colorId);
    }

    public void setToolBarDevideLineVisibility(int visibility) {
        toolBartoolDevideLine.setVisibility(visibility);
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

    public void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }


    /***
     * 重写此方法，禁止字体随系统大小改变
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (resources != null && resources.getConfiguration().fontScale != 1.0f) {
            android.content.res.Configuration configuration = resources.getConfiguration();
            configuration.fontScale = 1.0f;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

}
