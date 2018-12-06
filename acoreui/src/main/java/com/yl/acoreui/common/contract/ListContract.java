package com.yl.acoreui.common.contract;

import com.yl.core.base.mvp.APresenter;
import com.yl.core.base.mvp.IModel;
import com.yl.core.base.mvp.IView;
import com.yl.core.http.RxSubscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by 寻水的鱼 on 2018/10/25.
 */
public interface ListContract {
    interface IListView extends IView {
        void refreshError();

        void loadMoreError();

        void refreshSucceed();

        void loadMoreSucceed();

        void noMoreData(boolean isRefresh);

        void upDataList();
    }

    abstract class AListPresenter<V extends IListView, M extends IModel, D> extends APresenter<V, M> {
        public int PAGE_SIZE = 20;
        protected int mPage = 1;
        protected boolean isRefresh = true;
        protected List<D> mData = new ArrayList<>();

        public AListPresenter(V view) {
            super(view);
        }

        public AListPresenter(V view, M model) {
            super(view, model);
        }

        public List<D> getRvDataList() {
            return mData;
        }

        public void addRvDataList(List<D> data) {
            if (isRefresh) {
                mData.clear();
                mData.addAll(data);
                mView.get().refreshSucceed();
            } else {
//                int endIndex = mData.size();
                if (data == null || data.size() == 0) {
                    mView.get().noMoreData(isRefresh);
                } else {
                    mData.addAll(data);
                    if (data.size() < PAGE_SIZE) {
                        mView.get().noMoreData(isRefresh);
                    } else {
                        mView.get().loadMoreSucceed();
                    }
                }

            }
        }

        public void addRvDataList(Observable<List<D>> observable) {
            addDispose(new RxSubscriber<List<D>>(observable) {
                @Override
                protected void _onNext(List<D> data) {
                   /* if(data == null || data.size() == 0){
                        mView.get().noMoreData(isRefresh);
                    }else {
                        addRvDataList(data);
                    }*/
                    addRvDataList(data);
                }

                @Override
                protected void _onError(String code, String msg) {
                    super._onError(code, msg);
                    if (isRefresh) {
                        mView.get().refreshError();
                    } else {
                        mView.get().loadMoreError();
                    }
                }
            });
        }

        public void onRefresh() {
            isRefresh = true;
        }

        public void onLoadMore() {
            isRefresh = false;
        }

        public int getPage() {
            if (isRefresh) {
                return 1;
            } else {
                return mPage + 1;
            }
        }

        public void setPageSize(int size) {
            PAGE_SIZE = size;
        }

        public int getPageSize() {
            return PAGE_SIZE;
        }
    }
}
