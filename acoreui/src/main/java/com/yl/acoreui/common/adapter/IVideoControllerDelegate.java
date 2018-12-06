package com.yl.acoreui.common.adapter;

import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;

/**
 * Created by 寻水的鱼 on 2018/12/4.
 */
public abstract class IVideoControllerDelegate<T> implements BaseHasHeadLineItemViewDelegate<T> {
    public final static String TAG = "IVideoControllerDelegate";
    protected GSYVideoHelper smallVideoHelper;

    protected GSYVideoHelper.GSYVideoHelperBuilder gsySmallVideoHelperBuilder;

    public IVideoControllerDelegate(GSYVideoHelper gsyVideoHelper,GSYVideoHelper.GSYVideoHelperBuilder gsyVideoHelperBuilder) {
        smallVideoHelper = gsyVideoHelper;
        gsySmallVideoHelperBuilder = gsyVideoHelperBuilder;
    }


}
