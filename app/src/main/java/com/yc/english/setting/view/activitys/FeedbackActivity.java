package com.yc.english.setting.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class FeedbackActivity extends FullScreenActivity {
    @Override
    public void init() {
        mToolbar.setTitle("意见反馈");
        mToolbar.showNavigationIcon();

    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_activity_feedback;
    }
}
