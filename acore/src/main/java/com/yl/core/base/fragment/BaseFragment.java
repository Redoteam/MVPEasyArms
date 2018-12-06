package com.yl.core.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.FragmentEvent;
import com.yl.core.base.mvp.IPresenter;
import com.yl.core.base.mvp.IView;
import com.yl.core.delegate.IFragment;
import com.yl.core.integration.lifecycle.FragmentLifecycleable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by 寻水的鱼 on 2018/8/27.
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, IView, FragmentLifecycleable {
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();

    protected P mPresenter;
    private Unbinder mUnbinder;
    protected View mRootView;

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = View.inflate(getActivity(), getLayoutId(savedInstanceState), null);
        mPresenter = initPresenter();
        mUnbinder = ButterKnife.bind(this, mRootView);
        initUI(savedInstanceState);
        initData(savedInstanceState);
        return mRootView;
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    public void setFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    @Override
    public P initPresenter() {
        return null;
    }

    @Override
    public void showError(String code, String messaege) {
        Toast.makeText(getActivity(), messaege, Toast.LENGTH_SHORT).show();
    }
}
