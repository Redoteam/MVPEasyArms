package com.yl.acoreui.common.adapter;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.utils.DeviceUtils;
import com.yl.acoreui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/10/29.
 */
public class BaseIndicatorViewPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    List<String> mTabNameList = new ArrayList<>();
    List<Fragment> mFragmentList = new ArrayList<>();

    public BaseIndicatorViewPagerAdapter(FragmentManager fragmentManager, List<String> tabNameList, List<Fragment> fragmentList) {
        super(fragmentManager);
        mTabNameList.clear();
        mTabNameList.addAll(tabNameList);

        mFragmentList.clear();
        mFragmentList.addAll(fragmentList);
        if (!equals(mTabNameList.size(), mFragmentList.size())) {
            throw new RuntimeException("tab list size is: " + mTabNameList.size() + ";but fragment list size is:" + mFragmentList.size());
        }
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(AppDelegate.getInstance().getmApplication());
            convertView = layoutInflater.inflate(R.layout.tab_top, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(mTabNameList.get(position));

        int witdh = getTextWidth(textView);
        float padding = DeviceUtils.dpToPixel(AppDelegate.getInstance().getmApplication(), 8);
        //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
        //1.3f是根据上面字体大小变化的倍数1.3f设置
        textView.setWidth((int) ((witdh * 1.3f) + padding));

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        return mFragmentList.get(position);
    }

    private boolean equals(int size1, int size2) {
        return size1 == size2;
    }

    private int getTextWidth(TextView textView) {
        if (textView == null) {
            return 0;
        }
        Rect bounds = new Rect();
        String text = textView.getText().toString();
        Paint paint = textView.getPaint();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }
}
