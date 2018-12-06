package com.yl.video.gsyvideo.strategy;

import android.content.Context;

import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;

/**
 * Created by 寻水的鱼 on 2018/11/1.
 */
public abstract class BaseVideoOptionStrategy implements IVideoOptionStrategy {
    //设置播放器内核
    public void setPlayerIJKEXOPLAYER2Kernel(Context context) {
//        GSYVideoManager.instance().setVideoType(context, GSYVideoType.IJKEXOPLAYER2);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式
    }

    //设置成系统播放器内核
    public void setPlayerSystemKernel(Context context) {
        PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式
    }

    //设置开启硬解码
    public void setMediaCodec(boolean enable) {
        if (enable) {
            GSYVideoType.enableMediaCodec();
        } else {
            GSYVideoType.disableMediaCodec();
        }
    }
}
