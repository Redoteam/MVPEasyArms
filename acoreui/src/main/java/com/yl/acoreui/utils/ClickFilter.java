package com.yl.acoreui.utils;

import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by 寻水的鱼 on 2018/11/16.
 */
public class ClickFilter {
    public static void setFilter(View view) {
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Class listInfoType = field.getType();
            Object listinfo = field.get(view);
            Field onclickField = listInfoType.getField("mOnClickListener");
            View.OnClickListener origin = (View.OnClickListener) onclickField.get(listinfo);
            onclickField.set(listinfo, new ClickProxy(origin));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFilter(View view, ClickProxy.InterceptClickListener listener) {
        try {
            Field field = View.class.getDeclaredField("mListenerInfo");
            field.setAccessible(true);
            Class listInfoType = field.getType();
            Object listinfo = field.get(view);
            Field onclickField = listInfoType.getField("mOnClickListener");
            View.OnClickListener origin = (View.OnClickListener) onclickField.get(listinfo);
            onclickField.set(listinfo, new ClickProxy(origin, listener));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
