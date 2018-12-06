package com.yl.video.gsyvideo.utils;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yl.video.gsyvideo.strategy.DefaultVideoOptionStrategy;
import com.yl.video.gsyvideo.strategy.IVideoOptionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;


/**
 * Created by 寻水的鱼 on 2018/6/28.
 */
public class VideoHelper {
    @Deprecated
    public static void defaultOption(Context context) {
//        GSYVideoManager.instance().setVideoType(context,SYSTEMPLAYER);
        //PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式
//    PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式
//    PlayerFactory.setPlayManager(IjkPlayerManager.class);//ijk模式
        //
        //CacheFactory.setCacheManager(ExoPlayerCacheManager.class);//exo缓存模式，支持m3u8，只支持exo
        //CacheFactory.setCacheManager(ProxyCacheManager.class);//代理缓存模式，支持所有模式，不支持m3u8等
    }

    public static void initVideoOption(Context context) {
       /* List<VideoOptionModel> list = new ArrayList<>();
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", "tcp");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_flags", "prefer_tcp");
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "allowed_media_types", "video"); //根据媒体类型来配置
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "buffer_size", 10 * 1024);
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1);  // 无限读
        list.add(videoOptionModel);
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100);
        list.add(videoOptionModel);

        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 50);
        list.add(videoOptionModel);

        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1);
        list.add(videoOptionModel);
        //  关闭播放器缓冲，这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        list.add(videoOptionModel);
        defaultOption(context);*/
        DefaultVideoOptionStrategy defaultVideoOptionStrategy = new DefaultVideoOptionStrategy();
        defaultVideoOptionStrategy.initVideoOption(context);
        defaultVideoOptionStrategy.initVideoType();
    }

    public static <K> void initVideoOption(Context context, Map<K, IVideoOptionStrategy> map, K key,IVideoOptionStrategy defaultVideoOption) {
        IVideoOptionStrategy iVideoOptionStrategy = map.get(key);
        if (iVideoOptionStrategy == null) {
            if(defaultVideoOption == null){
                initVideoOption(context);
            }else {
                defaultVideoOption.initVideoType();
                defaultVideoOption.initVideoOption(context);
            }

        } else {
            iVideoOptionStrategy.initVideoType();
            iVideoOptionStrategy.initVideoOption(context);
        }

    }

    public interface OnAllVideoPlayErrorListener {
        void onVideoPlayError(StandardGSYVideoPlayer gsyVideoPlayer);
    }

    public static OnAllVideoPlayErrorListener getVideoPlayListener() {
        return mVideoPlayListener;
    }

    public static void setVideoPlayListener(OnAllVideoPlayErrorListener videoPlayListener) {
        VideoHelper.mVideoPlayListener = videoPlayListener;
    }

    private static OnAllVideoPlayErrorListener mVideoPlayListener;

}
