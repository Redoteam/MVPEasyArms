package com.yl.acoreui.common.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.yl.core.base.mvp.IPresenter;
import com.yl.acoreui.R;


/**
 * Created by 寻水的鱼 on 2018/8/14.
 */
public abstract class BaseVideoActivity<P extends IPresenter> extends BaseFullActivity<P> implements VideoAllCallBack {
    public StandardGSYVideoPlayer gsyVideoPlayer;

    private OrientationUtils orientationUtils;

    protected String mVideoPath;
    protected String mVideoTitle;

    @Override
    public int getContainerLayout(@Nullable Bundle savedInstanceState) {

        return R.layout.activity_base_video;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        gsyVideoPlayer = findViewById(R.id.video_player);
        initVideoSetting();
    }

    public void initVideoSetting() {
        //注意，这是全局生效的
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, gsyVideoPlayer);
        orientationUtils.setRotateWithSystem(false);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        gsyVideoPlayer.setRotateViewAuto(true);
        gsyVideoPlayer.setLockLand(false);
        gsyVideoPlayer.setVideoAllCallBack(this);
    }


    public void playVideo(String playUrl, String title) {
        if (TextUtils.isEmpty(playUrl)) {
            return;
        }
        mVideoPath = playUrl;
        mVideoTitle = TextUtils.isEmpty(title) ? "" : title;
        gsyVideoPlayer.setUp(playUrl, false, mVideoTitle);
        gsyVideoPlayer.startPlayLogic();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        gsyVideoPlayer.onVideoReset();
        if (gsyVideoPlayer.getCurrentState() == GSYVideoPlayer.CURRENT_STATE_NORMAL) {
            playVideo(mVideoPath, mVideoTitle);
        }
    }

    @Override
    public void onDestroy() {
        GSYVideoManager.releaseAllVideos();
        orientationUtils.releaseListener();
        orientationUtils = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 进入全屏播放
     *
     * @param actionBar 是否有actionBar，有的 true
     * @param statusBar 是否有状态bar，有的话需要隐藏 true , 没有 false
     */
    public void gotoFullVideo(boolean actionBar, boolean statusBar) {
        if (!GSYVideoManager.isFullState(this)) {
            if (orientationUtils != null) {
                orientationUtils.backToProtVideo();
            }
            gsyVideoPlayer.startWindowFullscreen(this, actionBar, statusBar);
        } else {
            GSYVideoManager.backFromWindowFull(this);
        }

    }

    /**
     * 进入全屏播放
     */
    public void gotoFullVideo() {
        if (!GSYVideoManager.isFullState(this)) {
            if (orientationUtils != null) {
                orientationUtils.backToProtVideo();
            }
            gsyVideoPlayer.startWindowFullscreen(this, false, false);
        } else {
            GSYVideoManager.backFromWindowFull(this);
        }
    }

    public void onFullVideoClicked() {
        if (GSYVideoManager.isFullState(this)) {
            gsyVideoPlayer.onClick(gsyVideoPlayer.getStartButton());
        } else {
            gotoFullVideo();
        }
    }


    /******************* VideoAllCallBack start*************************/
    @Override
    public void onStartPrepared(String url, Object... objects) {
    }

    @Override
    public void onPrepared(String url, Object... objects) {
        orientationUtils.setEnable(true);
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
    }

    @Override
    public void onClickStop(String url, Object... objects) {
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
    }

    @Override
    public void onClickResume(String url, Object... objects) {
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {
    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {
    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        gsyVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
    }


    @Override
    public void onQuitFullscreen(String url, Object... objects) {
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {
    }

    @Override
    public void onPlayError(String url, Object... objects) {
    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {
    }

    @Override
    public void onClickBlank(String url, Object... objects) {
    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {
    }
}
