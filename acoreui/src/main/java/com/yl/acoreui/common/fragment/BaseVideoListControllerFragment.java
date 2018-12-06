package com.yl.acoreui.common.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yl.acoreui.R;
import com.yl.acoreui.common.adapter.IVideoControllerDelegate;
import com.yl.acoreui.common.contract.ListContract;


/**
 * Created by 寻水的鱼 on 2018/12/4.
 * 配合{@link IVideoControllerDelegate}
 */
public abstract class BaseVideoListControllerFragment<P extends ListContract.AListPresenter,A extends RecyclerView.Adapter> extends BaseListFragment<P,A> {
    protected GSYVideoHelper smallVideoHelper;

    protected GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    int lastVisibleItem;

    int firstVisibleItem;

    ViewGroup videoFullContainer;

    @Override
    public int getLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.layout_base_video_list_controller;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        videoFullContainer = getFullContainer();
        initGSYSmallVideoHelperBuilder();
        initGSYVideoHelper(getGSYVideoPlayer(),gsySmallVideoHelperBuilder);
        super.initUI(savedInstanceState);
        mRecyclerView.addOnScrollListener(getRvOnScrollListener());
    }

    public ViewGroup getFullContainer(){
        FrameLayout videoFullContainer = mRootView.findViewById(R.id.video_full_container);
        return videoFullContainer;
    }


    public RecyclerView.OnScrollListener getRvOnScrollListener(){
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                BaseVideoListControllerFragment.this.onScrollStateChanged(recyclerView,newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                BaseVideoListControllerFragment.this.onScrolled(recyclerView,dx,dy);
            }
        };
    }

    protected void onScrollStateChanged(RecyclerView recyclerView, int newState){

    }

    protected void onScrolled(RecyclerView recyclerView, int dx, int dy){
        firstVisibleItem   = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        lastVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
        //大于0说明有播放,//对应的播放列表TAG
        if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(IVideoControllerDelegate.TAG)) {
            //当前播放的位置
            int position = smallVideoHelper.getPlayPosition();
            //不可视的是时候
            if ((position < firstVisibleItem || position > lastVisibleItem)) {
                //如果是小窗口就不需要处理
                if (!smallVideoHelper.isSmall() && !smallVideoHelper.isFull()) {
                    //小窗口
                    int size = CommonUtil.dip2px(requireContext(), 160);
                    int hSize = CommonUtil.dip2px(requireContext(), 90);
                    //actionbar为true才不会掉下面去
                    smallVideoHelper.showSmallVideo(new Point(size, hSize), true, true);
                }
            } else {
                if (smallVideoHelper.isSmall()) {
                    smallVideoHelper.smallVideoToNormal();
                }
            }
        }
    }

    protected  void initGSYVideoHelper(StandardGSYVideoPlayer gsyVideoPlayer,GSYVideoHelper.GSYVideoHelperBuilder videoHelperBuilder){
        smallVideoHelper = new GSYVideoHelper(requireContext(),gsyVideoPlayer);
        smallVideoHelper.setFullViewContainer(videoFullContainer);
        smallVideoHelper.setGsyVideoOptionBuilder(videoHelperBuilder);
    }

    protected StandardGSYVideoPlayer getGSYVideoPlayer(){
        return new NormalGSYVideoPlayer(requireContext());
    }

    protected void initGSYSmallVideoHelperBuilder(){
        //配置
        gsySmallVideoHelperBuilder = new GSYVideoHelper.GSYVideoHelperBuilder();
        gsySmallVideoHelperBuilder
                .setHideActionBar(true)
                .setHideStatusBar(true)
                .setNeedLockFull(true)
                .setCacheWithPlay(true)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(true)
                .setLockLand(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        Debuger.printfLog("Duration " + smallVideoHelper.getGsyVideoPlayer().getDuration() + " CurrentPosition " + smallVideoHelper.getGsyVideoPlayer().getCurrentPositionWhenPlaying());
                    }

                    @Override
                    public void onQuitSmallWidget(String url, Object... objects) {
                        super.onQuitSmallWidget(url, objects);
                        BaseVideoListControllerFragment.this.onQuitSmallWidget();
                    }
                });
    }

    public void onQuitSmallWidget(){
        //大于0说明有播放,//对应的播放列表TAG
        if (smallVideoHelper.getPlayPosition() >= 0 && smallVideoHelper.getPlayTAG().equals(IVideoControllerDelegate.TAG)) {
            //当前播放的位置
            int position = smallVideoHelper.getPlayPosition();
            //不可视的是时候
            if ((position < firstVisibleItem || position > lastVisibleItem)) {
                //释放掉视频
                smallVideoHelper.releaseVideoPlayer();
                mMultiItemTypeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        mRecyclerView.clearOnScrollListeners();
        super.onDestroy();
    }
}
