package com.yc.junior.english.speak.view.activity;

import com.google.android.material.tabs.TabLayout;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.speak.view.fragment.SpeakFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
        mToolbar.setTitle(getString(R.string.dub_language));
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

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

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
