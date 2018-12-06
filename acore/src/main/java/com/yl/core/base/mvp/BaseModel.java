package com.yl.core.base.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.yl.core.integration.IRepositoryManager;
import com.yl.core.integration.RepositoryManager;

/**
 * Created by 寻水的鱼 on 2018/8/12.
 */
public abstract class BaseModel implements IModel, LifecycleObserver {
    protected IRepositoryManager mRepositoryManager; //用于管理网络请求层, 以及数据缓存层

    public BaseModel(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager == null ? RepositoryManager.getInstance() : repositoryManager;
    }

    public BaseModel() {
        this.mRepositoryManager = RepositoryManager.getInstance();
    }

    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
