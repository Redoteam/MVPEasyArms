package com.yl.acoreui.utils;

import android.content.Context;
import android.widget.ImageView;

import com.yl.acoreui.R;
import com.yl.core.factory.SingletonImageLoader;
import com.yl.core.http.imageloader.ImageConfig;
import com.yl.core.http.imageloader.glide.ImageConfigImpl;

/**
 * Created by 寻水的鱼 on 2018/12/5.
 */
public class ImageUtils {

    public static <T extends ImageConfig> void loadImage(Context context,T t){
        SingletonImageLoader.getInstance().getImageLoader().loadImage(context, t);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        ImageConfigImpl build = ImageConfigImpl.builder()
                .url(url)
                .cacheStrategy(4)
                .placeholder(R.mipmap.image_load)
                .errorPic(R.mipmap.image_error)
                .skipMemoryCache(false)
                .imageView(imageView)
                .isCrossFade(true)
                .skipMemoryCache(false)
                .build();
        SingletonImageLoader.getInstance().getImageLoader().loadImage(context, build);
    }
    /**
     * @param context
     * @param url
     * @param imageRadius 圆角大小
     * @param imageView
     */
    public static void loadRoundedImage(Context context, String url, int imageRadius, ImageView imageView) {
        ImageConfigImpl build = ImageConfigImpl.builder().url(url).cacheStrategy(4)
//                .placeholder(R.mipmap.image_load)
//                .errorPic(R.mipmap.image_error)
                .skipMemoryCache(true)
                .imageView(imageView)
                .isCrossFade(true)
                .imageRadius(imageRadius)
                .build();
        SingletonImageLoader.getInstance().getImageLoader().loadImage(context, build);
    }


    public static void loadRoundedImage(Context context, String url, CornerTransform cornerTransform, ImageView imageView) {

        ImageConfigImpl build = ImageConfigImpl.builder().url(url).cacheStrategy(4)
//                .placeholder(R.mipmap.image_load)
//                .errorPic(R.mipmap.image_error)
                .skipMemoryCache(false)
                .imageView(imageView)
                .isCrossFade(false)
                .transformation(cornerTransform)
                .build();
        SingletonImageLoader.getInstance().getImageLoader().loadImage(context, build);

    }
}
