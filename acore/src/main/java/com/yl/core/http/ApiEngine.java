package com.yl.core.http;

import android.app.Application;
import android.support.annotation.DrawableRes;

import com.google.gson.Gson;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.factory.OkHttpClientFactory;
import com.yl.core.factory.RetrofitFactory;
import com.yl.core.integration.GlobalConfigModule;
import com.yl.core.utils.Preconditions;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by 寻水的鱼 on 2018/8/12.
 */
@Deprecated
public class ApiEngine {
    private volatile static ApiEngine apiEngine;

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private final OkHttpClient okHttpClient;
    private Retrofit retrofit;

    public static void initialization() {
        getInstance();
    }

    private static GlobalConfigModule mGlobalConfigModule;

    private ApiEngine() {
        //日志拦截器
        /*HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
        mGlobalConfigModule = AppDelegate.getInstance().getmGlobalConfigModule();
        mGlobalConfigModule = Preconditions.checkNotNull(mGlobalConfigModule, GlobalConfigModule.class.getCanonicalName() + "can not be null.");
        Application application = AppDelegate.getInstance().getmApplication();
        okHttpClient = OkHttpClientFactory.provideClient(application, mGlobalConfigModule);
        retrofit = RetrofitFactory.provideRetrofit(application, mGlobalConfigModule.provideRetrofitConfiguration(),
                new Retrofit.Builder(), okHttpClient, mGlobalConfigModule.provideBaseUrl(), mGlobalConfigModule.provideGsonConverterFactory(), new Gson());
    }

    public static ApiEngine getInstance() {
        if (apiEngine == null) {
            synchronized (ApiEngine.class) {
                if (apiEngine == null) {
                    apiEngine = new ApiEngine();
                }
            }
        }
        return apiEngine;
    }


    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     * @param service
     * @param <T>
     * @return
     */
    public synchronized <T> T obtainRetrofitService(Class<T> service) {
        return retrofit.create(service);
    }
}

