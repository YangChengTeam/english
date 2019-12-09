package com.kk.guide;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by zhangkai on 2017/6/16.
 */

public class GuideView extends View {
    private Context mContext;
    private GuideCallback callback;
    private RectF mRectF;
    private int bgColor;
    private GuidePopupWindow guidePopupWindow;
    private GuideType type = GuideType.OVAL;
    private float border;

    public void setBorder(float border) {
        border = GuideUtil.dip2px(mContext, border);
        this.border = border;
    }

    public GuideView(Context context) {
        super(context);
        this.mContext = context;
        setBgAlpha(0.4f);
    }

    public GuideView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBgAlpha(float bgAlpha) {
        String alpth = Integer.toHexString((int) (0xFF - 0xFF * bgAlpha));
        if (alpth.length() < 2) {
            alpth = "0" + alpth;
        }
        bgColor = Color.parseColor("#" + alpth + "000000");
    }

    public void setMeasure(float l, float t, float r, float b) {
        mRectF = new RectF(l , t , r, b );
    }


    public void setType(GuideType type) {
        this.type = type;
    }

    public void setCallback(GuideCallback callback) {
        this.callback = callback;
    }

    public void setGuidePopupWindow(GuidePopupWindow guidePopupWindow) {
        this.guidePopupWindow = guidePopupWindow;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        int screenWidth, screenHeight;
        if (Build.VERSION.SDK_INT >= 13) {
            Display localDisplay = ((WindowManager) mContext.getSystemService(Application.WINDOW_SERVICE)).getDefaultDisplay();
            Point localPoint = new Point();
            localDisplay.getSize(localPoint);
            screenWidth = localPoint.x;
            screenHeight = localPoint.y;
        } else {
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        }

        Bitmap bgBitmap = Bitmap.createBitmap(screenWidth, screenHeight, conf);
        Canvas bgCanvas = new Canvas(bgBitmap);
        bgCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        bgCanvas.drawColor(bgColor);
        bgCanvas.save();

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bgBitmap, 0f, 0f, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        if (type == GuideType.OVAL) {
            canvas.drawRoundRect(mRectF, border, border, paint);
        } else if (type == GuideType.CIRCLE) {
            float cx = (mRectF.left + mRectF.right) / 2;
            float cy = (mRectF.top + mRectF.bottom) / 2;
            float r = (mRectF.right - mRectF.left) / 2;
            canvas.drawCircle(cx, cy, r, paint);
        }

        if (!bgBitmap.isRecycled()) {
            bgBitmap.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x >= mRectF.left && x <= mRectF.right) && (y >= mRectF.top && y <= mRectF.bottom)) {
            if (event.getAction() == ACTION_UP) {
                if (callback != null) {
                    callback.onClick(guidePopupWindow);
                }
            }
        }
        return true;
    }

    public enum GuideType {
        OVAL,
        CIRCLE
    }

}
