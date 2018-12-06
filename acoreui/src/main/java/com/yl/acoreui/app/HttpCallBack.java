package com.yl.acoreui.app;

/**
 * Created by 寻水的鱼 on 2018/8/27.
 */
public interface HttpCallBack<T> {
    void onSucceed(T data);
    void onError(String msg);

    abstract class Empty<T> implements HttpCallBack<T> {
        @Override
        public void onSucceed(T data) {
        }

        @Override
        public void onError(String msg) {
        }
    }
}
