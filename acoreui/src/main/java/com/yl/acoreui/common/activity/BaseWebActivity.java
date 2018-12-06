package com.yl.acoreui.common.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yl.core.base.mvp.IPresenter;
import com.yl.acoreui.R;
import com.yl.acoreui.app.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/8/14.
 */
public abstract class BaseWebActivity<P extends IPresenter> extends BaseFullActivity<P> {
    protected WebView mWebView;

    @Override
    public int getContainerLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_base_web;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        mWebView = findViewById(R.id.web);
        initWebSetting();
    }


    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    protected void initWebSetting() {
        mWebView.setClickable(false);
        mWebView.setFocusable(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.addJavascriptInterface(BaseWebActivity.this, "imagelistener");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setBlockNetworkImage(false);
        settings.setPluginState(WebSettings.PluginState.ON);
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img, String[] imageUrls, int position) {
        if (imageUrls == null) {
            return;
        }
        if (position >= imageUrls.length) {
            position = 0;
        }
        List<String> list = new ArrayList<>();
        Collections.addAll(list, imageUrls);
        PreviewImageActivity.gotoPreviewImageActivity(this, list, null, position);
    }

    protected void setHtml(String content) {
        mWebView.loadDataWithBaseURL(null, Constant.HTML_START_WITH_CLICK + content + Constant.HTML_END, "text/html", "utf-8", null);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeAllViews();
                mWebView.removeAllViews();
                mWebView.destroy();
            }
        }
        super.onDestroy();
    }
}
