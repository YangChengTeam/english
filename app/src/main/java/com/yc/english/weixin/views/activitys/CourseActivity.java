package com.yc.english.weixin.views.activitys;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.weixin.views.fragments.CourseFragment;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseActivity extends FullScreenActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.indicator)
    ScrollIndicatorView mIndicator;

    private IndicatorViewPager mIndicatorViewPager;


    @Override
    public void init() {
        mToolbar.setTitle("每日微课");
        mToolbar.showNavigationIcon();

        float unSelectSize = 14;
        float selectSize = 14;
        mIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(Color.parseColor
                ("#21b5f8"), Color.parseColor
                ("#333333")).setSize
                (selectSize,
                        unSelectSize));
        mIndicator.setScrollBar(new ColorBar(this, 0xFF21b5f8, 6));
        mViewPager.setOffscreenPageLimit(2);
        mIndicatorViewPager = new IndicatorViewPager(mIndicator, mViewPager);
        mIndicatorViewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_activity_course;
    }

    private class PageAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] versions = {"词法", "句法", "时态", "语态", "情景交际", "微信公众号"};

        @Override
        public int getCount() {
            return versions.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.weixin_tab, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(versions[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            return new CourseFragment();
        }


        public PageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_UNCHANGED;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }

    }
}
