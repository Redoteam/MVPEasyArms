package com.yl.acoreui.common.adapter;

import android.support.annotation.NonNull;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by 寻水的鱼 on 2018/11/19.
 */
public interface BaseItemViewDelegate<T> extends ItemViewDelegate<T> {
    void onViewRecycled(@NonNull ViewHolder holder);
}
