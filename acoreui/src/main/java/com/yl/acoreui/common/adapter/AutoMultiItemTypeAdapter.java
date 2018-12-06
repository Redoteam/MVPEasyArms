package com.yl.acoreui.common.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/2/23.
 * 使用AutoLayout框架下，recyclerview的adapter继承AutoAdapter才会进行适配
 */
public class AutoMultiItemTypeAdapter<T> extends MultiItemTypeAdapter<T> {

    public AutoMultiItemTypeAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = AutoViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }
}
