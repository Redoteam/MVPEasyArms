package com.yl.acoreui.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/1/19.
 */
public class SimpleViewPagerAdapter extends FragmentPagerAdapter {
    private List<? extends Fragment> fragmentList;

    public SimpleViewPagerAdapter(FragmentManager fm, List<? extends Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
