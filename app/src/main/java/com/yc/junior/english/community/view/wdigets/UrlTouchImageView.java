/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.yc.junior.english.community.view.wdigets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wang.avi.AVLoadingIndicatorView;
import com.yc.english.R;

public class UrlTouchImageView extends RelativeLayout {

    protected TouchImageView mShowImageView;

    protected Context mContext;

    public View loadingView;

    public AVLoadingIndicatorView mAvLoading;

    private String mImageUrl;

    public UrlTouchImageView(Context ctx, String imgUrl) {
        super(ctx);
        mContext = ctx;
        mImageUrl = imgUrl;
        init();
    }

    public UrlTouchImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }

    public TouchImageView getImageView() {
        return mShowImageView;
    }

    protected void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.community_touch_image_view, null);
        mShowImageView = (TouchImageView) view.findViewById(R.id.touch_image_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        this.addView(view, params);

        //加载进度控件View
        LayoutParams loadParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        loadParams.addRule(RelativeLayout.CENTER_VERTICAL);
        loadParams.setMargins(30, 0, 30, 0);
        loadingView = LayoutInflater.from(mContext).inflate(R.layout.note_image_loading, null);
        this.addView(loadingView, loadParams);

        mShowImageView.setVisibility(VISIBLE);
        mShowImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(GONE);
                ((Activity) mContext).finish();
                ((Activity) mContext).overridePendingTransition(R.anim.note_img_zoomin, R.anim.note_img_zoomout);
            }
        });

        Glide.with(mContext).load(mImageUrl).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                loadingView.setVisibility(GONE);
                mShowImageView.setScaleType(ImageView.ScaleType.MATRIX);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                mShowImageView.setImageBitmap(bitmapDrawable.getBitmap());
            }
        });
    }

}
