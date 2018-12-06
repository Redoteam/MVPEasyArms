package com.yl.acoreui.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by 寻水的鱼 on 2018/1/29.
 */
public class AutoViewHolder extends ViewHolder {
    public AutoViewHolder(Context context, View itemView) {
        super(context, itemView);
        AutoUtils.autoSize(itemView);
    }

    public static ViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new AutoViewHolder(context, itemView);
        return holder;
    }
}
