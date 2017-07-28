package com.yc.english.group.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by wanglin  on 2017/7/26 18:20.
 */

public class GroupChangeNameActivity extends FullScreenActivity {


    @Override
    public void init() {
        mToolbar.setTitle(getResources().getString(R.string.group_name));
        mToolbar.showNavigationIcon();

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_change_class;
    }


}
