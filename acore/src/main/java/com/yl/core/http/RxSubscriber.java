package com.yl.core.http;

import android.widget.Toast;

import com.yl.core.base.mvp.IView;
import com.yl.core.delegate.AppDelegate;
import com.yl.core.http.handler.IHttpError;
import com.yl.core.http.handler.UnknownError;
import com.yl.core.integration.lifecycle.ActivityLifecycleable;
import com.yl.core.integration.lifecycle.FragmentLifecycleable;
import com.yl.core.utils.RxLifecycleUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class RxSubscriber<D> {
    private static final String TAG = "RxSubscriber";
    private Disposable mSubscribe;

    public RxSubscriber(Observable<D> observable) {
        subscribe(observable, null);
    }

    public RxSubscriber(Observable<D> observable, IView view) {
        subscribe(observable, view);
    }

    private void subscribe(Observable<D> observable, final IView view) {
        Observable<D> dObservable = observable.switchIfEmpty(new ObservableSource<D>() {
            @Override
            public void subscribe(Observer<? super D> observer) {
                String code = HttpCode.CODE_30001.getCode();
                _onError(code, HttpCode.getMsg(code));
            }
        }).doFinally(() -> {
            if (view != null) {
                view.hideLoading();
            }
        });
        Observable<D> compose;
        if (view != null && (view instanceof ActivityLifecycleable || view instanceof FragmentLifecycleable)) {
            compose = dObservable.compose(RxLifecycleUtils.bindToLifecycle(view));
        } else {
            compose = dObservable;
        }
        mSubscribe = compose.subscribe(d -> {

            if (d != null) {
                _onNext(d);
            } else {
                String code = HttpCode.CODE_30002.getCode();
                _onError(code, HttpCode.getMsg(code));
            }
        }, throwable -> {
            if (view != null) {
                view.hideLoading();
            }
            throwable.printStackTrace();
            IHttpError iHttpError = HttpCode.httpCodeMap.get(throwable.getClass().getCanonicalName());
            if (iHttpError == null) {
                iHttpError = new UnknownError();
            }
            HttpCode httCode = iHttpError.getHttCode(throwable.getMessage());
            _onError(httCode.getCode(), httCode.getMsg());
            /*if (throwable instanceof HttpException
                    || throwable instanceof CompositeException
                    || throwable instanceof SocketTimeoutException
                    || throwable instanceof NoRouteToHostException
                    || throwable instanceof ConnectException
                    || throwable instanceof UnknownHostException) {
                String canonicalName = throwable.getClass().getCanonicalName();
                String code = HttpCode.CODE_30001.getCode();
                _onError(code, HttpCode.getMsg(code));
            } else if (throwable instanceof ApiException) {
                _onError(throwable.getMessage(), HttpCode.getMsg(throwable.getMessage()));
            } else if (throwable instanceof JsonException) {
                String code = HttpCode.CODE_30003.getCode();
                _onError(code, HttpCode.getMsg(code));
            }else if(throwable instanceof TokenInvalidException){
                String code = HttpCode.CODE_20005.getCode();
                _onError(code, HttpCode.getMsg(code));
            }
            else {
//                    KLog.d(throwable.getMessage());
                String code = HttpCode.CODE_30001.getCode();
                _onError(code, HttpCode.getMsg(code));
            }*/
        });
    }

    public Disposable getSubscribe() {
        return mSubscribe;
    }

    protected abstract void _onNext(D d);

    protected void _onError(String code, String msg) {
        Toast.makeText(AppDelegate.getInstance().getmApplication(), msg, Toast.LENGTH_SHORT).show();
    }
}
