package com.yl.acoreui.common.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/9/4.
 */
public abstract class SimpleStateViewPagerAdapter<D> extends FragmentStatePagerAdapter {
    protected List<D> mData = new ArrayList<>();

    public SimpleStateViewPagerAdapter(FragmentManager fm, List<D> data) {
        super(fm);
        mData.clear();
        mData.addAll(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
