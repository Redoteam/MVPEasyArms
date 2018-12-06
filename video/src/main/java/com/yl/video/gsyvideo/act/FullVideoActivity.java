package com.yl.video.gsyvideo.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yl.video.R;
import com.yl.video.gsyvideo.utils.VideoHelper;

/**
 * Created by 寻水的鱼 on 2018/8/20.
 */
public class FullVideoActivity extends AppCompatActivity {
    public StandardGSYVideoPlayer gsyVideoPlayer;
    private long mTime; //从指定时间开始播放
    private String mVideoPath;//播放地址
    private boolean isLoop; // 是否循环播放
    private OrientationUtils orientationUtils;
    private boolean isLoadingSucceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);
        gsyVideoPlayer = findViewById(R.id.video_player);

        Intent intent = getIntent();

        mTime = intent.getLongExtra("time", 0);
        mVideoPath = intent.getStringExtra("videoPath");
        isLoop = intent.getBooleanExtra("loop", false);

        initVideoSetting();
        getGSYVideoOptionBuilder().build(gsyVideoPlayer);
        gsyVideoPlayer.startPlayLogic();
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
        gsyVideoPlayer.setLockLand(true);
        gsyVideoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                orientationUtils.setEnable(true);
                isLoadingSucceed = true;
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                gsyVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
                gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                if (isLoop) {
                    playVideo(mVideoPath);
                }
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                Toast.makeText(FullVideoActivity.this.getApplicationContext(), "播放失败！", Toast.LENGTH_SHORT).show();
                if (!isLoadingSucceed && VideoHelper.getVideoPlayListener() != null && gsyVideoPlayer != null) {
                    gsyVideoPlayer.release();
                    VideoHelper.getVideoPlayListener().onVideoPlayError(gsyVideoPlayer);
                }
            }
        });
    }

    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {

        //增加封面。内置封面可参考SampleCoverVideo
        ImageView imageView = new ImageView(this);

        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder().setThumbImageView(imageView).setUrl(mVideoPath).setCacheWithPlay(false).setRotateWithSystem(false)
//                .setVideoTitle("测试视频")
                .setIsTouchWiget(true).setRotateViewAuto(false).setLockLand(false).setShowFullAnimation(false)

                .setNeedLockFull(true);
        if (mTime != 0) {
            gsyVideoOptionBuilder.setSeekOnStart(mTime);
        }
        return gsyVideoOptionBuilder;
    }


    public void playVideo(String playUrl) {
        if (gsyVideoPlayer != null) {
            gsyVideoPlayer.setUp(playUrl, false, "");
            gsyVideoPlayer.startPlayLogic();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onDestroy() {
        VideoHelper.setVideoPlayListener(null);
//        GSYVideoManager.releaseAllVideos();
        if (gsyVideoPlayer != null) {
            gsyVideoPlayer.release();
            gsyVideoPlayer = null;
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        VideoHelper.setVideoPlayListener(null);
//        GSYVideoManager.releaseAllVideos();
        if (gsyVideoPlayer != null) {
            gsyVideoPlayer.release();
            gsyVideoPlayer = null;
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
            orientationUtils = null;
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
        }

    }


}
