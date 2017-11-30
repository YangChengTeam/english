package com.yc.english.vip.views.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.yc.english.R;
import com.yc.english.pay.alipay.SignUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/11/27 15:16.
 */

public class BasePayDialogFragment extends DialogFragment

{
    @BindView(R.id.m_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.btn_pay)
    Button btnPay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);//((ViewGroup) window.findViewById(android.R.id.content))
        View view = inflater.inflate(R.layout.base_pay_popupwindow, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight() * 2 / 3);//这2行,和上面的一样,注意顺序就行;
        window.setWindowAnimations(R.style.vip_style);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setCurrentItem(0);
        final View customView = LayoutInflater.from(getActivity()).inflate(R.layout.vip_tab_item, null);
        final TextView textView = customView.findViewById(R.id.tab_vip);
        mTabLayout.getTabAt(0).setCustomView(customView);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_333));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private BaseVipPayFragment vipPayFragment;
    private BaseVipPayFragment generalFragment;
    private BaseVipPayFragment singleFragment;

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] titles;

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
            titles = getActivity().getResources().getStringArray(R.array.vip);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (vipPayFragment == null) {
                    vipPayFragment = new BaseVipPayFragment();
                    vipPayFragment.setType(1);

                }
                return vipPayFragment;
            } else if (position == 1) {
                if (generalFragment == null) {
                    generalFragment = new BaseVipPayFragment();
                    generalFragment.setType(2);
                }
                return generalFragment;
            } else if (position == 2) {
                if (singleFragment == null) {
                    singleFragment = new BaseVipPayFragment();
                    singleFragment.setType(3);
                }
                return singleFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
