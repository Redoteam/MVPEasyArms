package com.yl.video.gsyvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.yl.video.R;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


/**
 * Created by 寻水的鱼 on 2018/8/20.
 */

public class PreviewVideo extends NormalGSYVideoPlayer {
    private static final String TAG = PreviewVideo.class.getSimpleName();
    private RelativeLayout mPreviewLayout;

    private ImageView mPreView;

    //是否因为用户点击
    private boolean mIsFromUser;

    //是否打开滑动预览
    private boolean mOpenPreView = false;

    private int mPreProgress = -2;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public PreviewVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public PreviewVideo(Context context) {
        super(context);
    }

    public PreviewVideo(Context context, AttributeSet attrs) {
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
        if (fromUser) {
            if (mOpenPreView) {
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
            }
            if (mHadPlay) {
                mPreProgress = progress;
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        mIsFromUser = true;
        if (mOpenPreView) {
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
            if (mPreProgress >= 0) {
                seekBar.setProgress(mPreProgress);
            }
            super.onStopTrackingTouch(seekBar);
            mIsFromUser = false;
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
        PreviewVideo customGSYVideoPlayer = (PreviewVideo) gsyBaseVideoPlayer;
        customGSYVideoPlayer.mOpenPreView = mOpenPreView;
        return gsyBaseVideoPlayer;
    }


    @Override
    public void onPrepared() {
        super.onPrepared();
        if (mOpenPreView)
            startDownFrame(mOriginUrl);
    }

  /*  public boolean isOpenPreView() {
        return mOpenPreView;
    }*/

    /**
     * 如果是需要进度条预览的设置打开，默认关闭
     */
    /*public void setOpenPreView(boolean localFile) {
        this.mOpenPreView = localFile;
    }*/
    private void showPreView(String url, long time) {
        Log.e(TAG, "showPreview utl--->" + url);
        int width = CommonUtil.dip2px(getContext(), 150);
        int height = CommonUtil.dip2px(getContext(), 100);
        Glide.with(getContext().getApplicationContext()).setDefaultRequestOptions(new RequestOptions()
                //这里限制了只从缓存读取
                .onlyRetrieveFromCache(true).frame(1000 * time).override(width, height).dontAnimate().centerCrop()).load(url).into(mPreView);
    }


    private void startDownFrame(String url) {
        //同步完50帧
        for (int i = 1; i <= 50; i++) {
            int time = i * getDuration() / 50;
            int width = CommonUtil.dip2px(getContext(), 150);
            int height = CommonUtil.dip2px(getContext(), 100);
            Glide.with(getContext().getApplicationContext()).setDefaultRequestOptions(new RequestOptions().frame(1000 * time).override(width, height).centerCrop()).load(url).preload(width, height);

        }
    }


    /**
     * 显示视频 第三秒 那一帧
     *
     * @param context
     * @param uri
     * @param imageView
     * @param frameTimeMicros 要截取得时间。单位：微秒
     */
    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {
        // 这里的时间是以微秒为单位
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

}

