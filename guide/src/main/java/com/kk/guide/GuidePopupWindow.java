package com.kk.guide;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Size;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkai on 2017/6/16.
 */

public class GuidePopupWindow extends PopupWindow {
    private ColorDrawable mBackgroundDrawable;
    private Activity mContext;
    private FrameLayout mRootView;
    private float mDelay;
    private List<ImageInfo> imageInfos;
    private float pl;
    private float pt;
    private float pr;
    private float pb;

    public GuidePopupWindow(Activity context) {
        super(context);
        this.mContext = context;
        imageInfos = new ArrayList<>();
        mRootView = new FrameLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mRootView.setLayoutParams(layoutParams);
        this.setContentView(mRootView);

        this.setOutsideTouchable(false);
        this.setClippingEnabled(false);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = context.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1.0f;
        window.setAttributes(lp);
    }

    public GuidePopupWindow(final Activity context, final Builder builder) {
        this(context);
        this.mDelay = builder.delay;
        final View targetView = builder.targetView;
        targetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int[] locations = new int[2];
                targetView.getLocationOnScreen(locations);
                int x = locations[0];
                int y = locations[1];
                int w = targetView.getWidth();
                int h = targetView.getHeight();
                GuideView guideView = new GuideView(context);
                guideView.setCallback(builder.callback);
                guideView.setBgAlpha(builder.alpha);
                guideView.setGuidePopupWindow(GuidePopupWindow.this);
                guideView.setType(builder.type);
                guideView.setMeasure(x, y, x + w, y + h);
                guideView.setOffset(pl, pt, pr, pt);
                mRootView.addView(guideView);
                targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                for (ImageInfo imageInfo : imageInfos) {
                    addImageView(imageInfo);
                }
            }
        });
    }

    public void setOffset(float pl, float pt, float pr, float pb) {
        this.pl = pl;
        this.pt = pt;
        this.pr = pr;
        this.pb = pb;
    }

    public void addCustomView(final View customView, final int x, final int y) {
        if (customView != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(x, y, 0, 0);
            customView.setLayoutParams(layoutParams);
            mRootView.addView(customView);
            mRootView.bringChildToFront(customView);
            mRootView.updateViewLayout(customView, customView.getLayoutParams());
        }
    }

    public void addImageView(int imageId, int x, int y, int w, int h, View.OnClickListener onClickListener) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setImageId(imageId);
        imageInfo.setX(x);
        imageInfo.setY(y);
        imageInfo.setW(w);
        imageInfo.setH(h);
        imageInfo.setOnClickListener(onClickListener);
        imageInfos.add(imageInfo);
    }

    private void addImageView(ImageInfo imageInfo) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(ContextCompat.getDrawable(mContext, imageInfo.getImageId()));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = GuideUtil.dip2px(mContext, imageInfo.getW() / 3);
        layoutParams.height = GuideUtil.dip2px(mContext, imageInfo.getH() / 3);
        layoutParams.setMargins( GuideUtil.dip2px(mContext, imageInfo.getX() / 3),  GuideUtil.dip2px(mContext,
                imageInfo.getY() / 3), 0, 0);
        if (imageInfo.getOnClickListener() != null) {
            imageView.setOnClickListener(imageInfo.getOnClickListener());
        }
        imageView.setLayoutParams(layoutParams);
        mRootView.addView(imageView);
        mRootView.bringChildToFront(imageView);
        mRootView.updateViewLayout(imageView, imageView.getLayoutParams());
    }

    public void addImageView(int imageId, int x, int y, int w, int h) {
        addImageView(imageId, x, y, w, h, null);
    }


    public void addCustomView(int layoudId, int x, int y) {
        View customeView = mContext.getLayoutInflater().inflate(layoudId, null);
        addCustomView(customeView, x, y);
    }

    public void show(final String key) {
        if (GuideUtil.getString(mContext, key).isEmpty()) {
            GuideUtil.postDelayed((long) mDelay * 1000, new Runnable() {
                @Override
                public void run() {
                    View rootView = mContext.getWindow().getDecorView().getRootView();
                    GuidePopupWindow.this.showAtLocation(rootView
                            , Gravity.NO_GRAVITY, 0, 0);
                    GuideUtil.putString(mContext, key, key);
                }
            });
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Builder {
        private View targetView;
        private GuideCallback callback;
        private float alpha = 0.3f;
        private float delay = 1.0f;
        private GuideView.GuideType type = GuideView.GuideType.OVAL;

        public Builder setType(GuideView.GuideType type) {
            this.type = type;
            return this;
        }


        public Builder setAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Builder setTargetView(final View targetView) {
            this.targetView = targetView;
            return this;
        }

        public Builder setDelay(float delay) {
            this.delay = delay;
            return this;
        }

        public Builder setGuideCallback(final GuideCallback callback) {
            this.callback = callback;
            return this;
        }

        public GuidePopupWindow build(Activity context) {
            return new GuidePopupWindow(context, this);
        }
    }


    @Override
    public void setContentView(View contentView) {
        if (contentView != null) {
            super.setContentView(contentView);
            setFocusable(true);
            setTouchable(true);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        if (touchable) {
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0x00000000);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }

}
