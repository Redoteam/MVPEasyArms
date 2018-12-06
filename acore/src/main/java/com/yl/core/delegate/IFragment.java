package com.yl.core.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yl.core.base.mvp.IPresenter;

/**
 * Created by 寻水的鱼 on 2018/8/27.
 */
public interface IFragment {
    int getLayoutId(@Nullable Bundle savedInstanceState);

    void initUI(@Nullable Bundle savedInstanceState);

    void initData(@Nullable Bundle savedInstanceState);

    IPresenter initPresenter();
}
