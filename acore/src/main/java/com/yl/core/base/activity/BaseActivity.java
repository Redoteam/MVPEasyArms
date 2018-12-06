package com.yl.core.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.InflateException;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yl.core.base.mvp.IPresenter;
import com.yl.core.base.mvp.IView;
import com.yl.core.delegate.IActivity;
import com.yl.core.integration.AppManager;
import com.yl.core.integration.lifecycle.ActivityLifecycleable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView,
        IActivity, ActivityLifecycleable {
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    protected P mPresenter;
    private Unbinder mUnbinder;

    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        try {
            int layoutResID = getLayoutId(savedInstanceState);
            if (layoutResID != 0) {
                setContentView(layoutResID);
                mUnbinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            if (e instanceof InflateException) throw e;
            e.printStackTrace();
        }
        mPresenter = initPresenter();
        initUI(savedInstanceState);
        initData(savedInstanceState);
    }


    @Override
    public abstract P initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY){
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
        if (mPresenter != null){
            mPresenter.onDestroy();//释放资源
        }
        this.mPresenter = null;
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showError(String code, String messaege) {
        Toast.makeText(this, messaege, Toast.LENGTH_SHORT).show();
    }
}
