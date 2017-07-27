package com.yc.english.base.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.yc.english.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/25.
 */

public abstract class BaseToolBar extends BaseView {
    private AppCompatActivity mActivity;
    protected boolean isShowNavigationIcon;

    @BindView(R.id.toolbar_sub)
    Toolbar mToolbar;

    @BindView(R.id.tv_tb_title)
    protected TextView mTitleTextView;

    public BaseToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(AppCompatActivity activity) {
        mToolbar.setTitle("");
        mActivity = activity;
        activity.setSupportActionBar(mToolbar);
        if (isShowNavigationIcon) {
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener;

    public void setOnMenuItemClickListener() {
        if(onMenuItemClickListener != null) {
            mToolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }

    public void setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }


    public void setTitle(String title) {
        mTitleTextView.setTypeface(null, Typeface.BOLD);
        mTitleTextView.setText(title);
    }

    public void showNavigationIcon() {
        mToolbar.setNavigationIcon(R.mipmap.base_back);
        isShowNavigationIcon = true;
    }

    private boolean hasMenu;

    private int iconResid;

    private String menuTitle;

    public int getIconResid() {
        return iconResid;
    }

    public void setIconResid(int iconResid) {
        hasMenu = true;
        this.iconResid = iconResid;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        hasMenu = true;
        this.menuTitle = menuTitle;
    }

    public boolean isHasMenu() {
        return hasMenu;
    }
}
