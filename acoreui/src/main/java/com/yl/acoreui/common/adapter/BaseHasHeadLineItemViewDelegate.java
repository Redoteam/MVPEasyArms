package com.yl.acoreui.common.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by 寻水的鱼 on 2018/12/5.
 */
public interface BaseHasHeadLineItemViewDelegate<T> extends BaseItemViewDelegate<T>{
    View getHeaderLine(@NonNull ViewHolder holder);
}
