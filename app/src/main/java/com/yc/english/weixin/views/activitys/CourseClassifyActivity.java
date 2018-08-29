package com.yc.english.weixin.views.activitys;

import android.content.Intent;
import android.os.Bundle;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.weixin.views.fragments.CourseFragment;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class CourseClassifyActivity extends FullScreenActivity {


    @Override
    public void init() {

        Intent intent = getIntent();
        if (intent != null) {
            int type = intent.getIntExtra("type", 0);

            if (type == 7) {
                mToolbar.setTitle("每日音频");
            } else if (type == 8) {
                mToolbar.setTitle("每日视频");
            }

            CourseFragment courseFragment = new CourseFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", type + "");
            courseFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, courseFragment).commit();

        }
        mToolbar.showNavigationIcon();


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_classify;
    }


}
