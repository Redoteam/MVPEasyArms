package com.yl.acoreui.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yl.acoreui.utils.ClickFilter;
import com.yl.acoreui.utils.ProxyHandle;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 寻水的鱼 on 2018/11/16.
 */
public abstract class IVideoItemViewDelegate<V extends StandardGSYVideoPlayer, T> extends GSYSampleCallBack implements BaseHasHeadLineItemViewDelegate<T> {
    public final static String TAG = "IVideoItemViewDelegate";
    protected GSYVideoOptionBuilder mGsyVideoOptionBuilder;

    protected Context mContext;

    public IVideoItemViewDelegate(@NonNull Context context) {
        mContext = context;
        mGsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    public IVideoItemViewDelegate(@NonNull Context context, @NonNull OnVideoPlayStartClickListener<V> listener) {
        mContext = context;
        mGsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        mOnStartClickListener = listener;
    }


    @Override
    public void convert(ViewHolder holder, T t, int position) {
        initVideoOption(mGsyVideoOptionBuilder, holder, t, position);
        if (mOnStartClickListener != null) {
            ClickFilter.setFilter(getGsyVideoPlayer(holder).getStartButton(), (v, origin) -> {
                mOnStartClickListener.onVideoPlayStartClicked(v, origin, getGsyVideoPlayer(holder));
                return true;
            });
        }
        //设置返回键
        getGsyVideoPlayer(holder).getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        getGsyVideoPlayer(holder).getFullscreenButton().setOnClickListener(v -> resolveFullBtn(getGsyVideoPlayer(holder)));
    }

    /**
     * 全屏幕按键处理
     */
    protected void resolveFullBtn(@NonNull final V standardGSYVideoPlayer) {
        //设置全屏按键功能
        standardGSYVideoPlayer.startWindowFullscreen(mContext, false, false);
    }

    public interface OnVideoPlayStartClickListener<V extends StandardGSYVideoPlayer> {
        /***
         *
         * @param view
         * @param origin 原始点击事件
         * @param standardGSYVideoPlayer
         */
        void onVideoPlayStartClicked(View view, View.OnClickListener origin, @NonNull V standardGSYVideoPlayer);
    }

    protected OnVideoPlayStartClickListener mOnStartClickListener;

    public void setOnStartClickListener(OnVideoPlayStartClickListener<V> listener) {
        mOnStartClickListener = listener;
    }

    public abstract void initVideoOption(@NonNull GSYVideoOptionBuilder gsyVideoOptionBuilder, ViewHolder holder, T t, int position);

    public void initDefaultVideoOption(@NonNull String url, String title, V gsyVideoPlayer, View thumbImageView, int position) {
        //防止错位，离开释放
        gsyVideoPlayer.initUIState();
        if (thumbImageView != null) {
            mGsyVideoOptionBuilder.setThumbImageView(thumbImageView);
        }
        if (TextUtils.isEmpty(title)) {
            mGsyVideoOptionBuilder.setVideoTitle(title);
        }
        mGsyVideoOptionBuilder.setNeedLockFull(true).setCacheWithPlay(true).setAutoFullWithSize(true).setShowFullAnimation(true).setLockLand(true).setPlayPosition(position).setPlayTag(TAG).setUrl(url).build(gsyVideoPlayer);
    }

    protected abstract @NonNull
    V getGsyVideoPlayer(@NonNull ViewHolder holder);


    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        super.onEnterFullscreen(url, objects);
    }

    public void a(View view,Object realObj,Method realMethod){
        try {
            //反射获得Button的setOnclicListen方法(参数1:方法名;2:方法参数类)
            Method setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
            //获得真实回调的方法(后期可以用注解获得这里hardCode了)
            Method realClick=this.getClass().getMethod("realClick",View.class);
            //创建一个代理
            ProxyHandle handle = new ProxyHandle(this, realClick);
            //设置代理 返回的是对应的接口 (OnClickListener实例对象)
            Object proxyObj = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(), new Class[]{View.OnClickListener.class}, handle);

            //调用对应View的设置监听方法 之后会到代理handle里去(对象可以用注解获得这里暂不分析)
            setOnClickListener.invoke(view,proxyObj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
