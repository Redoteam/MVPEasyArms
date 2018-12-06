package com.yl.acoreui.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yl.acoreui.R;


public class ClearEditViewSingle extends LinearLayout {
    private EditText mEtContent;
    private ImageView mIvClear;
    private ImageView mIvIcon;
    private int mTextColor = 0xFF444444;
    private int mTextSize = 16;
    private int mMaxLength = 0;
    private int mFocusBg = 0;
    private int mBlurBg = 0;
    private Drawable mFocusIcon = null;
    private Drawable mBlurIcon = null;
    private String mHint = "";
    private int mInputType = 1;

    public ClearEditViewSingle(Context context) {
        super(context);
        init(context, null);
    }

    public ClearEditViewSingle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditViewSingle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.single_clear_edit_view, this, true);
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditViewSingle);
        mTextColor = typedArray.getColor(R.styleable.ClearEditViewSingle_s_text_color, mTextColor);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.ClearEditViewSingle_s_text_size, mTextSize);
        mMaxLength = typedArray.getInteger(R.styleable.ClearEditViewSingle_s_max_length, mMaxLength);
        mHint = typedArray.getString(R.styleable.ClearEditViewSingle_s_hint);
        mFocusIcon = typedArray.getDrawable(R.styleable.ClearEditViewSingle_s_focus_icon);
        mBlurIcon = typedArray.getDrawable(R.styleable.ClearEditViewSingle_s_blur_icon);
        mFocusBg = typedArray.getResourceId(R.styleable.ClearEditViewSingle_s_focus_bg, mFocusBg);
        mBlurBg = typedArray.getResourceId(R.styleable.ClearEditViewSingle_s_blur_bg, mBlurBg);
        mInputType = typedArray.getInteger(R.styleable.ClearEditViewSingle_s_input_type, mInputType);
        typedArray.recycle();
    }

    public void setText(String s) {
        mEtContent.setText(s);
        mEtContent.setSelection(s.length());
        mIvClear.setVisibility(GONE);
    }

    public void setInputType(int type) {
        if (type != -1) {
            mEtContent.setInputType(type);
        }
    }

    public String getText() {
        return mEtContent.getText().toString().trim();
    }

    public EditText getEditText() {
        return mEtContent;
    }

    public void getFocus() {
        mEtContent.setFocusable(true);
        mEtContent.setFocusableInTouchMode(true);
        mEtContent.requestFocus();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEtContent = (EditText) findViewById(R.id.clear_et_content);
        mIvClear = (ImageView) findViewById(R.id.clear_iv_clear);
        mIvIcon = (ImageView) findViewById(R.id.clear_iv_icon);

        mEtContent.setTextSize(mTextSize);
        mEtContent.setTextColor(mTextColor);
        mEtContent.setHint(mHint);

        if (mInputType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            mEtContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mEtContent.setInputType(mInputType);
        }

        if (mBlurIcon == null && mFocusIcon == null) {
            mIvIcon.setVisibility(View.GONE);
        } else {
            mIvIcon.setVisibility(View.VISIBLE);
            mIvIcon.setImageDrawable(mBlurIcon == null ? mFocusIcon : mBlurIcon);
        }
        if (mBlurBg != 0 || mFocusBg != 0) {
            ClearEditViewSingle.this.setBackgroundResource(mBlurBg == 0 ? mFocusBg : mBlurBg);
        }
        setListener();
    }

    private void setListener() {
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = s.length();

                if (len == 0) {
                    mIvClear.setVisibility(GONE);
                } else {
                    mIvClear.setVisibility(VISIBLE);
                }

                if (mMaxLength == 0) {
                    return;
                }

                if (len > mMaxLength) {
                    String str = s.toString();
                    String newStr = str.substring(0, mMaxLength);
                    mEtContent.setText(newStr);
                    mEtContent.setSelection(mMaxLength);
                }
            }
        });

        mEtContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mEtContent.getText().toString().length() != 0) {
                    mIvClear.setVisibility(VISIBLE);
                }
                if (mFocusBg != 0) {
                    ClearEditViewSingle.this.setBackgroundResource(mFocusBg);
                }
                if (mFocusIcon != null) {
                    mIvIcon.setImageDrawable(mFocusIcon);
                }
            } else {
                mIvClear.setVisibility(GONE);
                if (mBlurBg != 0) {
                    ClearEditViewSingle.this.setBackgroundResource(mBlurBg);
                }
                if (mBlurIcon != null) {
                    mIvIcon.setImageDrawable(mBlurIcon);
                }
            }
        });

        mIvClear.setOnClickListener(v -> mEtContent.setText(""));
    }
}
