package com.yc.junior.english.vip.views.fragments;


import android.content.Intent;
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
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseActivity;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.news.model.domain.OrderGood;
import com.yc.junior.english.news.model.domain.OrderParams;
import com.yc.junior.english.pay.PayConfig;
import com.yc.junior.english.pay.alipay.IAliPay1Impl;
import com.yc.junior.english.pay.alipay.IPayCallback;
import com.yc.junior.english.pay.alipay.IWXPay1Impl;
import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.setting.model.bean.Config;
import com.yc.junior.english.setting.model.bean.GoodInfo;
import com.yc.junior.english.setting.model.bean.GoodInfoWrapper;
import com.yc.junior.english.vip.contract.VipBuyContract;
import com.yc.junior.english.vip.model.bean.GoodsType;
import com.yc.junior.english.vip.presenter.VipBuyPresenter;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.junior.english.vip.utils.VipInfoHelper;
import com.yc.junior.english.vip.views.activity.ProtocolActivity;
import com.yc.junior.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.Date;
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
    @BindView(R.id.tv_right_introduce)
    TextView tvRightIntroduce;


    private List<String> mTitles = new ArrayList<>();
    private List<BaseVipPayFragment> fragmentList = new ArrayList<>();

    private int goodsType = GoodsType.TYPE_SVIP;


    private GoodInfo mGoodInfo;
    private String mPayWayName;

    private IAliPay1Impl iAliPay;
    private IWXPay1Impl iwxPay;

    private int currentPosition = 0;

    @Override
    public void init() {
        mPresenter = new VipBuyPresenter(getActivity(), this);
        iAliPay = new IAliPay1Impl(getActivity());
        iwxPay = new IWXPay1Impl(getActivity());

        mTitles.add(getString(R.string.member));
        mTitles.add(getString(R.string.tutorship));

        BaseVipPayFragment svipPayFragment = new BaseVipPayFragment();//SVIP
        svipPayFragment.setType(GoodsType.TYPE_SVIP);
        BaseVipPayFragment gVipPayFragment = new BaseVipPayFragment();//GVIP
        gVipPayFragment.setType(GoodsType.TYPE_GENERAL_VIP);
        fragmentList.add(gVipPayFragment);
        fragmentList.add(svipPayFragment);

        Bundle bundle = getArguments();
        if (bundle != null) {
            goodsType = bundle.getInt(GoodsType.GOODS_KEY);
            if (goodsType == GoodsType.TYPE_SINGLE_INDIVIDUALITY_PLAN) {
                mTitles.remove(0);
                fragmentList.remove(0);
                //隐藏mTabLayout
                mTabLayout.setVisibility(View.GONE);
                window.setLayout(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight() * 3 / 5);

            } else if (goodsType == GoodsType.TYPE_SINGLE_WEIKE) {
                //显示三项
                CourseInfo courseInfo = bundle.getParcelable("courseInfo");
                mTitles.add(0, getString(R.string.synchronization_weike));
                BaseVipPayFragment weikePayFragmentNew = new BaseVipPayFragment();//微课
                weikePayFragmentNew.setType(GoodsType.TYPE_SINGLE_WEIKE);
                weikePayFragmentNew.setCourseInfo(courseInfo);
                fragmentList.add(0, weikePayFragmentNew);
            } else if (goodsType == GoodsType.TYPE_SINGLE_DIANDU) {
                mTitles.add(0, getString(R.string.textbook_read));
                BaseVipPayFragment dianduPayFragmentNew = new BaseVipPayFragment();//微课
                dianduPayFragmentNew.setType(GoodsType.TYPE_SINGLE_DIANDU);
                fragmentList.add(0, dianduPayFragmentNew);
            }
        }
        setPayTitle(0);

        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mTitles));
        mViewPager.setOffscreenPageLimit(mTitles.size());
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        final View customView = LayoutInflater.from(getActivity()).inflate(R.layout.vip_tab_item, null);
        final TextView textView = customView.findViewById(R.id.tab_vip);
        mTabLayout.getTabAt(mTitles.size() - 1).setCustomView(customView);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mTitles.size() - 1) {
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == mTitles.size() - 1) {
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_333));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        restoreGoodInfoAndPayWay(0);
        initListener();

    }

    private void initListener() {

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                restoreGoodInfoAndPayWay(position);
                currentPosition = position;
                fragmentList.get(position).setOnVipClickListener(BasePayDialogFragment.this);
                setPayTitle(position);
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
                umenStatistics(currentPosition);


            }
        });

        RxView.clicks(tvRightIntroduce).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), ProtocolActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_pay_popupwindow;
    }

    private void restoreGoodInfoAndPayWay(int position) {
        mPayWayName = PayConfig.ali_pay;
        BaseVipPayFragment baseVipPayFragmentNew = fragmentList.get(position);
        int type = baseVipPayFragmentNew.getmType();
        GoodInfoWrapper goodInfoWrapper = VipInfoHelper.getGoodInfoWrapper();
        if (type == GoodsType.TYPE_SVIP) {
            if (goodInfoWrapper.getSvip() != null && goodInfoWrapper.getSvip().size() > 0)
                mGoodInfo = VipInfoHelper.getGoodInfoWrapper().getSvip().get(0);
        } else if (type == GoodsType.TYPE_GENERAL_VIP) {
            if (goodInfoWrapper.getVip() != null && goodInfoWrapper.getVip().size() > 0)
                mGoodInfo = VipInfoHelper.getGoodInfoWrapper().getVip().get(0);
        } else if (type == GoodsType.TYPE_SINGLE_DIANDU) {
            if (goodInfoWrapper.getDiandu() != null && goodInfoWrapper.getDiandu().size() > 0) {
                mGoodInfo = VipInfoHelper.getGoodInfoWrapper().getDiandu().get(0);
            }
        } else if (type == GoodsType.TYPE_SINGLE_WEIKE) {
            if (goodInfoWrapper.getWvip() != null && goodInfoWrapper.getWvip().size() > 0) {
                mGoodInfo = VipInfoHelper.getGoodInfoWrapper().getWvip().get(0);
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
        mPayWayName = payWayName;

        mGoodInfo = goodInfo;

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

            fragmentList.get(0).setOnVipClickListener(BasePayDialogFragment.this);
            return fragmentList.get(position);
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
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (mGoodInfo.getType_id().equals("4")) {
            userInfo.setIsVip(2);
        } else if (mGoodInfo.getType_id().equals("1")) {
            userInfo.setIsVip(1);
        }

        Date date = new Date();
        userInfo.setVip_start_time(date.getTime() / 1000);
        if (mGoodInfo.getUse_time_limit() != null) {
            int use_time_Limit = Integer.parseInt(mGoodInfo.getUse_time_limit());
            long vip_end_time = date.getTime() + use_time_Limit * 30 * (Config.MS_IN_A_DAY);
            userInfo.setVip_end_time(vip_end_time / 1000);
        }
        UserInfoHelper.saveUserInfo(userInfo);

        RxBus.get().post(Constant.COMMUNITY_ACTIVITY_REFRESH, "form pay");
        VipDialogHelper.dismissVipDialog();
    }


    private void umenStatistics(int position) {
        BaseVipPayFragment baseVipPayFragmentNew = fragmentList.get(position);
        int type = baseVipPayFragmentNew.getmType();
        switch (type) {
            case GoodsType.TYPE_SVIP:
                MobclickAgent.onEvent(getActivity(), "score_vip", "提分辅导");
                break;
            case GoodsType.TYPE_GENERAL_VIP:
                MobclickAgent.onEvent(getActivity(), "general_vip", "普通会员");
                break;
            case GoodsType.TYPE_SINGLE_DIANDU:
            case GoodsType.TYPE_SINGLE_WEIKE:
                MobclickAgent.onEvent(getActivity(), "single_buy", "单次购买");
                break;
        }
    }

    private void setPayTitle(int position) {
        BaseVipPayFragment baseVipPayFragmentNew = fragmentList.get(position);
        int type = baseVipPayFragmentNew.getmType();

        if (type == GoodsType.TYPE_SVIP) {
            btnPay.setText("立即开通(仅限人教版用户)");
            tvRightIntroduce.setText(getString(R.string.study_introduce));
            tvRightIntroduce.setVisibility(View.VISIBLE);
        } else {
            btnPay.setText("立即开通");
            tvRightIntroduce.setVisibility(View.GONE);
        }

    }


}
