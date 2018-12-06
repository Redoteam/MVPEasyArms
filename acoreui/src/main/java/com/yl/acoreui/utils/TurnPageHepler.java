package com.yl.acoreui.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/9/4.
 */
public abstract class TurnPageHepler<D> {
    private List<D> mSourceData = new ArrayList<>();

    public List<D> getShowData() {
        return showData;
    }

    private List<D> showData = new ArrayList<>();

    private int totalPage = 1;

    private int onePageSum = 5 * 9;

    private int startIndex = 0;

    private int page = 1;

    private boolean isHasMore = true;

    /***
     * 左翻页
     */
    public void turnLeft() {
        if (page == 1) {
            return;
        }
        page--;
        upDataPageNum(page, totalPage);
        initShowData(true);
    }

    /***
     * 右翻页
     */
    public void turnRight() {
        if (page == totalPage) {
            if (mOnLoadmoreLisitener != null) {
                mOnLoadmoreLisitener.onLoadmore(isHasMore);
            }
            return;
        }
        page++;
        upDataPageNum(page, totalPage);
        initShowData(false);
    }

    private void initShowData(boolean isLeft) {
        showData.clear();
        if (isLeft) {
            startIndex = onePageSum * page - onePageSum;
        } else {
            startIndex = onePageSum * (page - 1);
        }
        List<D> ds = mSourceData.subList(startIndex, mSourceData.size());
        showData.addAll(ds);
      /*  for (int i = startIndex; i < mSourceData.size(); i++) {
            if (i < onePageSum * page) {
                showData.add(mSourceData.get(i));
            }
        }*/
        notifyDataSetChange(showData);
    }

    public void setSourceData(List<D> sourceData) {
        this.mSourceData = sourceData;
        totalPage = 0;
        page = 1;
        int size = mSourceData.size();
        if (size == 0) {
            totalPage = 0;
            page = 0;
        } else if (size % onePageSum == 0) {
            totalPage = size / onePageSum;
        } else {
            totalPage = size / onePageSum + 1;
        }
        upDataPageNum(page, totalPage);
        initShowData(false);
    }


    public void addSourceData(List<D> addData) {
        this.mSourceData.addAll(addData);
        int size = mSourceData.size();
        if (size == 0) {
            return;
        } else if (size % onePageSum == 0) {
            totalPage = size / onePageSum;
        } else {
            totalPage = size / onePageSum + 1;
        }
        int showDataSize = showData.size();
        if (showDataSize == onePageSum) {
            page++;
            upDataPageNum(page, totalPage);
            initShowData(false);
        } else {
//            upDataPageNum(page,totalPage);
            for (int i = 0; i < onePageSum - showDataSize; i++) {
                showData.add(addData.get(i));
            }
            notifyDataSetChange(showData);
        }
    }

    public void setNoMore() {
        isHasMore = false;
    }

    public List<D> getSourceData() {
        return mSourceData;
    }

    /***
     *
     * @param onePageSum 设置一页共有多少个item
     */
    public void setOnePageSum(int onePageSum) {
        this.onePageSum = onePageSum;
    }

    /***
     * 对外提供数据，以便更新页数
     * @param page
     * @param totalPage
     */
    public abstract void upDataPageNum(int page, int totalPage);

    public abstract void notifyDataSetChange(List<D> showData);


    public void setOnLoadmoreLisitener(OnLoadmoreLisitener mOnLoadmoreLisitener) {
        this.mOnLoadmoreLisitener = mOnLoadmoreLisitener;
    }

    private OnLoadmoreLisitener mOnLoadmoreLisitener;

    public interface OnLoadmoreLisitener {
        void onLoadmore(boolean hasMore);
    }


    public void clear() {
        mSourceData.clear();
        showData.clear();
        mOnLoadmoreLisitener = null;
        mSourceData = null;
        showData = null;
    }
}
