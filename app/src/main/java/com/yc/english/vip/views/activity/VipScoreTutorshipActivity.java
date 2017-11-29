package com.yc.english.vip.views.activity;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.yc.english.R;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.vip.views.fragments.VipTutorshipDetailFragment;
import com.yc.english.vip.views.fragments.VipUserEvaluateFragment;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/11/28 15:05.
 */

public class VipScoreTutorshipActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.score_tabLayout)
    TabLayout scoreTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @BindView(R.id.btn_pay)
    Button mBtnPay;

    @BindView(R.id.toolbarWarpper)
    ImageView mToolbarWarpper;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;

    private String[] mTitles = {"辅导详情", "用户评价"};


    @Override
    public void init() {


        StatusBarCompat.compat(this, mToolbarWarpper, toolbar);

        mViewPager.setAdapter(new MyFragmentPager(getSupportFragmentManager()));
        scoreTabLayout.setupWithViewPager(mViewPager);
        toolbar.setNavigationIcon(R.mipmap.vip_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                LogUtils.e(verticalOffset + "--" + appBarLayout.getHeight() + "--" + collapsingToolbarLayout.getHeight());
                if (-verticalOffset + collapsingToolbarLayout.getHeight() >= appBarLayout.getHeight()) {
                    collapsingToolbarLayout.setTitle("提分辅导");
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_score_tutorship;
    }

    private VipTutorshipDetailFragment vipTutorshipDetailFragment;
    private VipUserEvaluateFragment vipUserEvaluateFragment;


    private class MyFragmentPager extends FragmentStatePagerAdapter {
        private MyFragmentPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (vipTutorshipDetailFragment == null) {
                    vipTutorshipDetailFragment = new VipTutorshipDetailFragment();
                }
                return vipTutorshipDetailFragment;
            } else if (position == 1) {
                if (vipUserEvaluateFragment == null) {
                    vipUserEvaluateFragment = new VipUserEvaluateFragment();

                }
                return vipUserEvaluateFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }


}
