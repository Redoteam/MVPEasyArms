package com.yl.core.factory;

import com.yl.core.delegate.AppDelegate;
import com.yl.core.integration.GlobalConfigModule;

import okhttp3.OkHttpClient;

/**
 * Created by 寻水的鱼 on 2018/11/6.
 */
public class SingletonOKHttpClient {
    public OkHttpClient okHttpClient;

    private SingletonOKHttpClient() {
        GlobalConfigModule globalConfigModule = AppDelegate.getInstance().getmGlobalConfigModule();
        okHttpClient = OkHttpClientFactory.provideClient(AppDelegate.getInstance().getmApplication(), globalConfigModule);
    }

    public static SingletonOKHttpClient getInstance() {
        return SingletonOKHttpClientHolder.instance;
    }

    private static class SingletonOKHttpClientHolder {
        private static final SingletonOKHttpClient instance = new SingletonOKHttpClient();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
