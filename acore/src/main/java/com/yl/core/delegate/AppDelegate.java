package com.yl.core.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yl.core.factory.SingletonImageLoader;
import com.yl.core.factory.SingletonOKHttpClient;
import com.yl.core.factory.SingletonRetrofit;
import com.yl.core.factory.SingletonRxCache;
import com.yl.core.http.imageloader.BaseImageLoaderStrategy;
import com.yl.core.http.imageloader.ImageLoader;
import com.yl.core.integration.ConfigModule;
import com.yl.core.integration.GlobalConfigModule;
import com.yl.core.integration.ManifestParser;
import com.yl.core.integration.cache.Cache;
import com.yl.core.utils.DataHelper;
import com.yl.core.utils.TimberUtil;

import java.io.File;
import java.util.List;

import io.rx_cache2.internal.RxCache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by 寻水的鱼 on 2018/8/12.
 */
public class AppDelegate implements AppLifecycles {
    private List<ConfigModule> mModules;
    private Application mApplication;
    private GlobalConfigModule mGlobalConfigModule;
    private SingletonRetrofit singletonRetrofit;
    private SingletonOKHttpClient singletonOKHttpClient;
    private SingletonRxCache singletonRxCache;
    private Cache.Factory cacheFactory;
    private File cacheFile;
    private SingletonImageLoader singletonImageLoader;

    private AppDelegate() {
    }

    public static AppDelegate getInstance() {

        return AppDelegateHolder.instance;
    }

    private static class AppDelegateHolder {
        private static final AppDelegate instance = new AppDelegate();
    }


    @Override
    public void attachBaseContext(@NonNull Context base) {
        this.mModules = new ManifestParser(base).parse();
    }

    @Override
    public void onCreate(@NonNull Application application) {
        mApplication = application;
        // GlobalConfigModule 的优先级最高，必须第一个初始化
        mGlobalConfigModule = getGlobalConfigModule(application, mModules);
        //初始化在 getGlobalConfigModule() 之后
        initialization(mGlobalConfigModule);

        //设置log自动在apk为debug版本时打开，在release版本时关闭
        TimberUtil.setLogAuto();
        //也可以设置log一直开
        //TimberUtil.setLogDebug();

    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return GlobalConfigModule
     */
    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();
        //遍历 ConfigModule 集合, 给全局配置 GlobalConfigModule 添加参数
        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }
        return builder.build();
    }

    public Application getmApplication() {
        return mApplication;
    }

    public GlobalConfigModule getmGlobalConfigModule() {
        return mGlobalConfigModule;
    }


    /***
     * 与GlobalConfigModule相关初始化配置
     * @param globalConfigModule
     */
    private void initialization(GlobalConfigModule globalConfigModule) {
        cacheFile = globalConfigModule.provideCacheFile(mApplication);
        singletonOKHttpClient = SingletonOKHttpClient.getInstance();
        singletonRetrofit = SingletonRetrofit.getInstance();
        singletonRxCache = SingletonRxCache.getInstance();
        cacheFactory = globalConfigModule.provideCacheFactory(mApplication);
        singletonImageLoader = SingletonImageLoader.getInstance();
        //暂定 todo 。。
        mGlobalConfigModule = null;
    }


    public Retrofit provideRetrofit() {
        return singletonRetrofit.getRetrofit();
    }

    public OkHttpClient provideOkHttpClient() {
        return singletonOKHttpClient.getOkHttpClient();
    }

    public RxCache provideRxCache() {
        return singletonRxCache.getRxCache();
    }

    public Cache.Factory provideCacheFactory() {
        return cacheFactory;
    }


    public ImageLoader provideImageLoader(){
        return singletonImageLoader.getImageLoader();
    }

    public File provideCacheFile() {
        return cacheFile == null ? DataHelper.getCacheFile(mApplication) : cacheFile;
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */

    public synchronized <T> T obtainRetrofitService(Class<T> service) {
        return singletonRetrofit.getRetrofit().create(service);
    }

}
