package com.yl.core.factory;

import com.google.gson.Gson;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.integration.GlobalConfigModule;

import retrofit2.Retrofit;

/**
 * Created by 寻水的鱼 on 2018/11/6.
 */
public class SingletonRetrofit {
    Retrofit retrofit;

    private SingletonRetrofit() {
        AppDelegate instance = AppDelegate.getInstance();
        GlobalConfigModule mGlobalConfigModule = AppDelegate.getInstance().getmGlobalConfigModule();
        Gson gson = mGlobalConfigModule.provideGson(instance.getmApplication(), mGlobalConfigModule.provideGsonConfiguration());
        retrofit = RetrofitFactory.provideRetrofit(instance.getmApplication(), mGlobalConfigModule.provideRetrofitConfiguration(), new Retrofit.Builder(), AppDelegate.getInstance().provideOkHttpClient(), mGlobalConfigModule.provideBaseUrl(), mGlobalConfigModule.provideGsonConverterFactory(), gson);
    }

    private static class SingletonRetrofitHolder {
        private static final SingletonRetrofit instance = new SingletonRetrofit();
    }

    public static SingletonRetrofit getInstance() {
        return SingletonRetrofitHolder.instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
