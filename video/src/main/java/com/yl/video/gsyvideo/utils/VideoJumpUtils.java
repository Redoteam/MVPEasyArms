package com.yl.video.gsyvideo.utils;

import android.content.Context;
import android.content.Intent;

import com.yl.video.gsyvideo.act.FullVideoActivity;

/**
 * Created by 寻水的鱼 on 2018/8/14.
 */
public class VideoJumpUtils {

    /***
     * 全屏视频
     * @param mContext
     * @param path
     * @param loop 是否循环播放
     * @param time 指定开始播放的位置
     */
    public static void gotoFullVideo(Context mContext, String path, boolean loop, long time) {
        mContext.startActivity(new Intent(mContext, FullVideoActivity.class).putExtra("videoPath", path).putExtra("time", time).putExtra("loop", loop));
    }
}
