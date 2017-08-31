package com.kk.guide;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
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
    private boolean isDebug;
    private View customeView;
    private View okButton;

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public GuidePopupWindow(Activity context) {
        super(context);
        this.mContext = context;
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
                guideView.setBorder(builder.corner);
                guideView.setMeasure(x, y, x + w, y + h);
                mRootView.addView(guideView);
                targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                addCustomView(customeView);
            }
        });
    }


    public void addCustomView(final View customView) {
        if (customView != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);
            customView.setLayoutParams(layoutParams);
            mRootView.addView(customView);
            mRootView.bringChildToFront(customView);
            mRootView.updateViewLayout(customView, customView.getLayoutParams());
        }
    }

    public void addCustomView(int layoudId, int okid, final View.OnClickListener onClickListener) {
        customeView = mContext.getLayoutInflater().inflate(layoudId, null);

        if(okid == 0) return;

        okButton = customeView.findViewById(okid);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public void addCustomView(int layoudId) {
        addCustomView(layoudId, 0, null);
    }

    public void show(final String key) {
        if (GuideUtil.getString(mContext, key).isEmpty() || isDebug) {
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

    public void show(final View view, final String key) {
        if (GuideUtil.getString(mContext, key).isEmpty() || isDebug) {
            GuideUtil.postDelayed((long) mDelay * 1000, new Runnable() {
                @Override
                public void run() {
                    GuidePopupWindow.this.showAtLocation(view
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
        private float corner = 3f;

        public Builder setCorner(float corner) {
            this.corner = corner;
            return this;
        }

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
