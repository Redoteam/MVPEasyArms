package com.yl.acoreui.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yl.acoreui.weight.recyclerview.decoration.StickyHeaderAdapter;
import com.yl.acoreui.weight.recyclerview.decoration.StickyHeaderDecoration;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/12/5.
 *  带悬浮头部的
 */
public abstract class BaseHasHeadMultiItemTypeAdapter<T, I extends BaseHasHeadLineItemViewDelegate<T>,VH extends RecyclerView.ViewHolder> extends BaseMultiItemTypeAdapter<T,I> implements StickyHeaderAdapter<VH> {
    public BaseHasHeadMultiItemTypeAdapter(Context context, List datas) {
        super(context, datas);
    }

  /*  @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
    }*/

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindHeadLineHolder(holder,position);
    }

    public boolean hasHeader(int position){
        return getHeaderId(position)!= StickyHeaderDecoration.NO_HEADER_ID;
    }

    /***
     * 当前Item是否显示头部分割线
     * @param holder
     * @param position
     */
    public void onBindHeadLineHolder(ViewHolder holder, int position){
        View item_header_line =  getItemViewDelegate(position).getHeaderLine(holder);
        if(item_header_line != null){
            if(hasHeader(position+1)){
                long nextHeaderId = getHeaderId(position + 1);
                if(hasHeader(position)){
                    long headerId = getHeaderId(position);
                    if(headerId != nextHeaderId){
                        item_header_line.setVisibility(View.VISIBLE);
                    }else {
                        item_header_line.setVisibility(View.GONE);
                    }
                }else {
                    item_header_line.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
