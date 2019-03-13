package com.yc.english.base.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kk.utils.ScreenUtil;
import com.yc.english.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.base.BaseView;

/**
 * Created by zhangkai on 2017/7/25.
 */

public abstract class BaseToolBar extends BaseView {
    private BaseActivity mActivity;
    protected boolean isShowNavigationIcon;


    @BindView(R.id.toolbar_sub)
    Toolbar mToolbar;

    @Nullable
    @BindView(R.id.toolbarWarpper)
    FrameLayout mtoolbarWarpper;

//

    public FrameLayout getToolbarWarpper() {
        return mtoolbarWarpper;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @BindView(R.id.tv_tb_title)
    protected TextView mTitleTextView;
    private Context mContext;

    public BaseToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void init(BaseActivity activity) {
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

        if (backClickListener != null) {
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    backClickListener.onClick(v);
                }
            });
        }
    }

    private OnClickListener backClickListener;

    public void setBackOnClickListener(final OnClickListener onClickListener) {
        backClickListener = onClickListener;
    }

    public void setOnMenuItemClickListener() {
        if (onItemClickLisener != null) {
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onItemClickLisener.onClick();
                    return false;
                }
            });
        }
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setTitleSize(float titleSize) {
        mTitleTextView.setTextSize(ScreenUtil.dip2px(mContext, titleSize));
    }

    public void setTitleGravity(int gravity) {
        mTitleTextView.setGravity(gravity);
    }

    public void showNavigationIcon() {
        mToolbar.setNavigationIcon(R.mipmap.base_back);
        isShowNavigationIcon = true;
    }

    public void clear() {
        mToolbar.getMenu().clear();
    }

    protected boolean hasMenu;

    protected int mIconResid = 0;

    protected String mMenuTitle;

    private OnItemClickLisener onItemClickLisener;

    public int getmIconResid() {
        return mIconResid;
    }

    public void setMenuIcon(int iconResid) {
        hasMenu = true;
        this.mIconResid = iconResid;
    }

    public String getMenuTitle() {
        return mMenuTitle;
    }

    public void setMenuTitle(String mMenuTitle) {
        hasMenu = true;
        this.mMenuTitle = mMenuTitle;
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

    public void setMenuTitleColor(int color) {

    }

    public interface OnItemClickLisener {
        void onClick();
    }


}
