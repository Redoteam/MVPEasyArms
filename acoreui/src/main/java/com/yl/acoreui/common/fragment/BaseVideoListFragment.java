package com.yl.acoreui.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.yl.acoreui.common.adapter.IVideoItemViewDelegate;
import com.yl.acoreui.common.contract.ListContract;

/**
 * Created by 寻水的鱼 on 2018/11/19.
 * 配合{@link IVideoItemViewDelegate}使用
 */
public abstract class BaseVideoListFragment<P extends ListContract.AListPresenter,A extends RecyclerView.Adapter> extends BaseListFragment<P,A> {

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onRecyclerViewScrolled(recyclerView,dx,dy);

                /*//大于0说明有播放,//对应的播放列表TAG
                if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(BaseVideoItemViewDelegate.TAG)) {
                    //当前播放的位置
                    int position = smallVideoHelper.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(requireContext(), 150);
                            //actionbar为true才不会掉下面去
                            smallVideoHelper.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (smallVideoHelper.isSmall()) {
                            smallVideoHelper.smallVideoToNormal();
                        }
                    }
                }*/
            }
        });
    }
    public void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        //大于0说明有播放
        if (GSYVideoManager.instance().getPlayPosition() >= 0) {
            //当前播放的位置
            int position = GSYVideoManager.instance().getPlayPosition();
            //对应的播放列表TAG
            if (GSYVideoManager.instance().getPlayTag().equals(IVideoItemViewDelegate.TAG) && (position < firstVisibleItem || position > lastVisibleItem)) {

                //如果滑出去了上面和下面就是否，和今日头条一样
                //是否全屏
                if (!GSYVideoManager.isFullState(getActivity())) {
                    GSYVideoManager.releaseAllVideos();
                    mMultiItemTypeAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    public boolean onBackPressed() {
        return  GSYVideoManager.backFromWindowFull(getActivity());
    }

    @Override
    public void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        super.onDestroy();
    }
}
