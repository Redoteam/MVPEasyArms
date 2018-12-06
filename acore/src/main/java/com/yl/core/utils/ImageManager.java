package com.yl.core.utils;

import android.content.Context;
import android.widget.ImageView;

import com.yl.core.R;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.factory.SingletonImageLoader;
import com.yl.core.http.imageloader.ImageConfig;
import com.yl.core.http.imageloader.ImageLoader;

/**
 * Created by 寻水的鱼 on 2018/8/13.
 */
public class ImageManager {


    /**
     * 加载图片
     *
     * @param context
     * @param config
     * @param <T>
     */
    public static <T extends ImageConfig> void loadImage(Context context, T config) {
        ImageLoader imageLoader = SingletonImageLoader.getInstance().getImageLoader();
        imageLoader.loadImage(context, config);
    }

    /**
     * 停止加载或清理缓存
     *
     * @param context
     * @param config
     * @param <T>
     */
    public static <T extends ImageConfig> void clear(Context context, T config) {
        ImageLoader imageLoader = SingletonImageLoader.getInstance().getImageLoader();
        imageLoader.clear(context, config);
    }

}
