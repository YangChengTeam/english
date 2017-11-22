package com.yc.english.base.view;

import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public abstract class ToolbarFragment<P extends BasePresenter> extends BaseFragment<P> {

    @BindView(R.id.toolbar)
    protected BaseToolBar mToolbar;

    @Override
    public void init() {
        if (mToolbar == null) {
            throw new NullPointerException("error, please set com.yc.english.main.view.MainToolBar id -> toolbar.");
        }

        if (isInstallToolbar()) {
            mToolbar.init((AppCompatActivity) getActivity());
            getActivity().invalidateOptionsMenu();
        }
    }


    public BaseToolBar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public abstract boolean isInstallToolbar();
}
