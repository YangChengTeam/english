package com.yc.english.weixin.views.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yc.english.R;
import com.yc.english.base.view.ToolbarFragment;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypeFragment extends ToolbarFragment {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.fiv_indicator)
    FixedIndicatorView mFixedIndicatorView;

    private FragmentAdapter mFragmentAdapter;


    @Override
    public void init() {
        mToolbar.setTitle("每日微课");

        mFixedIndicatorView.setAdapter(new MyAdapter(3));

        mFixedIndicatorView.setScrollBar(new ColorBar(getActivity(), ContextCompat.getColor(getActivity(), R.color
                .primary), 6));

        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(getActivity(), R.color.primary);
        int unSelectColor = ContextCompat.getColor(getActivity(), R.color.black_333);
        mFixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        mFixedIndicatorView.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                mViewPager.setCurrentItem(position);
                return false;
            }
        });
        mFixedIndicatorView.setCurrentItem(0, true);

        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mFixedIndicatorView.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean isInstallToolbar() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course_type;
    }

    private CourseFragment courseFragment1;
    private CourseFragment courseFragment2;
    private CourseFragment courseFragment3;

    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (courseFragment1 == null) {
                    courseFragment1 = new CourseFragment();
                }
                return courseFragment1;
            } else if (position == 1) {
                if (courseFragment2 == null) {
                    courseFragment2 = new CourseFragment();
                }
                return courseFragment2;
            } else if (position == 2) {
                if (courseFragment3 == null) {
                    courseFragment3 = new CourseFragment();
                }
                return courseFragment3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class MyAdapter extends Indicator.IndicatorAdapter {
        private final String[] titles = new String[]{"图文微课", "音频微客", "视频微客"};

        private final int count;

        public MyAdapter(int count) {
            super();
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.weixin_tab, parent, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(titles[position]);
            return convertView;
        }
    }
}
