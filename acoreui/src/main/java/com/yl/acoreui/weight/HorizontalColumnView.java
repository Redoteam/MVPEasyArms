package com.yl.acoreui.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.yl.acoreui.R;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * Created by 寻水的鱼 on 2017/12/13.
 */

public class HorizontalColumnView extends AutoRelativeLayout {
    private HorizontalColumn hColumn;

    public Button getTvTitle() {
        return tvTitle;
    }

    private Button tvTitle;

    public HorizontalColumnView(Context context) {
        super(context);
        initView(context, null);
    }

    public HorizontalColumnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public HorizontalColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(final Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.item_horizontal_column, this);
        hColumn = (HorizontalColumn) view.findViewById(R.id.hColumn);
        tvTitle = (Button) view.findViewById(R.id.title);
        tvTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tvTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.red_D2311F));
            } else {
                tvTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            }
        });
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setData(int data, int total) {
        hColumn.setData(data, total);
    }

    public int getData() {
        return hColumn.getData();
    }
}
