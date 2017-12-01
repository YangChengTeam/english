package com.yc.english.vip.views.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.vip.model.bean.GoodsType;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/11/27 15:16.
 */

public class BasePayDialogFragment extends BaseDialogFragment {
    @BindView(R.id.m_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.btn_pay)
    Button btnPay;

    private List<String> mTitles = new ArrayList<>();
    private int goodsType;
    private CourseInfo courseInfo;


    @Override
    public void init() {
        mTitles.add(getString(R.string.tutorship));
        mTitles.add(getString(R.string.member));

        Bundle bundle = getArguments();
        if (bundle != null) {
            goodsType = bundle.getInt(GoodsType.GOODS_KEY);
            if (goodsType == GoodsType.GENERAL_TYPE_WEIKE || goodsType == GoodsType.GENERAL_TYPE_DIANDU) {
                //显示三项
                mTitles.add(getString(R.string.sigle_buy));
                if (bundle.getParcelable("courseInfo") != null) {
                    courseInfo = bundle.getParcelable("courseInfo");

                }
            }

        }

        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mTitles));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
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
        RxView.clicks(btnPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
//                OrderParams orderParams = new OrderParams();
//                orderParams.setTitle(goodInfo.getName());
//                orderParams.setMoney(goodInfo.getPay_price());
//                orderParams.setPayWayName(pay_way_name);
//                List<OrderGood> list = new ArrayList<>();
//                OrderGood orderGood = new OrderGood();
//                orderGood.setGood_id(goodInfo.getId());
//                orderGood.setNum(1);
//
//                list.add(orderGood);
//                orderParams.setGoodsList(list);
//
//                mPresenter.createOrder(orderParams);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_pay_popupwindow;
    }


    private BaseVipPayFragment vipPayFragment;
    private BaseVipPayFragment generalFragment;
    private BaseVipPayFragment singleFragment;

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private List<String> mTitles;

        private MyPagerAdapter(FragmentManager fm, List<String> titles) {
            super(fm);
            this.mTitles = titles;

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
                    singleFragment.setGeneralType(goodsType);
                    singleFragment.setCourserInfo(courseInfo);
                }
                return singleFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }

    //支付宝支付
    private void aliPay() {

    }

    //微信支付
    private void wxPay() {
    }

}
