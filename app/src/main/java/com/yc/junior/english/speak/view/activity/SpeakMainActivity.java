package com.yc.junior.english.speak.view.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.speak.view.fragment.SpeakFragment;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/10/12 14:33.
 */

public class SpeakMainActivity extends FullScreenActivity {

    @BindView(R.id.viewPager)
    ViewPager viewpager;
    @BindView(R.id.m_tabLayout)
    TabLayout mTabLayout;

    private String[] mTitles = new String[]{"说英语", "听英语"};

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.spoken_language));
        mToolbar.showNavigationIcon();
        initViewPager();

    }

    @Override
    public int getLayoutId() {
        return R.layout.speak_activity_main_view;
    }

    private void initViewPager() {
        MyFragmentAdapter mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(viewpager);
    }

    private SpeakFragment speakFragment;
    private SpeakFragment listenFragment;

    private class MyFragmentAdapter extends FragmentStatePagerAdapter {

        private MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                if (speakFragment == null) {
                    speakFragment = new SpeakFragment();
                    speakFragment.setType(1);
                }
                return speakFragment;
            } else if (position == 1) {
                if (listenFragment == null) {
                    listenFragment = new SpeakFragment();
                    listenFragment.setType(2);
                }
                return listenFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }
    }
}
