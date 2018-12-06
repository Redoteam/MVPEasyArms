package com.yl.core.factory;

import android.app.Application;
import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.yl.core.integration.GlobalConfigModule;
import java.io.File;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by 寻水的鱼 on 2018/11/6.
 */
public class RxCacheFactory {
    public static RxCache provideRxCache(Application application, @Nullable GlobalConfigModule.RxCacheConfiguration configuration, File cacheDirectory, Gson gson) {
        RxCache.Builder builder = new RxCache.Builder();
        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder);
        }
        if (rxCache != null)
            return rxCache;
        return builder.useExpiredDataIfLoaderNotAvailable(true)   //RxCache提供被驱逐的数据
                .persistence(cacheDirectory, new GsonSpeaker(gson));
    }
}
