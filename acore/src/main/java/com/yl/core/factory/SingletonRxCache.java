package com.yl.core.factory;

import com.google.gson.Gson;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.integration.GlobalConfigModule;

import io.rx_cache2.internal.RxCache;

/**
 * Created by 寻水的鱼 on 2018/11/6.
 */
public class SingletonRxCache {
    private final RxCache rxCache;

    private SingletonRxCache() {
        AppDelegate instance = AppDelegate.getInstance();
        GlobalConfigModule globalConfigModule = instance.getmGlobalConfigModule();
        rxCache = RxCacheFactory.provideRxCache(instance.getmApplication(), globalConfigModule.provideRxCacheConfiguration(), globalConfigModule.provideCacheFile(instance.getmApplication()), new Gson());
    }

    public static SingletonRxCache getInstance() {
        return SingletonRxCacheHolder.instance;
    }

    private static class SingletonRxCacheHolder {
        private static final SingletonRxCache instance = new SingletonRxCache();
    }

    public RxCache getRxCache() {
        return rxCache;
    }
}
