package com.yl.acoreui.weight;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by 寻水的鱼 on 2018/4/10.
 */
public class ScrollTextView extends android.support.v7.widget.AppCompatTextView {
    /**
     * 文字长度
     */
    private float textLength = 0f;

    /**
     * 滚动条长度
     */
    private float viewWidth = 0f;
    /**
     * 文本x轴 的坐标
     */
    private float tx = 0f;
    /**
     * 文本Y轴的坐标
     */
    private float ty = 0f;
    /**
     * 文本当前长度
     */
    private float temp_tx1 = 0.0f;
    /**
     * 文本当前变换的长度
     */
    private float temp_tx2 = 0x0f;
    /**
     * 文本滚动开关
     */
    private boolean isStarting = false;
    /**
     * 画笔对象
     */
    private Paint paint = null;
    /**
     * 显示的文字
     */
    private String text = "";
    /**
     * 文本滚动速度
     **/
    private float sudu;

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,android.R.style.,0,0);
    }

    /**
     * 初始化自动滚动条,每次改变文字内容时，都需要重新初始化一次
     *
     * @param activity 获取屏幕
     * @param text     显示的内容
     * @param su       文字滚动的速度 越大滚动越快
     */
    public void initScrollTextView(final Activity activity, String text, float su) {
        // 得到画笔,获取父类的textPaint
        paint = this.getPaint();
        // 得到文字
        this.text = text;
        this.sudu = su;
        textLength = paint.measureText(text);// 获得当前文本字符串长度


//        viewWidth =  705;// 获取宽度return mRight - mLeft;
        //获取根元素
        View mContainerView = activity.getWindow().getDecorView();//.findViewById(android.R.id.content);
        //得到整个view树的viewTreeObserver
        final ViewTreeObserver viewTreeObserver = mContainerView.getViewTreeObserver();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewWidth = getWidth();
                if (viewWidth == 0) {
                    // 获取当前屏幕的属性
                    Display display = activity.getWindowManager().getDefaultDisplay();
                    DisplayMetrics dm = new DisplayMetrics();
                    display.getMetrics(dm);
                    viewWidth = dm.widthPixels;// 获取屏幕宽度  viewWidth 是滚动的开始位置，需要修改的
                    // 可再此入手
                }
                tx = textLength;
                temp_tx1 = viewWidth + textLength;
                temp_tx2 = viewWidth + textLength * 2;// 自己定义，文字变化多少
                // // 文字的大小+距顶部的距离
                ty = ScrollTextView.this.getTextSize() + ScrollTextView.this.getPaddingTop();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    /**
     * 开始滚动
     */
    public void starScroll() {
        // 开始滚动
        isStarting = true;
        this.invalidate();// 刷新屏幕
    }

    /**
     * 停止方法,停止滚动
     */
    public void stopScroll() {
        // 停止滚动
        isStarting = false;
        this.invalidate();// 刷新屏幕
    }


    /**
     * 重写onDraw方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (isStarting) {
            // A-Alpha透明度/R-Read红色/g-Green绿色/b-Blue蓝色
//            paint.setARGB(255, 200, 200, 200);//白色
            paint.setARGB(255, 0, 0, 0);//黑色


            paint.setColor(getCurrentTextColor());
            canvas.drawText(text, temp_tx1 - tx, ty, paint);
            tx += sudu;
            // 当文字滚动到屏幕的最左边
            if (tx > temp_tx2) {
                // 把文字设置到最右边开始
                tx = textLength;
            }
            this.invalidate();// 刷新屏幕
        }
        super.onDraw(canvas);
    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
    }
}
