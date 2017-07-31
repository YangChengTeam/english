package com.yc.english.base.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by admin on 2017/7/28.
 */

public class DrawableUtils {

    /**
     *
     * @param mContext
     * @param radius 圆角半径
     * @param resColor resources资源颜色
     * @return
     */
    public static GradientDrawable getBgColor(Context mContext, int radius,int resColor) {

        int roundRadius = SizeUtils.dp2px(radius);
        //创建drawable
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ContextCompat.getColor(mContext, resColor));
        gradientDrawable.setCornerRadius(roundRadius);
        return gradientDrawable;
    }
}
