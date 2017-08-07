package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.view.adapter.CommonAdapter;
import com.yc.english.group.view.adapter.GroupPageAdapter;
import com.yc.english.group.view.fragments.GroupFinishTaskFragment;
import com.yc.english.group.view.fragments.GroupLookTaskFragment;
import com.yc.english.group.view.fragments.GroupUnFinishTaskFragment;
import com.yc.english.group.view.fragments.GroupUnLookTaskFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/28 12:55.
 * 已查阅 未查阅
 */

public class GroupTaskLookAndUnLookActivity extends FullScreenActivity {
    @BindView(R.id.m_tv_issue_time)
    TextView mTvIssueTime;
    @BindView(R.id.m_ll_task_detail)
    LinearLayout mLlTaskDetail;
    @BindView(R.id.m_magicIndicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.m_viewPager)
    ViewPager mViewPager;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonAdapter(this, 2, mViewPager, 3, 17));

        commonNavigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(commonNavigator);
        fragments.add(new GroupLookTaskFragment());
        fragments.add(new GroupUnLookTaskFragment());

        mViewPager.setAdapter(new GroupPageAdapter(getSupportFragmentManager(), fragments));

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

        initListener();


    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupTaskLookAndUnLookActivity.this, GroupPublishTaskListActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_item_detail;
    }


}
