package com.yc.junior.english.group.activitys;

import com.yc.junior.english.R;
import com.yc.junior.english.group.adapter.GroupPicTaskDetailAdapter;

import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import yc.com.base.BaseActivity;


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

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
