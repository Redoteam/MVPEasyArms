package com.yl.core.base.mvp;

import android.app.Activity;

/**
 * Created by 寻水的鱼 on 2018/8/1.
 */
public interface IView {
    Activity getActivity();

    void showError(String code, String messaege);

    void showLoading();

    void showLoading(String msg);

    void hideLoading();
}
