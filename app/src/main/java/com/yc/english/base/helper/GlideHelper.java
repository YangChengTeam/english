package com.yc.english.base.helper;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by zhangkai on 2017/8/8.
 */

public class GlideHelper {
    public static void circleBorderImageView(final Context context, ImageView imageView, String url, int
            placehorder, float borderwidth, int bordercolor) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placehorder).transform(new GlideCircleTransformation(context, borderwidth,
                bordercolor));
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void circleImageView(final Context context, ImageView imageView, String url, int
            placehorder) {
        circleBorderImageView(context, imageView, url, placehorder, 0, Color.WHITE);
    }

    public static void imageView(final Context context, ImageView imageView, String url, int
            placehorder) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placehorder);
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
