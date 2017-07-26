package com.yc.english.base.view;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;
import com.yc.english.R;
import com.yc.english.main.view.activitys.MainActivity;

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
        mToolbar.init((AppCompatActivity) getActivity());
    }
}
