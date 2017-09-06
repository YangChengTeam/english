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
import com.yc.english.weixin.views.utils.TabsUtils;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypeFragment extends ToolbarFragment {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.fiv_indicator)
    FixedIndicatorView mFixedIndicatorView;


    private final String[] titles = new String[]{"音频微课", "视频微课"};

    @Override
    public void init() {
        mToolbar.setTitle("每日微课");
        mFixedIndicatorView.setAdapter(new TabsUtils.MyAdapter(getActivity(), titles));
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

        TabsUtils.MyFragmentAdapter mFragmentAdapter = new TabsUtils.MyFragmentAdapter(getChildFragmentManager(),
                new String[]{"7", "8"});
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

}
