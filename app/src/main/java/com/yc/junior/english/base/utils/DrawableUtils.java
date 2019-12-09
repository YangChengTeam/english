package com.yc.junior.english.base.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.yc.junior.english.R;

import androidx.core.content.ContextCompat;
import yc.com.blankj.utilcode.util.SizeUtils;

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


    public static GradientDrawable setBg(Context mContext, int radius, int width, int resColor) {
        int roundRadius = SizeUtils.dp2px(radius);
        int widthpx = SizeUtils.dp2px(width);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(roundRadius);
        gradientDrawable.setStroke(widthpx, ContextCompat.getColor(mContext, resColor));
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

    /**
     * 根据
     * @param context
     * @param uri
     * @return
     */
    public static String getPathBuUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((Activity) context).getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
