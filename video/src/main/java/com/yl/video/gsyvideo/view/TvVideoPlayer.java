package com.yl.video.gsyvideo.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.yl.video.R;

/**
 * Created by 寻水的鱼 on 2018/8/17.
 */
public class TvVideoPlayer extends PreviewVideo {
    public TvVideoPlayer(Context context) {
        super(context);
        getDisplay(context);
    }

    public TvVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        getDisplay(context);
    }

    public TvVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        getDisplay(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

  /*  @Override
    public int getLayoutId() {
        return R.layout.video_layout_custom;
    }*/


    /**
     * 亮度、进度、音频
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        float x = event.getX();
        float y = event.getY();
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
            return true;
        }
        if (id == R.id.fullscreen) {
            return false;
        }
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchSurfaceDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //触摸的X
// protected float mDownX;
//触摸的Y
// protected float mDownY;
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if ((mIfCurrentIsFullscreen && mIsTouchWigetFull) || (mIsTouchWiget && !mIfCurrentIsFullscreen)) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
                        }
                    }
                    touchSurfaceMove(deltaX, deltaY, y);
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    touchSurfaceUp();
                    startProgressTimer();
//不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
                        return true;
                    }
                    break;
            }
            gestureDetector.onTouchEvent(event);
        } else if (id == R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                case MotionEvent.ACTION_MOVE:
                    cancelProgressTimer();
                    ViewParent vpdown = getParent();
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true);
                        vpdown = vpdown.getParent();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    startProgressTimer();
                    ViewParent vpup = getParent();
                    while (vpup != null) {
                        vpup.requestDisallowInterceptTouchEvent(false);
                        vpup = vpup.getParent();
                    }
                    mBrightnessData = -1f;
                    break;
            }
        }
        return false;
    }

    //点击屏幕的默认值：屏幕中心点X,Y数值
    private static int pointX;
    private static int pointY;
    private static int moveX;
    private static int moveY;

    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private static void getDisplay(Context context) {
        //获得屏幕宽高
        Point size = getDisplaySize(context);
        pointX = size.x / 2;
        pointY = size.y / 2;
        moveX = pointX;
        moveY = pointY;
    }

    //处理按键快进和快退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.ACTION_DOWN:
                break;
            case KeyEvent.ACTION_UP:
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                onClickUi();
                firstKeyDown();
                mHandler.sendEmptyMessage(LEFT);
                mHandler.sendEmptyMessageDelayed(CANCLE, 1000);
                resetTime();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                onClickUi();
                firstKeyDown();
                mHandler.sendEmptyMessage(RIGHT);
                mHandler.sendEmptyMessageDelayed(CANCLE, 1000);
                resetTime();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                int state = getCurrentState();
                switch (state) {
                    case GSYVideoPlayer.CURRENT_STATE_PLAYING:
//                        onVideoPause();
//                        onClick(getStartButton());
                        touchDoubleUp();
                        break;
                    case GSYVideoPlayer.CURRENT_STATE_PAUSE:
//                        onVideoResume();
//                        onClick(getStartButton());
//                        onClickUiToggle();
                        touchDoubleUp();
                        break;
                    case GSYVideoPlayer.CURRENT_STATE_AUTO_COMPLETE:
                        startPlayLogic();
                        isPlayComplete = false;
                        mSeekTimePosition = 0;
                        mProgressBar.setProgress(0);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static boolean firstKeyDown = true;

    private void onClickUi() {
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
// return true;
        }
    }

    //第一次按下左右键
    private void firstKeyDown() {
        if (firstKeyDown) {
            touchSurfaceDown(pointX, pointY);
            firstKeyDown = false;
            if (mSeekTimePosition >= getDuration() || isPlayComplete) {

            } else {
                onStartTrackingTouch(mProgressBar);
            }
        }
    }

    public static boolean isPlayComplete = false;

    public static void setPlayComplete(boolean PlayComplete) {
        isPlayComplete = PlayComplete;
    }

    //连续按下左右键
    private void keyMove() {
        if ((mIfCurrentIsFullscreen && mIsTouchWigetFull) || (mIsTouchWiget && !mIfCurrentIsFullscreen)) {
            if (!mChangePosition && !mChangeVolume && !mBrightness) {
                touchSurfaceMoveFullLogic(Math.abs(moveX - pointX), 0);
            }
        }
        if (mSeekTimePosition >= getDuration() || isPlayComplete) {
            mHandler.sendEmptyMessageDelayed(CANCLE, 2500);
            mBottomContainer.setVisibility(GONE);
        } else {
            touchSurfaceMove(moveX - pointX, 0, pointY);
            mBottomContainer.setVisibility(VISIBLE);
            onProgressChanged(mProgressBar, mSeekTimePosition * 100 / getDuration(), true);
        }
    }

    //定义变量
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int CANCLE = 2;

    private static int tim = 2;

    //程序启动时，初始化并发送消息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LEFT:
                    if (tim > 0) {
                        if (tim > 1) {
                            moveX = moveX - pointX / 13;
                        } else {
                            moveX = moveX - pointX / 20;
                        }
                        tim -= 1;
                    } else {
                        moveX = moveX - pointX / 200;
                    }
                    keyMove();
                    break;
                case RIGHT:
                    if (tim > 0) {
                        if (tim > 1) {
                            moveX = moveX + pointX / 13;//13-20
                        } else {
                            moveX = moveX + pointX / 20;
                        }
                        tim -= 1;
                    } else {
                        moveX = moveX + pointX / 200;
                    }
                    keyMove();
                    break;
                case CANCLE:                        //停止按键
                    firstKeyDown = true;
                    moveX = pointX;
                    firstKeyDown = true;
                    tim = 2;
                    onStopTrackingTouch(mProgressBar);
                    mBottomContainer.setVisibility(GONE);
                    startDismissControlViewTimer();
                    touchSurfaceUp();
                    startProgressTimer();
                    //不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
// return true;
                    }
                    break;
            }
        }
    };

    //重置
    public void resetTime() {
        mHandler.removeMessages(CANCLE);
        mHandler.sendEmptyMessageDelayed(CANCLE, 1000);
    }

    @Override
    protected void dismissProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    @Override
    protected void touchDoubleUp()
    {
        if(GSYVideoPlayer.CURRENT_STATE_AUTO_COMPLETE == getCurrentState()){
            onClick(getStartButton());
        }
        super.touchDoubleUp();
    }*/
}
