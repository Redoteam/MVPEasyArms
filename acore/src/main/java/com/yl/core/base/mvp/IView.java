package com.yl.core.base.mvp;

import android.app.Activity;
import android.content.Context;

/**
 * Created by 寻水的鱼 on 2018/8/1.
 */
public interface IView {
    Activity getActivity();

    Context requireContext();

    void showError(String code, String messaege);

    void showLoading();

    void showLoading(String msg);

    void hideLoading();
}
