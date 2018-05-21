package com.yc.junior.english.intelligent.view.wdigets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.yc.junior.english.R;


/**
 * Created by wanglin  on 2017/6/23 17:45.
 */

public class RingView extends View {
    private int mFirstColor;//第一种颜色

    private int mSecondColor;//第二种颜色

    private int mRaduis;//圆弧的半径

    private int mSpeed;//速度

    private Paint mPaint;//画笔

    private int mProgress;//

    private boolean isNext;//是否绘制下一个

    private float mTextSize;

    private int mTextColor;

    private Rect mRect;

    private static final String TAG = "RingView";

    private String mTitle = "正答率";
    private String mText;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RingView, defStyleAttr, 0);
        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.RingView_firstColor:
                    mFirstColor = ta.getColor(attr, Color.RED);
                    break;
                case R.styleable.RingView_secondColor:
                    mSecondColor = ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.RingView_radius:
                    mRaduis = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RingView_speed:
                    mSpeed = ta.getInteger(attr, 20);
                    break;
                case R.styleable.RingView_ringTextSize:
                    mTextSize = ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.RingView_ringTextColor:
                    mTextColor = ta.getColor(attr, Color.RED);
                    break;
                case R.styleable.RingView_ringText:
                    mText = ta.getString(R.styleable.RingView_ringText);
                    break;
            }
        }
        ta.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mRect = new Rect();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;//圆心
        int width = center - mRaduis / 2;//半径
        mPaint.setStrokeWidth(mRaduis);//设置圆环的宽度
        mPaint.setAntiAlias(true);//消除锯齿

        RectF ovalRect = new RectF(center - width, center - width, center + width, center + width);

        if (!isNext) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(center, center, center, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);//设置圆环空心
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center, center, width, mPaint);
            mPaint.setColor(mSecondColor);
            canvas.drawArc(ovalRect, 0, mProgress, false, mPaint);

        } else {
            mPaint.setColor(mSecondColor); // 设置圆环的颜色
            canvas.drawCircle(center, center, width, mPaint); // 画出圆环
            mPaint.setColor(mFirstColor); // 设置圆环的颜色
            canvas.drawArc(ovalRect, 0, mProgress, false, mPaint); // 根据进度画圆弧
        }
        drawText(canvas, center);


    }


    private void drawText(Canvas canvas, int center) {
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFakeBoldText(true);

        mPaint.setTextSize(SizeUtils.sp2px(18));
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mRect);
        canvas.drawText(mTitle, center - mRect.width() / 2, center + mRect.height() / 2 - SizeUtils.dp2px(15), mPaint);

        mPaint.setTextSize(SizeUtils.sp2px(25));
        canvas.drawText(mText, center - mRect.width() / 2 + SizeUtils.dp2px(10), center + mRect.height() / 2 + SizeUtils.dp2px(15), mPaint);

        mPaint.setTextSize(SizeUtils.sp2px(14));
        mPaint.getTextBounds(mText, 0, mText.length(), mRect);
        canvas.drawText("%", center - mRect.width() / 2 + mRect.width() + SizeUtils.dp2px(5), center + mRect.height() / 2 + SizeUtils.dp2px(15), mPaint);
    }

    public void setText(final String mText) {
        this.mText = mText;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    if (mProgress < (Integer.parseInt(mText) / 100.0f) * 360) {
//                        mProgress = 0;
//                        isNext = !isNext;
                        mProgress++;
                        postInvalidate();
                    }
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
