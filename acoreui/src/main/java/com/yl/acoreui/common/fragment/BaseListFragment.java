package com.yl.acoreui.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yl.acoreui.R;
import com.yl.acoreui.common.adapter.EmptyWrapperAdapter;
import com.yl.acoreui.common.contract.ListContract;


/**
 * Created by 寻水的鱼 on 2018/10/25.
 */
public abstract class BaseListFragment<P extends ListContract.AListPresenter,A extends RecyclerView.Adapter> extends BaseFragment<P> implements ListContract.IListView, OnRefreshListener, OnLoadMoreListener {
    protected RecyclerView mRecyclerView;
    SmartRefreshLayout mRefreshLayout;
    protected RelativeLayout mEmptyView;
    private TextView mEmptyTv;
    protected EmptyWrapperAdapter mEmptyWrapperAdapter;
    protected A mMultiItemTypeAdapter;

    @Override
    public int getContainerLayout(@Nullable Bundle savedInstanceState) {
        return R.layout.layout_base_list;
    }

    @Override
    public void initUI(@Nullable Bundle savedInstanceState) {
        mRecyclerView = mRootView.findViewById(R.id.rv);
        mRefreshLayout = mRootView.findViewById(R.id.refresh);
        initRefreshHead(mRefreshLayout);
        initRefreshFoot(mRefreshLayout);

        mRecyclerView.setLayoutManager(getLayoutManager());
        initEmptyView();
        initRecyclerViewAdapter();
        initRefreshLayout();
    }

    public void initRefreshHead(SmartRefreshLayout refreshLayout) {
        refreshLayout.setRefreshHeader(new ClassicsHeader(requireContext()));
    }

    public void initRefreshFoot(SmartRefreshLayout refreshLayout) {
        refreshLayout.setRefreshFooter(new ClassicsFooter(requireContext()).setDrawableSize(20));
    }

    public void initRecyclerViewAdapter() {
        mMultiItemTypeAdapter = getRecyclerViewAdapter();
        mEmptyWrapperAdapter = new EmptyWrapperAdapter(mMultiItemTypeAdapter);
        mEmptyWrapperAdapter.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(mEmptyWrapperAdapter);
    }

    public void initEmptyView() {
        mEmptyView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_empty, mRecyclerView, false);
        mEmptyTv = mEmptyView.findViewById(R.id.tv_empty_text);
    }

    public void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableOverScrollDrag(true);
    }

    public abstract A getRecyclerViewAdapter();

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (mPresenter != null) {
            mPresenter.onRefresh();
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (mPresenter != null) {
            mPresenter.onLoadMore();
        }
    }

    @Override
    public void refreshError() {
        mRefreshLayout.finishRefresh(0, false);
        mEmptyTv.setText(getString(R.string.refresh_error));
    }

    @Override
    public void loadMoreError() {
        mRefreshLayout.finishLoadMore(0, false, false);
        mEmptyTv.setText(getString(R.string.load_error));
    }

    @Override
    public void refreshSucceed() {
        upDataList();
        mRefreshLayout.finishRefresh(0);
        mRefreshLayout.setNoMoreData(false);
    }

    @Override
    public void loadMoreSucceed() {
        upDataList();
        mRefreshLayout.finishLoadMore(3000, true, false);
    }

    @Override
    public void noMoreData(boolean isRefresh) {
        if (isRefresh) {
            mRefreshLayout.finishRefresh(0);
            mEmptyTv.setText(getString(R.string.no_more_data));
        } else {
            upDataList();
            mRefreshLayout.finishLoadMore(1000, true, true);
        }
    }

    /**
     * 更新列表
     */
    @Override
    public void upDataList() {
        mEmptyWrapperAdapter.notifyDataSetChanged();
    }
}
