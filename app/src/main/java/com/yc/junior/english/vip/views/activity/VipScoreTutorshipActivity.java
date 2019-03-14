package com.yc.junior.english.vip.views.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.RelativeLayout;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.vip.model.bean.GoodsType;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.junior.english.vip.views.fragments.VipTutorshipDetailFragment;
import com.yc.junior.english.vip.views.fragments.VipUserEvaluateFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarCompat;


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
    @BindView(R.id.rl_btn)
    RelativeLayout rlBtn;

    private String[] mTitles = {"辅导详情", "用户评价"};

    private boolean isVip = false;

    @Override
    public void init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarCompat.compat(this, mToolbarWarpper, toolbar);
        }
        mViewPager.setAdapter(new MyFragmentPager(getSupportFragmentManager()));
        scoreTabLayout.setupWithViewPager(mViewPager);
        toolbar.setNavigationIcon(R.mipmap.vip_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null && userInfo.getIsVip() == 1) {
            isVip = true;
        }

        rlBtn.setVisibility(isVip ? View.GONE : View.VISIBLE);

        initListener();


    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.USER_INFO)
            }
    )
    public void getUserInfo(UserInfo userInfo) {

        rlBtn.setVisibility(userInfo.getIsVip() == 0 ? View.VISIBLE : View.GONE);

        getWindow().getDecorView().invalidate();

    }

    private void initListener() {
        RxView.clicks(mBtnPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (UserInfoHelper.getUserInfo() == null) {
                    UserInfoHelper.isGotoLogin(VipScoreTutorshipActivity.this);
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_GENERAL_VIP);
                VipDialogHelper.showVipDialog(getSupportFragmentManager(), "", bundle);
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_score_tutorship;
    }

    private VipTutorshipDetailFragment vipTutorshipDetailFragment;
    private VipUserEvaluateFragment vipUserEvaluateFragment;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }


    private class MyFragmentPager extends FragmentStatePagerAdapter {
        private MyFragmentPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (vipTutorshipDetailFragment == null) {
                    vipTutorshipDetailFragment = new VipTutorshipDetailFragment();
                    vipTutorshipDetailFragment.setIsVip(isVip);
                }
                return vipTutorshipDetailFragment;
            } else if (position == 1) {
                if (vipUserEvaluateFragment == null) {
                    vipUserEvaluateFragment = new VipUserEvaluateFragment();
                    vipUserEvaluateFragment.setIsVip(isVip);
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
