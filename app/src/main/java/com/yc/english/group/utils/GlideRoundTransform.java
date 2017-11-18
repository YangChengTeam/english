package com.yc.english.group.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.yc.english.R;
import com.zhihu.matisse.internal.utils.UIUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

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
    private boolean mIsMore;

    public GlideRoundTransform(Context context, int position, boolean isMore) {
        this(context, 5, position, isMore);
        this.mPosition = position;
        this.mIsMore = isMore;
    }

    public GlideRoundTransform(Context context, int dp, int position, boolean isMore) {

        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        this.mPosition = position;
        this.mIsMore = isMore;
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
        height = (int) (width * (intrinsicHeight / intrinsicWidth));

        if (!mIsMore && mPosition == 0) {
            height = height * 4 / 5;
        }

        LogUtils.e("roundCrop " + mPosition + "--" + height + "==" + width);

        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);

        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        RectF rectF = new RectF(0f, 0f, width, height);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        return result;
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

        height = (int) (width * (intrinsicHeight / intrinsicWidth));


        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mPaint.setColor(Color.RED);
        RectF rectF = new RectF(0f, 0f, width, height);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(result, 0, 0, mPaint);
        return bitmap;

    }

}
