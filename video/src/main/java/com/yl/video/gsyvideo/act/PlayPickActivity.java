package com.yl.video.gsyvideo.act;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.yl.video.R;
import com.yl.video.gsyvideo.listener.OnTransitionListener;
import com.yl.video.gsyvideo.model.SwitchVideoModel;
import com.yl.video.gsyvideo.view.SmartPickVideo;

import java.util.ArrayList;
import java.util.List;


/**
 * 单独的视频播放页面
 * Created by shuyu on 2016/11/11.
 */
public class PlayPickActivity extends AppCompatActivity {

    public final static String IMG_TRANSITION = "IMG_TRANSITION";
    public final static String TRANSITION = "TRANSITION";

    SmartPickVideo videoPlayer;

    OrientationUtils orientationUtils;

    private boolean isTransition;

    private Transition transition;
    private long time;
    private String mVideoPath;
    private boolean loop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pick);
        videoPlayer = findViewById(R.id.video_player);
        isTransition = getIntent().getBooleanExtra(TRANSITION, false);
        init();
    }

    private void init() {
        time = getIntent().getLongExtra("time", 0);
        mVideoPath = getIntent().getStringExtra("videoPath");
        loop = getIntent().getBooleanExtra("loop", false);
        //借用了jjdxm_ijkplayer的URL
        String source1 = "http://116.77.76.2:5000/nn_vod/nn_x64/aWQ9ODViZTMxNGMxMWVkOTI4OTIzYWQ4ZjExMmY3YWEyNTgmdXJsX2MxPTJmNjY3NDc0NzQyZjY2NzQ3ODc3MmY2Njc0Nzg3NzMyMzAzMTM4MzAzNjJmMzIzMDMxMzgzMDM2MzEzMTVmNzg3MzcwMmU2ZDcwMzQwMCZubl9haz0wMWU1YjEzMTU5MzRkZjJjZmNmZTMz/YjE3OGQ0M2ZiMzJmJm5waXBzPTE3Mi4yNy4xNy43NTo1MTAwJm5jbXNpZD0xMDAxMDEwMSZuZ3M9NWIzNDc2MzIyY2I5Njc2OGU4ODAzMmY2ZDk0ZjQxOTQmbm5kPXR3bmV0Lmd1YW5nZG9uZy5zaGVuJm5zZD16Z2R4LnNoZW56aGVuJm5mdD1tcDQmbm5fdXNlcl9p/ZD04MDc1NTg4MDExMzgzNTgyJm5kdD1zdGImbmRpPTgwNzU1ODgwMTEzODM1ODImbmR2PTEuMS4zOS5TVEIuV1NZUFQuU1RELlRXNEswMi5SZWxlYXNlJm5zdD1pcHR2Jm5jYT0lMjZuYWklM2QxMzElMjZubl9jcCUzZHRpYW53ZWlfeGZmdCZuYWw9MDEzMjc2MzQ1/YjA2MDdhOTY3ZTg5NzA4MjU1NWQ0NGE0NjQxMjY0N2I5MGI2NA,,/85be314c11ed928923ad8f112f7aa258.mp4";
        source1 = mVideoPath;
        String name = "普通";
        SwitchVideoModel switchVideoModel = new SwitchVideoModel(name, source1);

        String source2 = "http://116.77.76.2:5000/nn_vod/nn_x64/aWQ9ODViZTMxNGMxMWVkOTI4OTIzYWQ4ZjExMmY3YWEyNTgmdXJsX2MxPTJmNjY3NDc0NzQyZjY2NzQ3ODc3MmY2Njc0Nzg3NzMyMzAzMTM4MzAzNjJmMzIzMDMxMzgzMDM2MzEzMTVmNzg3MzcwMmU2ZDcwMzQwMCZubl9haz0wMWU1YjEzMTU5MzRkZjJjZmNmZTMz/YjE3OGQ0M2ZiMzJmJm5waXBzPTE3Mi4yNy4xNy43NTo1MTAwJm5jbXNpZD0xMDAxMDEwMSZuZ3M9NWIzNDc2MzIyY2I5Njc2OGU4ODAzMmY2ZDk0ZjQxOTQmbm5kPXR3bmV0Lmd1YW5nZG9uZy5zaGVuJm5zZD16Z2R4LnNoZW56aGVuJm5mdD1tcDQmbm5fdXNlcl9p/ZD04MDc1NTg4MDExMzgzNTgyJm5kdD1zdGImbmRpPTgwNzU1ODgwMTEzODM1ODImbmR2PTEuMS4zOS5TVEIuV1NZUFQuU1RELlRXNEswMi5SZWxlYXNlJm5zdD1pcHR2Jm5jYT0lMjZuYWklM2QxMzElMjZubl9jcCUzZHRpYW53ZWlfeGZmdCZuYWw9MDEzMjc2MzQ1/YjA2MDdhOTY3ZTg5NzA4MjU1NWQ0NGE0NjQxMjY0N2I5MGI2NA,,/85be314c11ed928923ad8f112f7aa258.mp4";
        String name2 = "清晰";
        source2 = mVideoPath;
        SwitchVideoModel switchVideoModel2 = new SwitchVideoModel(name2, source2);

        List<SwitchVideoModel> list = new ArrayList<>();
        list.add(switchVideoModel);
        list.add(switchVideoModel2);

        videoPlayer.setUp(list, false, "");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.xxx1);
        videoPlayer.setThumbImageView(imageView);

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);

        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //过渡动画
        initTransition();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        GSYVideoManager.releaseAllVideos();
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
            }, 500);
        }
    }


    private void initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
            ViewCompat.setTransitionName(videoPlayer, IMG_TRANSITION);
            addTransitionListener();
            startPostponedEnterTransition();
        } else {
            videoPlayer.startPlayLogic();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean addTransitionListener() {
        transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new OnTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    super.onTransitionEnd(transition);
                    videoPlayer.startPlayLogic();
                    transition.removeListener(this);
                }
            });
            return true;
        }
        return false;
    }

}
