package com.yl.acoreui.utils;

import android.view.View;

/**
 * Created by 寻水的鱼 on 2018/11/16.
 */
public class ClickProxy implements View.OnClickListener {

    private View.OnClickListener origin;
    private long lastclick = 0;
    private long timems = 1000; //ms
    private IAgain mIAgain;
    private InterceptClickListener interceptClickListener;

    public ClickProxy(View.OnClickListener origin, long timems, IAgain again) {
        this.origin = origin;
        this.mIAgain = again;
        this.timems = timems;
    }

    public ClickProxy(View.OnClickListener origin) {
        this.origin = origin;
    }

    public ClickProxy(View.OnClickListener origin, InterceptClickListener listener) {
        this.origin = origin;
        interceptClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - lastclick >= timems) {
            if (interceptClickListener == null) {
                origin.onClick(v);
                lastclick = System.currentTimeMillis();
            } else {
                if (!interceptClickListener.onInterceptClicked(v, origin)) {
                    origin.onClick(v);
                }
            }
            lastclick = System.currentTimeMillis();

        } else {
            if (mIAgain != null)
                mIAgain.onAgain();
        }
    }

    public interface IAgain {
        void onAgain();//重复点击
    }

    public interface InterceptClickListener {
        boolean onInterceptClicked(View v, View.OnClickListener origin);
    }
}