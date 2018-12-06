package com.yl.acoreui.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.acoreui.utils.ImageUtils;
import com.yl.core.http.imageloader.glide.ImageConfigImpl;
import com.yl.acoreui.R;

import java.util.List;

/**
 * Created by 寻水的鱼 on 2018/8/14.
 */
public class BigImagePreviewAdapter extends BasePagerAdapter<String> {
    private List<String> mTitleLsit;
    private Context mContext;

    /***
     *
     * @param context
     * @param titleList 标题列表
     * @param urlList  图片地址列表
     */
    public BigImagePreviewAdapter(Context context, List<String> titleList, List<String> urlList) {
        super(urlList);
        mTitleLsit = titleList;
        mContext = context;
    }

    @Override
    public View newView(int position) {
        View rootView = View.inflate(mContext, R.layout.viewpager_big_image_preview, null);
        TextView tvTitle = rootView.findViewById(R.id.tv_big_title);
        TextView tvPage = rootView.findViewById(R.id.tv_big_page);
        ImageView imageView = rootView.findViewById(R.id.iv_big);
        if (checkTitles()) {
            ImageUtils.loadImage(mContext,ImageConfigImpl.builder().url(mData.get(position)).imageView(imageView).errorPic(R.mipmap.image_error).placeholder(R.mipmap.image_load).cacheStrategy(2).isCrossFade(true).build());
            tvPage.setText((position + 1) + "/" + mData.size());
            if (tvTitle != null && mTitleLsit != null) {
                tvTitle.setText(mTitleLsit.get(position));
            }
        }
        return rootView;
    }

    private boolean checkTitles() {
        if (mTitleLsit != null && mData != null) {
            if (mTitleLsit.size() == mData.size()) {
                return true;
            } else {
                throw new IllegalArgumentException("mTitleLsit size is" + mTitleLsit.size() + "; but urlList size is" + mData.size() + "!");
            }
        } else {
            return true;
        }
    }
}
