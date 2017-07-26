package com.yc.english.main.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.ForgotContract;
import com.yc.english.main.presenter.ForgotPresenter;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class ForgotActivity extends FullScreenActivity<ForgotPresenter> implements ForgotContract.View {
    @Override
    public void init() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.main_activity_forgot;
    }
}
