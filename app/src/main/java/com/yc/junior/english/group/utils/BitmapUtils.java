package com.yc.junior.english.group.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * Created by wanglin  on 2017/10/27 14:47.
 */

public class BitmapUtils {


    public static Bitmap transformRoundDrawable(Context context, int resId, int dp) {
        float radius = Resources.getSystem().getDisplayMetrics().density * dp;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);

        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setXfermode(null);

        return result;


    }
}
