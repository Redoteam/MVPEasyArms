package com.yl.video.gsyvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.yl.video.R;

/**
 * Created by 寻水的鱼 on 2018/9/4.
 */
public class MainEmptyControlVideo extends NormalGSYVideoPlayer {
    private ImageView mCoverImage;

    public MainEmptyControlVideo(Context context) {
        super(context);
    }

    public MainEmptyControlVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MainEmptyControlVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mCoverImage = findViewById(R.id.thumbImage);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_empty_control_video;
    }

    public void loadCoverImage(int res) {
        mCoverImage.setImageResource(res);
        /*Glide.with(getContext().getApplicationContext()).setDefaultRequestOptions(new
                RequestOptions().frame(1000000).centerCrop().error(res).placeholder(res)).load(url).into(mCoverImage);*/
    }


    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }


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
