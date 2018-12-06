package com.yl.core.factory;

import android.app.Application;
import android.support.annotation.Nullable;

import com.yl.core.http.GlobalHttpHandler;
import com.yl.core.http.log.DefaultFormatPrinter;
import com.yl.core.http.log.RequestInterceptor;
import com.yl.core.integration.GlobalConfigModule;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


/**
 * Created by 寻水的鱼 on 2018/8/12.
 */
public class OkHttpClientFactory {
    private static final int TIME_OUT = 10;

    public static OkHttpClient provideClient(Application application, OkHttpClient.Builder builder, Interceptor intercept, @Nullable List<Interceptor> interceptors, @Nullable final GlobalHttpHandler handler) {
        OkHttpClient.Builder with = RetrofitUrlManager.getInstance().with(builder);
        with.connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS).addNetworkInterceptor(intercept);
        if (handler != null)
            with.addInterceptor(chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())));
        if (interceptors != null) {
            //如果外部提供了interceptor的集合则遍历添加
            for (Interceptor interceptor : interceptors) {
                with.addInterceptor(interceptor);
            }
        }

        return with.build();
    }

    public static OkHttpClient provideClient(Application application, GlobalConfigModule globalConfigModule) {
        GlobalConfigModule.OkhttpConfiguration okhttpConfiguration = globalConfigModule.provideOkhttpConfiguration();
        OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder());
        RequestInterceptor requestInterceptor = new RequestInterceptor(globalConfigModule.provideGlobalHttpHandler(), globalConfigModule.provideFormatPrinter(), globalConfigModule.providePrintHttpLogLevel());

        okhttpConfiguration.configOkhttp(application, builder);
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS).addNetworkInterceptor(requestInterceptor);

        final GlobalHttpHandler globalHttpHandler = globalConfigModule.provideGlobalHttpHandler();
        if (globalHttpHandler != null) {
            builder.addInterceptor(chain -> chain.proceed(globalHttpHandler.onHttpRequestBefore(chain, chain.request())));
        }
        List<Interceptor> interceptors = globalConfigModule.provideInterceptors();
        if (interceptors != null) {
            //如果外部提供了interceptor的集合则遍历添加
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }

    public static OkHttpClient provideClient(Application application, GlobalConfigModule globalConfigModule, String logTag) {
        GlobalConfigModule.OkhttpConfiguration okhttpConfiguration = globalConfigModule.provideOkhttpConfiguration();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        RequestInterceptor requestInterceptor = new RequestInterceptor(globalConfigModule.provideGlobalHttpHandler(), new DefaultFormatPrinter(logTag), globalConfigModule.providePrintHttpLogLevel());

        okhttpConfiguration.configOkhttp(application, builder);
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS).addNetworkInterceptor(requestInterceptor);

        final GlobalHttpHandler globalHttpHandler = globalConfigModule.provideGlobalHttpHandler();
        if (globalHttpHandler != null) {
            builder.addInterceptor(chain -> chain.proceed(globalHttpHandler.onHttpRequestBefore(chain, chain.request())));
        }
        List<Interceptor> interceptors = globalConfigModule.provideInterceptors();
        if (interceptors != null) {
            //如果外部提供了interceptor的集合则遍历添加
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }
}
