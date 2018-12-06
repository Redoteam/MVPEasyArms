package com.yl.video.gsyvideo.act;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yl.video.R;
import com.yl.video.gsyvideo.view.PreViewGSYVideoPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class PreViewGSYVideoActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> {

    PreViewGSYVideoPlayer webPlayer;
    private float deltaX = 80;
    private float deltaY = 0;
    private int backupRendType;
    private int seekBarProgress;
    private SeekBar seekBar;
    private long time;
    private String mVideoPath;
    private boolean loop;
    private String TAG = PreViewGSYVideoActivity.class.getSimpleName();
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view_gsyvideo);
        webPlayer = findViewById(R.id.web_player);
        backupRendType = GSYVideoType.getRenderType();
        //注意，这是全局生效的
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        time = getIntent().getLongExtra("time", 0);
        mVideoPath = getIntent().getStringExtra("videoPath");
        if (TextUtils.isEmpty(mVideoPath)) {
            finish();
            return;
        }
        url = mVideoPath;
        loop = getIntent().getBooleanExtra("loop", false);

        resolveNormalVideoUI();

        initVideoBuilderMode();


        webPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                    webPlayer.getCurrentPlayer().setRotateViewAuto(!lock);
                }
            }
        });


        orientationUtils.setRotateWithSystem(true);

        seekBar = webPlayer.getSeekBar();
        seekBarProgress = seekBar.getProgress();

//         webPlayer.seekTo(time);
        webPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                if (loop) {
                    webPlayer.release();
                    webPlayer.setUp(url, false, "");
                    webPlayer.startPlayLogic();
                } else {
                    Toast.makeText(PreViewGSYVideoActivity.this, "播放完毕！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onPlayError(String url, Object... objects) {
                super.onPlayError(url, objects);
            }
        });
        webPlayer.startPlayLogic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler = null;
        }
        if (webPlayer != null && webPlayer.isInPlayingState())
            webPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        initHandler();
        super.onResume();

        if (webPlayer != null && !webPlayer.isInPlayingState()) {
            webPlayer.onVideoResume();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //设置为GL播放模式，才能支持滤镜，注意此设置是全局的
        GSYVideoType.setRenderType(backupRendType);

        //释放所有
        webPlayer.setVideoAllCallBack(null);
        webPlayer.release();
    }

    @Override
    public void onBackPressed() {
        //设置为GL播放模式，才能支持滤镜，注意此设置是全局的
        GSYVideoType.setRenderType(backupRendType);

        //释放所有
        if (webPlayer != null) {
            webPlayer.setVideoAllCallBack(null);
            webPlayer.release();
        }
        super.onBackPressed();

    }

    @Override
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return webPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {

        //增加封面。内置封面可参考SampleCoverVideo
        ImageView imageView = new ImageView(this);

        mVideoPath = getIntent().getStringExtra("videoPath");
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder().setThumbImageView(imageView).setUrl(url).setCacheWithPlay(false).setRotateWithSystem(false)
//                .setVideoTitle("测试视频")
                .setIsTouchWiget(true).setRotateViewAuto(false).setLockLand(false).setShowFullAnimation(false)

                .setNeedLockFull(true);
        if (time != 0) {
            gsyVideoOptionBuilder.setSeekOnStart(time);
        }
        return gsyVideoOptionBuilder;
    }

    @Override
    public void clickForFullScreen() {

    }

    /**
     * 是否启动旋转横屏，true表示启动
     *
     * @return true
     */
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }


    private void resolveNormalVideoUI() {
        //增加title
        webPlayer.getTitleTextView().setVisibility(View.GONE);
        webPlayer.getBackButton().setVisibility(View.GONE);
    }

    private boolean isTouchMove = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        int currentState = webPlayer.getCurrentState();
        Log.e(TAG, "gsyvideo player currentState--->:" + currentState);
        switch (keyCode) {

            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_MEDIA_REWIND:

                if (timer != null)
                    timer.cancel();
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (!isTouchMove) {
                        webPlayer.mtouchSurfaceDown();
                        deltaX = -80;
                        isTouchMove = true;
                    }

                    deltaX -= 3;
                    webPlayer.onKeyDownLeft(deltaX);
//                    deltaX--;
//                    webPlayer.mtouchSurfaceMove(deltaX,deltaY,0);
                    initTimer();
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_FORWARD://快进键
                if (timer != null)
                    timer.cancel();
                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    if (!isTouchMove) {
                        webPlayer.mtouchSurfaceDown();
                        deltaX = 80;
                        isTouchMove = true;
                    }
//                    deltaX++;
//                    webPlayer.mtouchSurfaceMove(deltaX,deltaY,0);
                    initTimer();
                    deltaX += 3;
                    webPlayer.onKeyDownRight(deltaX);
//                    webPlayer.mtouchSurfaceUp();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                webPlayer.mtouchDoubleUp();
                break;

        }

        return super.onKeyDown(keyCode, event);
    }


    private Timer timer;

    private void initTimer() {
        timer = new Timer();

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isTouchMove = false;
                        if (webPlayer != null) {
                            webPlayer.mtouchSurfaceUp();
                        }

                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000);
    }

    private Handler handler = null;

    private void initHandler() {
        if (handler == null) {
            handler = new Handler();
        }
    }


}
