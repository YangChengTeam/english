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

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.pay.PayConfig;
import com.yc.english.pay.alipay.IAliPay1Impl;
import com.yc.english.pay.alipay.IPayCallback;
import com.yc.english.pay.alipay.IWXPay1Impl;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.setting.model.bean.GoodInfoWrapper;
import com.yc.english.vip.contract.VipBuyContract;
import com.yc.english.vip.model.bean.GoodsType;
import com.yc.english.vip.presenter.VipBuyPresenter;
import com.yc.english.vip.utils.VipDialogHelper;
import com.yc.english.vip.utils.VipInfoHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/11/27 15:16.
 */

public class BasePayDialogFragment extends BaseDialogFragment<VipBuyPresenter> implements VipBuyContract.View, BaseVipPayFragment.onVipClickListener {
    @BindView(R.id.m_tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.btn_pay)
    Button btnPay;

    private List<String> mTitles = new ArrayList<>();
    private int goodsType;
    private CourseInfo courseInfo;
    private BaseVipPayFragment currentFragment;

    private GoodInfo mGoodInfo;
    private String mPayWayName;

    private IAliPay1Impl iAliPay;
    private IWXPay1Impl iwxPay;

    @Override
    public void init() {
        mPresenter = new VipBuyPresenter(getActivity(), this);
        iAliPay = new IAliPay1Impl(getActivity());
        iwxPay = new IWXPay1Impl(getActivity());

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
        restoreGoodInfoAndPayway(0);
        initListener();

    }

    private void initListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                restoreGoodInfoAndPayway(position);
                switch (position) {
                    case 0:
                        currentFragment = vipPayFragment;
                        break;
                    case 1:
                        currentFragment = generalFragment;
                        break;
                    case 2:
                        currentFragment = singleFragment;
                        break;
                }
                if (currentFragment != null) {
                    currentFragment.setOnVipClickListener(BasePayDialogFragment.this);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RxView.clicks(btnPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                OrderParams orderParams = new OrderParams();
                orderParams.setTitle(mGoodInfo.getName());
                orderParams.setMoney(mGoodInfo.getPay_price());
                orderParams.setPayWayName(mPayWayName);
                List<OrderGood> list = new ArrayList<>();
                OrderGood orderGood = new OrderGood();
                orderGood.setGood_id(mGoodInfo.getId());
                orderGood.setNum(1);

                list.add(orderGood);
                orderParams.setGoodsList(list);
                mPresenter.createOrder(orderParams);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_pay_popupwindow;
    }

    private void restoreGoodInfoAndPayway(int postion) {
        mPayWayName = PayConfig.ali_pay;
        GoodInfoWrapper goodInfoWrapper = VipInfoHelper.getGoodInfoWrapper();
        if (postion == 0) {
            if (goodInfoWrapper.getSvip() != null && goodInfoWrapper.getSvip().size() > 0)
                mGoodInfo = VipInfoHelper.getGoodInfoWrapper().getSvip().get(0);
        } else if (postion == 1) {
            if (goodInfoWrapper.getVip() != null && goodInfoWrapper.getVip().size() > 0)
                mGoodInfo = VipInfoHelper.getGoodInfoWrapper().getVip().get(0);
        } else if (postion == 2) {
            mGoodInfo = new GoodInfo();
            if (courseInfo != null) {
                mGoodInfo.setId(courseInfo.getGoodId());
                mGoodInfo.setPay_price(courseInfo.getPayPrice() + "");
                mGoodInfo.setName("商品购买");
            }
        }

    }

    @Override
    public void showOrderInfo(OrderInfo orderInfo, String money, String name) {
        orderInfo.setMoney(Float.parseFloat(money));
        orderInfo.setName(name);
        if (mPayWayName.equals(PayConfig.ali_pay)) {
            aliPay(orderInfo);
        } else {
            wxPay(orderInfo);
        }
    }

    private BaseVipPayFragment vipPayFragment;
    private BaseVipPayFragment generalFragment;
    private BaseVipPayFragment singleFragment;

    @Override
    public void showLoadingDialog(String msg) {
        ((BaseActivity) getActivity()).showLoadingDialog(msg);
    }

    @Override
    public void dismissLoadingDialog() {
        ((BaseActivity) getActivity()).dismissLoadingDialog();
    }


    @Override
    public void onVipClick(GoodInfo goodInfo, String payWayName, int type) {
        switch (type) {
            case 1:
            case 2:
                mGoodInfo = goodInfo;
                mPayWayName = payWayName;
                break;
            case 3:
                mPayWayName = payWayName;
                break;

        }

        LogUtils.e(mGoodInfo.getPay_price() + "---" + payWayName);
    }


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
                vipPayFragment.setOnVipClickListener(BasePayDialogFragment.this);
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

    // 微信支付
    private void wxPay(OrderInfo orderInfo) {
        iwxPay.pay(orderInfo, new IPayCallback() {
            @Override
            public void onSuccess(OrderInfo orderInfo) {
                updateSuccessData();
            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
                ToastUtils.showLong("支付失败");
            }
        });
    }

    //支付宝支付
    private void aliPay(OrderInfo orderInfo) {
        iAliPay.pay(orderInfo, new IPayCallback() {
            @Override
            public void onSuccess(OrderInfo orderInfo) {
                updateSuccessData();
            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
            }
        });
    }

    private void updateSuccessData() {
        UserInfoHelper.getUserInfo().setIsVip(1);
//        Date date = new Date();
//        UserInfoHelper.getUserInfo().setVip_start_time(date.getTime() / 1000);
//        int use_time_Limit = Integer.parseInt(goodInfo.getUse_time_limit());
//        long vip_end_time = date.getTime() + use_time_Limit * 30 * (Config.MS_IN_A_DAY);
//        UserInfoHelper.getUserInfo().setVip_end_time(vip_end_time / 1000);
        VipDialogHelper.dismissVipDialog();
    }

}
