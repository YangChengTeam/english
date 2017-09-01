package com.yc.english.base.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.yc.english.R;

/**
 * Created by admin on 2017/7/28.
 */

public class DrawableUtils {

    /**
     * @param mContext
     * @param radius   圆角半径
     * @param resColor resources资源颜色
     * @return
     */
    public static GradientDrawable getBgColor(Context mContext, int radius, int resColor) {

        int roundRadius = SizeUtils.dp2px(radius);
        //创建drawable
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ContextCompat.getColor(mContext, resColor));
        gradientDrawable.setCornerRadius(roundRadius);
        return gradientDrawable;
    }

    /**
     * 获取资源下的图片Uri
     *
     * @param context
     * @return
     */
    public static Uri getAddUri(Context context) {
        Uri addUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + context.getResources().getResourcePackageName(R.mipmap.note_image_add_icon) + "/"
                + context.getResources().getResourceTypeName(R.mipmap.note_image_add_icon) + "/"
                + context.getResources().getResourceEntryName(R.mipmap.note_image_add_icon));
        return addUri;
    }
}
