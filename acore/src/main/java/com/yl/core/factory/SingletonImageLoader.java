package com.yl.core.factory;

import com.yl.core.delegate.AppDelegate;
import com.yl.core.http.imageloader.BaseImageLoaderStrategy;
import com.yl.core.http.imageloader.ImageLoader;

/**
 * Created by 寻水的鱼 on 2018/12/5.
 */
public class SingletonImageLoader {

    private ImageLoader imageLoader;

    public SingletonImageLoader() {
        imageLoader = new ImageLoader();
        BaseImageLoaderStrategy strategy = AppDelegate.getInstance().getmGlobalConfigModule().provideImageLoaderStrategy();
        imageLoader.setLoadImgStrategy(strategy);
    }

    private static class SingletonImageLoaderStrategyHolder {
        private static final SingletonImageLoader instance = new SingletonImageLoader();
    }

    public static SingletonImageLoader getInstance() {
        return SingletonImageLoaderStrategyHolder.instance;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
