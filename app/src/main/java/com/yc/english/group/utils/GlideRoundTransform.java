package com.yc.english.group.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.yc.english.R;

import java.io.PipedOutputStream;
import java.security.MessageDigest;

/**
 * Created by wanglin  on 2017/10/12 11:48.
 */

public class GlideRoundTransform extends BitmapTransformation {
    private float radius = 0f;
    private int mPosition;

    private Drawable drawable;
    private Paint mPaint;

    public GlideRoundTransform(Context context, int position) {
        this(context, 5, position);
        this.mPosition = position;
    }

    public GlideRoundTransform(Context context, int dp, int position) {

        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        this.mPosition = position;
        drawable = ContextCompat.getDrawable(context, R.mipmap.pic_example);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        float intrinsicWidth = drawable.getIntrinsicWidth();
        float intrinsicHeight = drawable.getIntrinsicHeight();

        int width = source.getWidth();
        int height = source.getHeight();
//        if (height > width) {
            height = (int) (width * (intrinsicHeight / intrinsicWidth));
//        }

        LogUtils.e("roundCrop " + mPosition + "--" + height + "==" + width);
//        if (mPosition == 0) {
//            if (height > width) {
//                height = width * 3 / 4;
//            }
//        } else {
//            if (height > width)
//                height = width * 3 / 4;
//        }
        Bitmap result = pool.get(width, height, source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888);


        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        RectF rectF = new RectF(0f, 0f, width, height);
        canvas.drawRoundRect(rectF, radius, radius, paint);


        return TransformationUtils.centerCrop(pool, result, width, height);
    }


    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
//        messageDigest.update(ByteBuffer.allocate(Integer.SIZE).putInt(signature).array());

    }


    private Bitmap roundBitmap1(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        float intrinsicWidth = drawable.getIntrinsicWidth();
        float intrinsicHeight = drawable.getIntrinsicHeight();

        int width = source.getWidth();
        int height = source.getHeight();
        if (height > width) {
            height = (int) (width * (intrinsicHeight / intrinsicWidth));
        }

        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Bitmap transform = TransformationUtils.centerCrop(pool, result, width, height);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rectF = new RectF(0f, 0f, width, height);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(result, 0, 0, mPaint);
        return bitmap;

    }

}
