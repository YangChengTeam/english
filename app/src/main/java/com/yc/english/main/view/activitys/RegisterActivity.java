package com.yc.english.main.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.RegisterContract;
import com.yc.english.main.presenter.RegisterPresenter;

/**
 * Created by zhangkai on 2017/7/21.
 */

public class RegisterActivity extends FullScreenActivity<RegisterPresenter> implements RegisterContract.View{
    @Override
    public void init() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.main_activity_register;
    }
}
