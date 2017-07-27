package com.yc.english.setting.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class ModifyPasswordActivity extends FullScreenActivity{
    @Override
    public void init() {
        mToolbar.setTitle("修改密码");
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_activity_modify_password;
    }
}
