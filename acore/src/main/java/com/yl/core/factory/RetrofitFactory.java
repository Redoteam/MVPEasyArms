package com.yl.core.factory;

import android.app.Application;
import com.google.gson.Gson;
import com.yl.core.integration.GlobalConfigModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 寻水的鱼 on 2018/8/12.
 */
public class RetrofitFactory {
    public static Retrofit provideRetrofit(Application application, GlobalConfigModule.RetrofitConfiguration configuration, Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl, Converter.Factory gsonConverterFactory, Gson gson) {
        builder.baseUrl(httpUrl).client(client);//设置okhttp
        if (configuration != null)
            configuration.configRetrofit(application, builder);
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());//使用 Rxjava
        /**
         * addConverterFactory 可以添加多个，框架会默认添加GsonConverterFactory
         * 如果有自定义的ConverterFactory ，会先添加自定义的，
         * 先添加的优先匹配
         */
        if (gsonConverterFactory != null) {
            builder.addConverterFactory(gsonConverterFactory);
        }
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

}
