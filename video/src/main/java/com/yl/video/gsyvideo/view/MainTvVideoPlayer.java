package com.yl.video.gsyvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by 寻水的鱼 on 2018/8/23.
 */
public class MainTvVideoPlayer extends TvVideoPlayer {
    public MainTvVideoPlayer(Context context) {
        super(context);
    }

    public MainTvVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MainTvVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if(mIfCurrentIsFullscreen){
            getParent().requestDisallowInterceptTouchEvent(true);
        }else {
            getParent().requestDisallowInterceptTouchEvent(false);

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if(mIfCurrentIsFullscreen){
            return super.onInterceptTouchEvent(ev);
        }else {
            return true;
        }

    }*/

    /*   @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(mIfCurrentIsFullscreen){
            return super.onTouch(v, event);
        }else {
            return ((View)getParent()).onTouchEvent(event);
        }

    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mIfCurrentIsFullscreen) {
            return super.onKeyDown(keyCode, event);
        } else {
            if (mOnKeyDownListener != null) {
                return mOnKeyDownListener.onKeyDown(keyCode, event);
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    public void setOnKeyDownListener(OnKeyDownListener onKeyDownListener) {
        this.mOnKeyDownListener = onKeyDownListener;
    }

    private OnKeyDownListener mOnKeyDownListener;

    public interface OnKeyDownListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }
}
