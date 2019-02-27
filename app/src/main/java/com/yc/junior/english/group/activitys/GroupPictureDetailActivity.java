package com.yc.junior.english.group.activitys;

import android.support.v4.view.ViewPager;

import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.group.adapter.GroupPicTaskDetailAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/8/14 09:21.
 */

public class GroupPictureDetailActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public void init() {
        if (getIntent() != null) {
            List<String> paths = getIntent().getStringArrayListExtra("mList");
            int position = getIntent().getIntExtra("position", -1);
            viewPager.setAdapter(new GroupPicTaskDetailAdapter(this, paths));
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_picture_item;
    }

}
