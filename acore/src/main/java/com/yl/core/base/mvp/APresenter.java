package com.yl.core.base.mvp;

import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.view.View;

import com.trello.rxlifecycle2.RxLifecycle;
import com.yl.core.http.RxManager;
import com.yl.core.http.RxSubscriber;
import com.yl.core.utils.Preconditions;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * Created by 寻水的鱼 on 2018/8/1.
 */
public abstract class APresenter<V extends IView, M extends IModel> implements IPresenter, LifecycleObserver {
    protected WeakReference<V> mView;
    protected M mModel;

    public RxManager getRxManager() {
        if (mRxManager == null) {
            mRxManager = new RxManager();
        }
        return mRxManager;
    }

    protected RxManager mRxManager;

    public APresenter(V view) {
        Preconditions.checkNotNull(view, "%s cannot be null", IView.class.getName());
        this.mView = new WeakReference<V>(view);
        onStart();
    }

    public APresenter(V view, M model) {
        Preconditions.checkNotNull(model, "%s cannot be null", IModel.class.getName());
        Preconditions.checkNotNull(view, "%s cannot be null", IView.class.getName());
        this.mView = new WeakReference<V>(view);
        this.mModel = model;
        onStart();
    }

    @Override
    public void onStart() {
        if (mView.get() != null && mView.get() instanceof LifecycleOwner) {
            ((LifecycleOwner) mView.get()).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver) {
                ((LifecycleOwner) mView.get()).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
    }


    @Override
    public void onDestroy() {
        unDispose();
        if (mView != null) {
            V v = mView.get();
            v = null;
        }
        mView = null;
        if (mModel != null) {
            mModel.onDestroy();
        }
        mModel = null;
    }

    /**
     * 只有当 {@code mRootView} 不为 null, 并且 {@code mRootView} 实现了 {@link LifecycleOwner} 时, 此方法才会被调用
     * 所以当您想在 {@link Service} 以及一些自定义 {@link View} 或自定义类中使用 {@code Presenter} 时
     * 您也将不能继续使用 {@link OnLifecycleEvent} 绑定生命周期
     *
     * @param owner link {@link SupportActivity} and {@link Fragment}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        /**
         * 注意, 如果在这里调用了 {@link #onDestroy()} 方法, 会出现某些地方引用 {@code mModel} 或 {@code mRootView} 为 null 的情况
         * 比如在 {@link RxLifecycle} 终止 {@link Observable} 时, 在 {@link io.reactivex.Observable#doFinally(Action)} 中却引用了 {@code mRootView} 做一些释放资源的操作, 此时会空指针
         * 或者如果你声明了多个 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) 时在其他 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
         * 中引用了 {@code mModel} 或 {@code mRootView} 也可能会出现此情况
         */
        owner.getLifecycle().removeObserver(this);
    }


    public void addDispose(Disposable disposable) {
        getRxManager().add(disposable);//将所有 Disposable 放入集中处理
    }

    public void addDispose(RxSubscriber disposable) {
        getRxManager().add(disposable);//将所有 Disposable 放入集中处理
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        if (mRxManager != null) {
            mRxManager.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }
}
