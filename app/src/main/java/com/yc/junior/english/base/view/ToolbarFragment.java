package com.yc.junior.english.base.view;

import com.yc.junior.english.R;
import com.yc.junior.english.base.presenter.BasePresenter;

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
            throw new NullPointerException("error, please set com.yc.junior.english.main.view.MainToolBar id -> toolbar.");
        }

        if (isInstallToolbar()) {
            mToolbar.init((BaseActivity) getActivity());
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
