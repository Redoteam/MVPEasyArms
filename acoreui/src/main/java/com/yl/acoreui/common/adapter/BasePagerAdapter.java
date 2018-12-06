package com.yl.acoreui.common.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.yl.core.utils.LogUtils;
import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/8/1.
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {
    protected List<T> mData;
    protected SparseArray<SoftReference<View>> mViews;

    public BasePagerAdapter(List<T> data) {
        mData = data;
        mViews = new SparseArray<SoftReference<View>>(data.size());
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SoftReference<View> viewSoftReference = mViews.get(position);
        View view = null;
        if (viewSoftReference == null) {
            view = newView(position);
            mViews.put(position, new SoftReference<View>(view));
        } else {
            view = viewSoftReference.get();
            if (view == null) {
                view = newView(position);
                mViews.delete(position);
                mViews.put(position, new SoftReference<View>(view));
            }
        }
        if (container == null) {
            LogUtils.errorInfo("BasePagerAdapter", "container--->null");
        } else if (view == null) {
            LogUtils.errorInfo("BasePagerAdapter", "view--->null");
        }
        container.addView(view);
        return view;
    }

    public abstract View newView(int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position).get());
    }

    public T getItem(int position) {
        if (mData != null && mData.size() > 0) {
            return mData.get(position);
        } else {
            return null;
        }
    }
}