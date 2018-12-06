package com.yl.acoreui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yl.acoreui.R;


/**
 * @author Yang Shihao
 * 高宽比率的ImageView
 */

public class RatioImageView extends AppCompatImageView {

    //宽:高
    private float mHeightWidthRatio = 0;

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
            mHeightWidthRatio = typedArray.getFloat(R.styleable.RatioImageView_height_width, 0);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightWidthRatio > 0) {
            int height = (int) (MeasureSpec.getSize(widthMeasureSpec) * mHeightWidthRatio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
