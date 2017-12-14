package com.yc.english.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.yc.english.R;

import java.math.BigDecimal;

public class HonourAbilityView extends View {

    //default value
    private int defLineColor = Color.WHITE;
    private float defLineStrokeWidth = SizeUtils.dp2px(1f);
    private int defDotColor = Color.WHITE;
    private float defDotStrokeWidth = SizeUtils.dp2px(1f);
    private float defDotRadius = SizeUtils.dp2px(1.5f);
    private int defCoverColor = Color.WHITE;
    private float defCoverStrokeWidth = SizeUtils.dp2px(1.5f);
    private int defCenterTextSize = SizeUtils.dp2px(12f);
    private int defTextSize = SizeUtils.dp2px(15f);
    private int defTextColor = Color.WHITE;
    private int defCenterTextColor = Color.WHITE;
    private int defTextOffset = SizeUtils.dp2px(8f);

    //attr value
    private int mLineColor;
    private float mLineStrokeWidth;
    private int mDotColor;
    private float mDotStrokeWidth;
    private float mDotRadius;
    private int mCoverColor;
    private float mCoverStrokeWidth;
    private int mTextColor;
    private float mTextSize;
    private float mCenterTextSize;
    private int mCenterTextColor;


    //画笔
    private Paint mLinePaint;
    private Paint mDotPaint;
    private Paint mCoverPaint;
    private Paint mTextPaint;
    private Paint mCenterTextPaint;

    private int layer = 5;
    private float radius;
    private int centerX;
    private int centerY;
    private float textOffset;

    private float[] datas;
    private String[] titles;
    private int[] titleColors;

    public HonourAbilityView(Context context) {
        this(context, null);
    }

