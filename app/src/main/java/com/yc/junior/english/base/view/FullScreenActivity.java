package com.yc.junior.english.base.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.yc.junior.english.R;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.base.utils.StatusBarCompat;

import butterknife.BindView;
import yc.com.blankj.utilcode.util.SizeUtils;

/**
 * Created by zhangkai on 2017/7/24.
 */

public abstract class FullScreenActivity<P extends BasePresenter> extends BaseActivity<P> {

    @BindView(R.id.toolbar)
    protected BaseToolBar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mToolbar == null) {
            throw new NullPointerException("error, please set com.yc.english.main.view.MainToolBar id -> toolbar.");
        }
        mToolbar.init(this);
        if (mToolbar instanceof MainToolBar) {
            StatusBarCompat.compat(this, mToolbar, mToolbar.getToolbar(), R.mipmap.base_actionbar);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mToolbar.getLayoutParams().height = SizeUtils.dp2px(48f);
            }
        } else if (mToolbar instanceof TaskToolBar && hasChangeStatus) {

            StatusBarCompat.light(this);
            StatusBarCompat.compat(this, mToolbar, mToolbar.getToolbarWarpper(), ((TaskToolBar) mToolbar).getStatusBar());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mToolbar.isHasMenu()) {
            getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
            mToolbar.setOnMenuItemClickListener();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean result = super.onPrepareOptionsMenu(menu);
        if (mToolbar.isHasMenu()) {
            MenuItem menuItem = menu.findItem(R.id.action);
            menuItem.setTitle(mToolbar.getMenuTitle());
            if (mToolbar.getmIconResid() != 0) {
                menuItem.setIcon(mToolbar.getmIconResid());
            }
        }
        return result;
    }
}