package com.yc.english.speak.view.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.speak.view.fragment.SpeakFragment;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/10/12 14:33.
 */

public class SpeakMainActivity extends FullScreenActivity {
    @BindView(R.id.fiv_indicator)
    FixedIndicatorView fivIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewpager;

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
        fivIndicator.setAdapter(new PagerIndicator(this));
        fivIndicator.setScrollBar(new ColorBar(this, ContextCompat.getColor(this, R.color
                .primary), 6));

        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(this, R.color.primary);
        int unSelectColor = ContextCompat.getColor(this, R.color.black_333);
        fivIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        fivIndicator.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                viewpager.setCurrentItem(position);
                return false;
            }
        });
        fivIndicator.setCurrentItem(0, true);

        MyFragmentAdapter mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(2);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                fivIndicator.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private class PagerIndicator extends Indicator.IndicatorAdapter {

        protected LayoutInflater layoutInflater;

        private PagerIndicator(Context context) {

            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.weixin_tab, parent, false);
            }
            TextView tv = ((TextView) convertView);
            tv.setText(mTitles[position]);
            return convertView;
        }

    }

    private SpeakFragment speakFragment;
    private SpeakFragment listenFragment;

    private class MyFragmentAdapter extends FragmentStatePagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
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
