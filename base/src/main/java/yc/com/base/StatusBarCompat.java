package yc.com.base;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.readystatesoftware.systembartint.SystemBarTintManager;

import yc.com.blankj.utilcode.util.SizeUtils;

/**
 * Created by zhangkai on 2017/11/27.
 */

public final class StatusBarCompat {
    public static void light(BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option = option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(option);
        }
    }

    public static void black(BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
        }
    }

    public static void transparentStatusBar(BaseActivity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            black(activity);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager
                            .LayoutParams
                            .FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(Color.TRANSPARENT);
        } else {
            View decorView = activity.getWindow().getDecorView();
            decorView.setFitsSystemWindows(true);
        }
    }

    public static void compat(BaseActivity activity, View mToolbarWarpper, View mToolBar) {
        compat(activity, mToolbarWarpper, mToolBar, null, 0);
    }

    public static void compat(BaseActivity activity, View mToolBar, int drawableId) {
        compat(activity, null, mToolBar, null, drawableId);
    }

    public static void compat(BaseActivity activity, View mToolbarWarpper, View mToolBar, int drawableId) {
        compat(activity, mToolbarWarpper, mToolBar, null, drawableId);
    }

    public static void compat(BaseActivity activity, View statusBar) {
        compat(activity, null, null, statusBar, 0);
    }

    public static void compat(BaseActivity activity, View mToolbarWarpper, View mToolBar, View mStatusBar) {
        compat(activity, mToolbarWarpper, mToolBar, mStatusBar, 0);
    }

    public static void compat(BaseActivity activity, View mToolbarWarpper, View mToolBar, View mStatusBar, int
            drawableId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (mToolbarWarpper != null) {
                mToolBar.getLayoutParams().height = SizeUtils.dp2px(48f);
                mToolBar.requestLayout();
                if (drawableId != 0) {
                    mToolbarWarpper.setBackground(ContextCompat.getDrawable(activity, drawableId));
                    mToolbarWarpper.requestLayout();
                } else {
                    mToolbarWarpper.getLayoutParams().height = SizeUtils.dp2px(48f);
                }
            }
            if (mStatusBar != null) {
                mStatusBar.getLayoutParams().height = 0;
            }
        } else {

            if (mStatusBar != null) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mStatusBar.getLayoutParams().height = activity.getStatusHeight();
                }else{
                    mStatusBar.getLayoutParams().height = 0;
                }
            }

            if (mToolBar != null) {
                mToolBar.getLayoutParams().height = SizeUtils.dp2px(72f) - activity.getStatusHeight();
                activity.setToolbarTopMargin(mToolBar);
            }
        }
    }
}
