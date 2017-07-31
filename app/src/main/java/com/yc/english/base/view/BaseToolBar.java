package com.yc.english.base.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    public void setBackOnClickListener(final OnClickListener onClickListener){
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
            }
        });
    }

    public void setOnMenuItemClickListener() {
        if(onItemClickLisener != null) {
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

    public void showNavigationIcon() {
        mToolbar.setNavigationIcon(R.mipmap.base_back);
        isShowNavigationIcon = true;
    }

    public void clear(){
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

    public void setTitleColor(int color){
        mTitleTextView.setTextColor(color);
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

    public  void setMenuTitleColor(int color){

    }

    public interface OnItemClickLisener {
            void onClick();
    }



}
