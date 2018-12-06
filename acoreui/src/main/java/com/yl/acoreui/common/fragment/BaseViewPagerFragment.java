package com.yl.acoreui.common.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yl.core.base.mvp.IPresenter;
import com.yl.acoreui.R;
import com.yl.acoreui.common.adapter.BaseIndicatorViewPagerAdapter;

import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/10/29.
 */
public abstract class BaseViewPagerFragment<P extends IPresenter> extends BaseFragment<P> {
    protected ScrollIndicatorView tabIndicator;
    ViewPager tabViewPager;

    private IndicatorViewPager indicatorViewPager;

    protected float unSelectSize = 12;
    protected float selectSize = unSelectSize * 1.3f;

    @Override
    public int getContainerLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_base_view_pager;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        tabIndicator = mRootView.findViewById(R.id.tab_indicator);
        tabViewPager = mRootView.findViewById(R.id.tab_viewPager);
        initTabIndicator();
        initTabIndicatorViewPager();
    }

    public void initTabIndicator() {
        tabIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));
        tabIndicator.setScrollBar(new ColorBar(getContext(), 0xFF2196F3, 4));
    }

    public void initTabIndicatorViewPager() {
        indicatorViewPager = new IndicatorViewPager(tabIndicator, tabViewPager);
    }

    public void createPagerAdapter(List<String> tabNameList, List<Fragment> fragmentList) {
        indicatorViewPager.setAdapter(new BaseIndicatorViewPagerAdapter(getChildFragmentManager(), tabNameList, fragmentList));
    }
}
