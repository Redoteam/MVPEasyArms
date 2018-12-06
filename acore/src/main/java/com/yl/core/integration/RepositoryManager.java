/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yl.core.integration;

import android.content.Context;
import android.support.annotation.Nullable;

import com.yl.core.base.mvp.IModel;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.integration.cache.Cache;
import com.yl.core.integration.cache.CacheType;
import com.yl.core.utils.Preconditions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * ================================================
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 {@link IModel} 层必要的 Api 做数据处理
 * <p>
 * ================================================
 */
public class RepositoryManager implements IRepositoryManager {


    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;


    private RepositoryManager() {
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    @Override
    public synchronized <T> T obtainRetrofitService(Class<T> serviceClass) {
        return createWrapperService(serviceClass);
    }

    /**
     * 根据 https://zhuanlan.zhihu.com/p/40097338 对 Retrofit 进行的优化
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    private <T> T createWrapperService(Class<T> serviceClass) {
        // 通过二次代理，对 Retrofit 代理方法的调用包进新的 Observable 里在 io 线程执行。
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
                if (method.getReturnType() == Observable.class) {
                    // 如果方法返回值是 Observable 的话，则包一层再返回
                    return Observable.defer(() -> {
                        final T service = getRetrofitService(serviceClass);
                        // 执行真正的 Retrofit 动态代理的方法
                        return ((Observable) getRetrofitMethod(service, method).invoke(service, args)).subscribeOn(Schedulers.io());
                    }).subscribeOn(Schedulers.single());
                }
                // 返回值不是 Observable 的话不处理
                final T service = getRetrofitService(serviceClass);
                return getRetrofitMethod(service, method).invoke(service, args);
            }
        });
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param serviceClass ApiService class
     * @param <T>          ApiService class
     * @return ApiService
     */
    private <T> T getRetrofitService(Class<T> serviceClass) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = AppDelegate.getInstance().provideCacheFactory().build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(serviceClass.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = AppDelegate.getInstance().provideRetrofit().create(serviceClass);
            mRetrofitServiceCache.put(serviceClass.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    private <T> Method getRetrofitMethod(T service, Method method) throws NoSuchMethodException {
        return service.getClass().getMethod(method.getName(), method.getParameterTypes());
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cacheClass Cache class
     * @param <T>        Cache class
     * @return Cache
     */
    @Override
    public synchronized <T> T obtainCacheService(Class<T> cacheClass) {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = AppDelegate.getInstance().provideCacheFactory().build(CacheType.CACHE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mCacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cacheClass.getCanonicalName());
        if (cacheService == null) {
            cacheService = AppDelegate.getInstance().provideRxCache().using(cacheClass);
            mCacheServiceCache.put(cacheClass.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        AppDelegate.getInstance().provideRxCache().evictAll().subscribe();
    }

    @Override
    public Context getApplicationContext() {
        return AppDelegate.getInstance().getmApplication();
    }

    private static class RepositoryManagerHolder {
        private static final RepositoryManager instance = new RepositoryManager();

    }

    public static RepositoryManager getInstance() {
        return RepositoryManagerHolder.instance;
    }
}
