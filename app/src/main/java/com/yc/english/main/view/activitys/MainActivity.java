package com.yc.english.main.view.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.presenter.MainPresenter;
import com.yc.english.main.view.fragments.IndexFragment;
import com.yc.english.main.view.wdigets.TabBar;
import com.yc.english.setting.view.fragments.MyFragment;

import butterknife.BindView;


public class MainActivity extends FullScreenActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tabbar)
    TabBar mTabBar;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private FragmentAdapter mFragmentAdapter;
    private int mCurrentIndex;


    @Override
    public int getLayoutID() {
        return R.layout.main_activity_main;
    }

    @Override
    public void init() {
        mPresenter = new MainPresenter(this, this);
        mToolbar.showNavigationIcon();
        mTabBar.setOnTabSelectedListener(new TabBar.OnTabSelectedListener() {
            @Override
            public void onSelected(int idx) {
                mViewPager.setCurrentItem(idx);
            }
        });
        mTabBar.tab(0);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mCurrentIndex == position) {
                    return;
                }
                mCurrentIndex = position;
            }

            @Override
            public void onPageSelected(int position) {
                mTabBar.tab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class FragmentAdapter extends FragmentStatePagerAdapter {
        IndexFragment mIndexFragment;
        IndexFragment mIndexFragment2;
        MyFragment mMyFragment;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (mIndexFragment == null) {
                    mIndexFragment = new IndexFragment();
                }
                return mIndexFragment;
            } else if (position == 1) {
                if (mIndexFragment2 == null) {
                    mIndexFragment2 = new IndexFragment();
                }
                return mIndexFragment2;
            } else if (position == 2) {
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                }
                return mMyFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void show(String html) {

    }

}
