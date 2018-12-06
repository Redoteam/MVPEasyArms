package com.yl.core.base.mvp;

import android.app.Activity;

/**
 * Created by 寻水的鱼 on 2018/8/5.
 */
public interface IPresenter {
    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 {@link Activity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    void onDestroy();
}
