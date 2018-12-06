package com.yl.acoreui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yl.core.utils.DeviceUtils;
import com.yl.acoreui.R;


/**
 * Created by 寻水的鱼 on 2017/12/12.
 */
public class HorizontalColumn extends View {
    int MAX = 100;//最大
    int corner = 8;//圆角
    int data = 0;//显示的数
    int tempData = 0;
    int textPadding = 4;
    Paint mPaint;
    int mColor;

    Context mContext;

    public HorizontalColumn(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    public HorizontalColumn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public HorizontalColumn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mColor = mContext.getResources().getColor(R.color.colorAccent);
        mPaint.setColor(mColor);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (data == 0) {
            mPaint.setTextSize(getHeight() / 2);
            RectF oval3 = new RectF(0, 0, DeviceUtils.pixelsToDp(mContext, 20), getHeight());// 设置个新的长方形
            canvas.drawRoundRect(oval3, DeviceUtils.pixelsToDp(mContext, corner), DeviceUtils.pixelsToDp(mContext, corner), mPaint);
            int totalTextWidth = getTotalTextWidth("0票");
            drawText(canvas, "0票", DeviceUtils.pixelsToDp(mContext, 20) + 2 * DeviceUtils.pixelsToDp(mContext, textPadding), getHeight() * 0.5f - totalTextWidth * 0.5f - getFirstTextHeight("0票") * 0.5f, mPaint, 90f);
            return;
        }
        //防止数值很大的的时候，动画时间过长
        int step = data / 100 + 1;
        if (tempData < data - step) {
            tempData = tempData + step;
        } else {
            tempData = data;
        }
        //画圆角矩形
        String S = tempData + "票";
        //一个字和两,三个字的字号相同
        if (S.length() < 4) {
            mPaint.setTextSize(getHeight() / 2);
        } else {
            mPaint.setTextSize(getHeight() / (S.length() - 1));
        }
        float textH = mPaint.ascent() + mPaint.descent();
        float MaxH = getWidth() - mPaint.measureText(S) - textH - 2 * DeviceUtils.pixelsToDp(mContext, textPadding);
        //圆角矩形的实际高度
        float realH = MaxH / MAX * tempData;
        RectF oval3 = new RectF(0, 0, realH, getHeight());// 设置个新的长方形
        canvas.drawRoundRect(oval3, DeviceUtils.pixelsToDp(mContext, corner), DeviceUtils.pixelsToDp(mContext, corner), mPaint);
        // 写数字
        drawVerticalText(S, canvas, realH);
        int totalTextWidth = getTotalTextWidth(S);
        drawText(canvas, S, realH + 2 * DeviceUtils.pixelsToDp(mContext, textPadding), getHeight() * 0.5f - totalTextWidth * 0.5f - getFirstTextHeight(S) * 0.5f, mPaint, 90f);
        if (tempData != data) {
            postInvalidate();
        }
    }

    public void setData(int data, int MAX) {
        this.data = data;
        tempData = 0;
        this.MAX = MAX;
        postInvalidate();
    }

    private int getTextHeigth(String text) {
        char[] charArray = text.toCharArray();
        Rect rect = new Rect();
        mPaint.getTextBounds(charArray, 0, charArray.length, rect);
        return rect.height();
    }

    private int getTotalTextWidth(String text) {
        char[] charArray = text.toCharArray();
        int totalTextWidth = 0;
        for (int i = 0; i < charArray.length; i++) {
            Rect r = new Rect();
            char[] charText = new char[]{charArray[i]};
            mPaint.getTextBounds(charText, 0, 1, r);
            totalTextWidth += r.width();
        }
        return totalTextWidth;
    }

    private int getFirstTextHeight(String text) {
        String substring = text.substring(0, 1);
        Rect r = new Rect();
        mPaint.getTextBounds(substring, 0, 1, r);
        return r.height();
    }

    public int getData() {
        return data;
    }

    private void drawVerticalText(String text, Canvas canvas, float realH) {

        int totalTextHeight = 0;
        int firstTextHeight = 0;
        int maxTextWidth = 0;
        char[] chars = text.toCharArray();
        int[] charHeight = new int[chars.length];
        int[] charWidth = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            Rect r = new Rect();
            char[] txt = new char[]{chars[i]};
            mPaint.getTextBounds(txt, 0, 1, r);
            if (i == 0) {
                firstTextHeight = r.height();
            }
            if (maxTextWidth < r.width()) {
                maxTextWidth = r.width();
            }
            charHeight[i] = r.height();
            charWidth[i] = r.width();
            totalTextHeight += r.height();
        }
        float starDrawTextHeight = getHeight() * 0.5f - totalTextHeight * 0.5f + firstTextHeight;
        for (int j = 0; j < chars.length; j++) {
            float x = DeviceUtils.pixelsToDp(mContext, 20) + 2 * DeviceUtils.pixelsToDp(mContext, textPadding);
            if (charWidth[j] < maxTextWidth) {
                x = x + maxTextWidth * 0.5f - charWidth[j] * 0.5f;
            }
            if (data == 0 || realH == 0) {
                canvas.drawText(String.valueOf(chars[j]), x, starDrawTextHeight, mPaint);
            } else {
                canvas.drawText(String.valueOf(chars[j]), x, starDrawTextHeight, mPaint);
            }
            starDrawTextHeight = starDrawTextHeight + charHeight[j] + 2 * DeviceUtils.pixelsToDp(mContext, textPadding);
        }
    }

    void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        if (angle != 0) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0) {
            canvas.rotate(-angle, x, y);
        }
    }
}