    public HonourAbilityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HonourAbilityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private class FPoint {
        private float x;
        private float y;

        public FPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.honour_ability_view, defStyleAttr, 0);
        mLineColor = a.getColor(R.styleable.honour_ability_view_honour_ability_line_color, defLineColor);
        mLineStrokeWidth = a.getDimension(R.styleable.honour_ability_view_honour_ability_line_width, defLineStrokeWidth);
        mDotColor = a.getColor(R.styleable.honour_ability_view_honour_ability_dot_color, defDotColor);
        mDotStrokeWidth = a.getDimension(R.styleable.honour_ability_view_honour_ability_dot_width, defDotStrokeWidth);
        mDotRadius = a.getDimension(R.styleable.honour_ability_view_honour_ability_dot_radius, defDotRadius);
        mCoverColor = a.getColor(R.styleable.honour_ability_view_honour_ability_cover_color, defCoverColor);
        mCoverStrokeWidth = a.getDimension(R.styleable.honour_ability_view_honour_ability_cover_width, defCoverStrokeWidth);
        mTextColor = a.getColor(R.styleable.honour_ability_view_honour_ability_text_color, defTextColor);
        mTextSize = a.getDimension(R.styleable.honour_ability_view_honour_ability_text_size, defTextSize);
        mCenterTextSize = a.getDimension(R.styleable.honour_ability_view_honour_ability_center_text_size, defCenterTextSize);
        mCenterTextColor = a.getColor(R.styleable.honour_ability_view_honour_ability_center_text_color,
                defCenterTextColor);
        textOffset = a.getDimension(R.styleable.honour_ability_view_honour_ability_text_offset, defTextOffset);
        a.recycle();
        initPaint();
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mLineStrokeWidth);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint.setStrokeWidth(mCoverStrokeWidth);
        mCoverPaint.setColor(mCoverColor);
        mCoverPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCoverPaint.setAlpha(120);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStyle(Paint.Style.FILL);

        mCenterTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setColor(mCenterTextColor);
        mCenterTextPaint.setTextSize(mCenterTextSize);
        mCenterTextPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(w, h) / 3;
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(datas == null || datas.length < layer){ return;}
        drawPolygon(canvas);
        drawLines(canvas);
        drawCoverAndDots(canvas);
        drawScore(canvas);
        drawTitle(canvas);
    }

    private void drawPolygon(Canvas canvas) {
        for (int i = 0; i < layer; i++) {
            drawPolygon(canvas, 1f - 1f / layer * i);
        }
    }


    private void drawPolygon(Canvas canvas, float percent) {
        FPoint[] points = getPoints(percent);
        drawPolygon(canvas, points, mLinePaint);
    }

    private void drawPolygon(Canvas canvas, FPoint[] points, Paint paint) {
        paint.setStrokeWidth(mLineStrokeWidth);
        Path path = new Path();
        path.moveTo(points[0].x, points[0].y);
        path.lineTo(points[1].x, points[1].y);
        path.lineTo(points[2].x, points[2].y);
        path.lineTo(points[3].x, points[3].y);
        path.lineTo(points[4].x, points[4].y);
        path.lineTo(points[5].x, points[5].y);
        path.lineTo(points[0].x, points[0].y);
        path.close();
        canvas.drawPath(path, paint);
    }

    private FPoint[] getPoints(float percent) {
        float tempRadius = radius * percent;
        float height = (float) (Math.sqrt(0.75f) * tempRadius);
        FPoint point = new FPoint(centerX, centerY - tempRadius);
        FPoint point2 = new FPoint(centerX - height, centerY - tempRadius / 2);
        FPoint point3 = new FPoint(centerX - height, centerY + tempRadius / 2);
        FPoint point4 = new FPoint(centerX, centerY + tempRadius);
        FPoint point5 = new FPoint(centerX + height, centerY + tempRadius / 2);
        FPoint point6 = new FPoint(centerX + height, centerY - tempRadius / 2);
        return new FPoint[]{point, point2, point3, point4, point5, point6};
    }

    private void drawLines(Canvas canvas) {
        FPoint[] maxPoints = getPoints(1);
        FPoint[] minPoints = getPoints((1f - 1f / layer * (layer - 1)));
        mLinePaint.setStrokeWidth(mLineStrokeWidth / 2);
        Path path = new Path();
        for (int i = 0; i < maxPoints.length; i++) {
            path.moveTo(maxPoints[i].x, maxPoints[i].y);
            path.lineTo(minPoints[i].x, minPoints[i].y);
            path.close();

            canvas.drawPath(path, mLinePaint);
        }
    }

    private void drawCoverAndDots(Canvas canvas) {
        FPoint[] scroePoints = new FPoint[6];
        for (int i = 0; i < datas.length; i++) {
            FPoint[] points = getPoints(datas[i]);
            mDotPaint.setStrokeWidth(mDotStrokeWidth);
            mDotPaint.setColor(mDotColor);
            canvas.drawCircle(points[i].x, points[i].y, mDotRadius, mDotPaint);
            scroePoints[i] = points[i];
        }
        drawPolygon(canvas, scroePoints, mCoverPaint);
    }


    private void drawScore(Canvas canvas) {
        float total = 0f;
        for (int i = 0; i < datas.length; i++) {
            total += datas[i];
        }
        String average = new BigDecimal(total / datas.length * 100+"").setScale(0, BigDecimal.ROUND_HALF_UP)
                .toString();
        drawText(average, centerX, centerY, mCenterTextPaint, canvas);
    }

    private void drawText(String text, float centerX, float centerY, Paint paint, Canvas canvas) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, centerX - bounds.width() / 2, centerY + bounds.height() / 2, paint);
    }

    private void drawTitle(Canvas canvas) {
        FPoint[] maxPoints = getPoints(1);
        for (int i = 0; i < titles.length; i++) {
            Rect bounds = new Rect();
            if (titleColors != null && titleColors.length == titles.length) {
                mTextPaint.setColor(titleColors[i]);
            }
            mTextPaint.getTextBounds(titles[i], 0, titles[i].length(), bounds);
            if (i == 0) {
                canvas.drawText(titles[i], maxPoints[i].x - bounds.width() / 2, maxPoints[i].y - textOffset,
                        mTextPaint);
            } else if (i == 1 || i == 2) {
                canvas.drawText(titles[i], maxPoints[i].x - bounds.width() - textOffset, maxPoints[i].y + bounds
                                .height() / 2,
                        mTextPaint);
            } else if (i == 4 || i == 5) {
                canvas.drawText(titles[i], maxPoints[i].x + textOffset, maxPoints[i].y + bounds.height() / 2,
                        mTextPaint);
            } else if (i == 3) {
                canvas.drawText(titles[i], maxPoints[i].x - bounds.width() / 2, maxPoints[i].y + bounds.height() +
                                textOffset,
                        mTextPaint);
            }
        }
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public HonourAbilityView setLineColor(int lineColor) {
        mLineColor = lineColor;
        return this;
    }

    public float getLineStrokeWidth() {
        return mLineStrokeWidth;
    }

    public HonourAbilityView setLineStrokeWidth(float lineStrokeWidth) {
        mLineStrokeWidth = lineStrokeWidth;
        return this;
    }

    public int getDotColor() {
        return mDotColor;
    }

    public HonourAbilityView setDotColor(int dotColor) {
        mDotColor = dotColor;
        return this;
    }

    public float getDotStrokeWidth() {
        return mDotStrokeWidth;
    }

    public HonourAbilityView setDotStrokeWidth(float dotStrokeWidth) {
        mDotStrokeWidth = dotStrokeWidth;
        return this;
    }

    public float getDotRadius() {
        return mDotRadius;
    }

    public HonourAbilityView setDotRadius(float dotRadius) {
        mDotRadius = dotRadius;
        return this;
    }

    public int getCoverColor() {
        return mCoverColor;
    }

    public HonourAbilityView setCoverColor(int coverColor) {
        mCoverColor = coverColor;
        return this;
    }

    public float getCoverStrokeWidth() {
        return mCoverStrokeWidth;
    }

    public HonourAbilityView setCoverStrokeWidth(float coverStrokeWidth) {
        mCoverStrokeWidth = coverStrokeWidth;
        return this;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public HonourAbilityView setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public HonourAbilityView setTextSize(float textSize) {
        mTextSize = textSize;
        return this;
    }

    public float getCenterTextSize() {
        return mCenterTextSize;
    }

    public HonourAbilityView setCenterTextSize(float centerTextSize) {
        mCenterTextSize = centerTextSize;
        return this;
    }

    public float[] getDatas() {
        return datas;
    }

    public HonourAbilityView setDatas(float[] datas) {
        this.datas = datas;
        return this;
    }

    public String[] getTitles() {
        return titles;
    }

    public HonourAbilityView setTitles(String[] titles) {
        this.titles = titles;
        return this;
    }

    public HonourAbilityView setTitleColors(int[] titleColors) {
        this.titleColors = titleColors;
        return this;
    }

}
