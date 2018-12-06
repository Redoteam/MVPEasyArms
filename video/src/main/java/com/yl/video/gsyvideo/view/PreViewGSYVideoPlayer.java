package com.yl.video.gsyvideo.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.yl.video.R;

/**
 * 进度图小图预览的另类实现
 * Created by shuyu on 2016/12/10.
 */

public class PreViewGSYVideoPlayer extends NormalGSYVideoPlayer {

    private RelativeLayout mPreviewLayout;

    private ImageView mPreView;

    //是否因为用户点击
    private boolean mIsFromUser;

    //是否打开滑动预览
    private boolean mOpenPreView = true;
    private LinearLayout layout_bottom;

    public int getmPreProgress() {
        return mPreProgress;
    }

    public void setmPreProgress(int mPreProgress) {
        this.mPreProgress = mPreProgress;
    }

    private int mPreProgress = -2;
    private float deltaX;
    private float deltaY = 0;
    private View surface_container;

    public SeekBar getSeekBar() {
        return seekBar;
    }

    SeekBar seekBar;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public PreViewGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public PreViewGSYVideoPlayer(Context context) {
        super(context);
    }

    public PreViewGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        initView();
    }

    private void initView() {
        mPreviewLayout = (RelativeLayout) findViewById(R.id.preview_layout);
        mPreView = (ImageView) findViewById(R.id.preview_image);
        surface_container = findViewById(R.id.surface_container);
        seekBar = findViewById(R.id.progress);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_layout_preview;
    }


    @Override
    protected void prepareVideo() {
        super.prepareVideo();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (fromUser && mOpenPreView) {
            int width = seekBar.getWidth();
            int time = progress * getDuration() / 100;
            int offset = (int) (width - (getResources().getDimension(R.dimen.seek_bar_image) / 2)) / 100 * progress;
            Debuger.printfError("***************** " + progress);
            Debuger.printfError("***************** " + time);
            showPreView(mOriginUrl, time);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPreviewLayout.getLayoutParams();
            layoutParams.leftMargin = offset;
            //设置帧预览图的显示位置
            mPreviewLayout.setLayoutParams(layoutParams);
            if (mHadPlay && mOpenPreView) {
                mPreProgress = progress;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        if (mOpenPreView) {
            mIsFromUser = true;
            mPreviewLayout.setVisibility(VISIBLE);
            mPreProgress = -2;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOpenPreView) {
            if (mPreProgress >= 0) {
                seekBar.setProgress(mPreProgress);
            }
            super.onStopTrackingTouch(seekBar);
            mIsFromUser = false;
            mPreviewLayout.setVisibility(GONE);
        } else {
            super.onStopTrackingTouch(seekBar);
        }
    }

    @Override
    protected void setTextAndProgress(int secProgress) {
        if (mIsFromUser) {
            return;
        }
        super.setTextAndProgress(secProgress);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        PreViewGSYVideoPlayer customGSYVideoPlayer = (PreViewGSYVideoPlayer) gsyBaseVideoPlayer;
        customGSYVideoPlayer.mOpenPreView = mOpenPreView;
        return gsyBaseVideoPlayer;
    }


    @Override
    public void onPrepared() {
        super.onPrepared();
        startDownFrame(mOriginUrl);
    }

    public boolean isOpenPreView() {
        return mOpenPreView;
    }

    /**
     * 如果是需要进度条预览的设置打开，默认关闭
     */
    public void setOpenPreView(boolean localFile) {
        this.mOpenPreView = localFile;
    }


    /**
     * 显示预览图片
     *
     * @param url
     * @param time
     */
    public void showPreView(String url, long time) {
        int width = CommonUtil.dip2px(getContext(), 150);
        int height = CommonUtil.dip2px(getContext(), 100);
        Glide.with(getContext().getApplicationContext()).setDefaultRequestOptions(new RequestOptions()
                //这里限制了只从缓存读取
                .onlyRetrieveFromCache(true).frame(1000 * time).override(width, height).dontAnimate().centerCrop()).load(url).into(mPreView);
    }


    private void startDownFrame(String url) {
        for (int i = 1; i <= 100; i++) {
            int time = i * getDuration() / 100;
            int width = CommonUtil.dip2px(getContext(), 150);
            int height = CommonUtil.dip2px(getContext(), 100);
            Glide.with(getContext().getApplicationContext()).setDefaultRequestOptions(new RequestOptions().frame(1000 * time).override(width, height).centerCrop()).load(url).preload(width, height);

        }
    }


    public void onKeyDownRight(float deltaX) {
        touchSurfaceMoveFullLogic(Math.abs(deltaX), 0);

//        deltaX += 3;
        int curWidth = CommonUtil.getCurrentScreenLand((Activity) getActivityContext()) ? mScreenHeight : mScreenWidth;

        int totalTimeDuration = getDuration();
        if (deltaX >= totalTimeDuration) {
            deltaX = totalTimeDuration;
        }
        mSeekTimePosition = (int) (mDownPosition + (deltaX * totalTimeDuration / curWidth) / mSeekRatio);

        if (mSeekTimePosition > totalTimeDuration)
            mSeekTimePosition = totalTimeDuration;
        String seekTime = CommonUtil.stringForTime(mSeekTimePosition);
        String totalTime = CommonUtil.stringForTime(totalTimeDuration);
        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);

    }

    public void mtouchSurfaceDown() {

        touchSurfaceDown(0, 0);
    }

    public void onKeyDownLeft(float deltaX) {
        touchSurfaceMoveFullLogic(Math.abs(deltaX), 0);


        int curWidth = CommonUtil.getCurrentScreenLand((Activity) getActivityContext()) ? mScreenHeight : mScreenWidth;

        int totalTimeDuration = getDuration();
        mSeekTimePosition = (int) (mDownPosition + (deltaX * totalTimeDuration / curWidth) / mSeekRatio);
        if (mSeekTimePosition > totalTimeDuration)
            mSeekTimePosition = totalTimeDuration;
        String seekTime = CommonUtil.stringForTime(mSeekTimePosition);
        String totalTime = CommonUtil.stringForTime(totalTimeDuration);
        showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
    }


    public void mtouchSurfaceUp() {
        touchSurfaceUp();

        int duration1 = getDuration();
        int progress1 = mSeekTimePosition * 100 / (duration1 == 0 ? 1 : duration1);
        if (mBottomProgressBar != null)
            mBottomProgressBar.setProgress(progress1);

        mTouchingProgressBar = false;
        dismissProgressDialog();
        dismissVolumeDialog();
        dismissBrightnessDialog();
        if (mChangePosition && getGSYVideoManager() != null && (mCurrentState == CURRENT_STATE_PLAYING || mCurrentState == CURRENT_STATE_PAUSE)) {
            try {
                getGSYVideoManager().seekTo(mSeekTimePosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int duration = getDuration();
            int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
            if (mProgressBar != null) {
                mProgressBar.setProgress(progress);
            }
            if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                Debuger.printfLog("onTouchScreenSeekPosition");
                mVideoAllCallBack.onTouchScreenSeekPosition(mOriginUrl, mTitle, this);
            }
        } else if (mBrightness) {
            if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                Debuger.printfLog("onTouchScreenSeekLight");
                mVideoAllCallBack.onTouchScreenSeekLight(mOriginUrl, mTitle, this);
            }
        } else if (mChangeVolume) {
            if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                Debuger.printfLog("onTouchScreenSeekVolume");
                mVideoAllCallBack.onTouchScreenSeekVolume(mOriginUrl, mTitle, this);
            }
        }
    }


    //双击播放|暂停
    public void mtouchDoubleUp() {
        touchDoubleUp();
    }

    public void setLayout_bottomVisivable(boolean show) {
        layout_bottom.setVisibility(show ? VISIBLE : INVISIBLE);
    }

}
