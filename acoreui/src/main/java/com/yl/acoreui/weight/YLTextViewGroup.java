package com.yl.acoreui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yl.acoreui.R;


public class YLTextViewGroup extends RelativeLayout {
    private TextView mTvLeft;
    private TextView mTvRight;
    private ImageView mIvLeft;
    private ImageView mIvArrow;

    private String mTextLeft = "";
    private String mTextRight = "";
    private int mTextColorLeft = 0;
    private int mTextColorRight = 0;
    private int mTextRightGravity = 5;
    private int mImgLeft = 0;
    private boolean mTextRightSingleLine = true;
    private boolean mArrowShow = true;
    private boolean mRequired = false;

    public ImageView getmIvRight() {
        return mIvRight;
    }

    private ImageView mIvRight;

    public YLTextViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public YLTextViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public YLTextViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.text_view_group, this, true);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YLTextViewGroup);
        mTextLeft = typedArray.getString(R.styleable.YLTextViewGroup_yl_text_left);
        mTextRight = typedArray.getString(R.styleable.YLTextViewGroup_text_right);
        mTextColorLeft = typedArray.getColor(R.styleable.YLTextViewGroup_text_color_left, mTextColorLeft);
        mTextColorRight = typedArray.getColor(R.styleable.YLTextViewGroup_text_color_right, mTextColorRight);
        mTextRightGravity = typedArray.getInt(R.styleable.YLTextViewGroup_text_right_gravity, mTextRightGravity);
        mImgLeft = typedArray.getResourceId(R.styleable.YLTextViewGroup_img_left, mImgLeft);
        mTextRightSingleLine = typedArray.getBoolean(R.styleable.YLTextViewGroup_text_right_single_line, mTextRightSingleLine);
        mArrowShow = typedArray.getBoolean(R.styleable.YLTextViewGroup_arrow_show, mArrowShow);
        mRequired = typedArray.getBoolean(R.styleable.YLTextViewGroup_required, false);
        typedArray.recycle();
    }

    public void setTextLeft(String s) {
        if (mRequired) {
            mTvLeft.setText(Html.fromHtml("<font width = 'auto' color='#FF0000'>*</font>" + s));
        } else {
            mTvLeft.setText(s);
        }
    }

    public String getTextLeft() {
        return mTvLeft.getText().toString();
    }

    public void setTextRight(String s) {
        if (s != null) {
            mTvRight.setText(s);
        }
    }

    public String getTextRight() {
        return mTvRight.getText().toString();
    }

    public TextView getTextViewRight() {
        return mTvRight;
    }

    public void setTextSizeLeft(int size) {
        mTvLeft.setTextSize(size);
    }

    public void setTextSizeRight(int size) {
        mTvRight.setTextSize(size);
    }

    public void setTextColorLeft(int color) {
        mTvLeft.setTextColor(color);
    }

    public void setTextColorRight(int color) {
        mTvRight.setTextColor(color);
    }

    public void setTextRightGravity(int gravity) {
        mTvRight.setGravity(gravity);
    }

    public void setArrowShow(boolean visibility) {
        viewVisibility(mIvArrow, visibility);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvLeft = (TextView) findViewById(R.id.vg_tv_left);
        mTvRight = (TextView) findViewById(R.id.vg_tv_right);
        mIvLeft = (ImageView) findViewById(R.id.vg_iv_left);
        mIvArrow = (ImageView) findViewById(R.id.vg_iv_arrow);
        mIvRight = (ImageView) findViewById(R.id.iv_right);

        if (!TextUtils.isEmpty(mTextLeft)) {
            setTextLeft(mTextLeft);
        }

        if (mTextColorLeft != 0) {
            mTvLeft.setTextColor(mTextColorLeft);
        }
        mTvRight.setText(mTextRight);
        if (mTextColorRight != 0) {
            mTvRight.setTextColor(mTextColorRight);
        }
        mTvRight.setGravity(mTextRightGravity);
        mTvRight.setSingleLine(mTextRightSingleLine);

        if (mImgLeft != 0) {
            mIvLeft.setVisibility(View.VISIBLE);
            mIvLeft.setImageResource(mImgLeft);
        }

        viewVisibility(mIvArrow, mArrowShow);

    }

    private void viewVisibility(View view, boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
