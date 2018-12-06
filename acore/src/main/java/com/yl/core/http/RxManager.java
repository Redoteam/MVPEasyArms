package com.yl.core.http;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Yang Shihao
 */
public class RxManager {
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public void add(RxSubscriber rxSubscriber) {
        Disposable subscribe = rxSubscriber.getSubscribe();
        if (subscribe != null && !subscribe.isDisposed()) {
            mDisposable.add(subscribe);
        }
    }

    public void add(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            mDisposable.add(disposable);
        }
    }

    public void clear() {
        if (!mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
