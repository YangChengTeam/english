package com.yc.english.base.view;

import android.support.v7.app.AppCompatActivity;

import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupListContract;
import com.yc.english.group.model.engin.GroupListEngine;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public abstract class ToolbarFragment extends BaseFragment {

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
