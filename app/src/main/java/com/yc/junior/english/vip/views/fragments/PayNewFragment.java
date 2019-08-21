package com.yc.junior.english.vip.views.fragments;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.ScreenUtil;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.SharePopupWindow;
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
import com.yc.junior.english.vip.adapter.PayNewAdapter;
import com.yc.junior.english.vip.adapter.PayTimeLimitAdapter;
import com.yc.junior.english.vip.contract.VipBuyContract;
import com.yc.junior.english.vip.model.bean.VipGoodInfo;
import com.yc.junior.english.vip.presenter.VipBuyPresenter;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.junior.english.vip.utils.VipInfoHelper;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseDialogFragment;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.TimeUtils;



/**
 * Created by wanglin  on 2019/3/12 17:28.
 */
public class PayNewFragment extends BaseDialogFragment<VipBuyPresenter> implements VipBuyContract.View, SharePopupWindow.OnShareItemClickListener {


    @BindView(R.id.vip_recyclerView)
    RecyclerView vipRecyclerView;
    @BindView(R.id.timeLimit_recyclerView)
    RecyclerView timeLimitRecyclerView;
    @BindView(R.id.ll_ali_pay)
    LinearLayout llAliPay;
    @BindView(R.id.ll_wx_pay)
    LinearLayout llWxPay;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.rl_wx)
    RelativeLayout rlWx;
    @BindView(R.id.rl_ali)
    RelativeLayout rlAli;


    private IAliPay1Impl iAliPay;
    private IWXPay1Impl iwxPay;

    private PayNewAdapter payNewAdapter;
    private GoodInfo mGoodInfo;
    private String mPayWayName = PayConfig.ali_pay;
    private SharePopupWindow sharePopupWindow;
    private PayTimeLimitAdapter timeLimitAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_pay;
    }

    @Override
    public void init() {
//        tvOriginPrice.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mPresenter = new VipBuyPresenter(getActivity(), this);
        iAliPay = new IAliPay1Impl(getActivity());
        iwxPay = new IWXPay1Impl(getActivity());

        llAliPay.setSelected(true);
        vipRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        payNewAdapter = new PayNewAdapter(null);
        vipRecyclerView.setAdapter(payNewAdapter);

        //时间期限
        timeLimitRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        timeLimitAdapter = new PayTimeLimitAdapter(null);
        timeLimitRecyclerView.setAdapter(timeLimitAdapter);
        timeLimitRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 5));


        List<GoodInfo> goodInfos = VipInfoHelper.getGoodInfoList();
        if (goodInfos != null && goodInfos.size() > 0) {

            mGoodInfo = goodInfos.get(0);
            timeLimitAdapter.setNewData(goodInfos);
        }

        RelativeLayout.MarginLayoutParams layoutParams = (RelativeLayout.MarginLayoutParams) ivClose.getLayoutParams();

        int viewHeight = ScreenUtil.getHeight(getActivity()) * 24 / 25;
        layoutParams.topMargin = viewHeight * 77 / 1000 + 20;
        ivClose.setLayoutParams(layoutParams);

//        LogUtil.msg("height" + ScreenUtil.getHeight(getActivity()));
        int paddingTop;
        if (ScreenUtil.getHeight(getActivity()) > 1280) {
            paddingTop = viewHeight * 3 / 10;
        } else {
            paddingTop = viewHeight * 32 / 100;
        }

        llContainer.setPadding(llContainer.getPaddingLeft(), paddingTop, llContainer.getPaddingRight(), llContainer.getPaddingBottom());

        initListener();
    }

    @Override
    protected float getWidth() {
        return super.getWidth();
    }

    private void initListener() {
        RxView.clicks(llAliPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            resetPayway();
            llAliPay.setSelected(true);
            mPayWayName = PayConfig.ali_pay;
            pay();
        });
        RxView.clicks(llWxPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            resetPayway();
            llWxPay.setSelected(true);
            mPayWayName = PayConfig.wx_pay;
            pay();
        });
        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> dismiss());

        RxView.clicks(tvPay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            OrderParams orderParams = new OrderParams();
            if (mGoodInfo != null) {
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
        RxView.clicks(rlWx).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            mPayWayName = PayConfig.wx_pay;
            pay();
        });

        RxView.clicks(rlAli).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            mPayWayName = PayConfig.ali_pay;
            pay();
        });

        RxView.clicks(tvShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                sharePopupWindow = new SharePopupWindow(getActivity());
                sharePopupWindow.setBackColor(R.drawable.share_vip_bg);
                sharePopupWindow.setOnShareItemClickListener(PayNewFragment.this);
                sharePopupWindow.setFromPay(true);
                sharePopupWindow.show(rootView);
            }
        });

        timeLimitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                timeLimitAdapter.getViewByPostion(position).setSelected(true);
                mGoodInfo = timeLimitAdapter.getItem(position);
            }
        });
    }


    private void pay() {
        OrderParams orderParams = new OrderParams();
        if (mGoodInfo != null) {
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
    }

    private void resetPayway() {
        llAliPay.setSelected(false);
        llWxPay.setSelected(false);
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 24 / 25;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }


    @Override
    public void showOrderInfo(ResultInfo<OrderInfo> orderInfo, String money, String name) {
        if (orderInfo != null) {
            if (orderInfo.data != null) {
                orderInfo.data.setMoney(Float.parseFloat(money));
                orderInfo.data.setName(name);
                if (mPayWayName.equals(PayConfig.ali_pay)) {
                    aliPay(orderInfo.data);
                } else {
                    wxPay(orderInfo.data);
                }
            }
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
//                ToastUtils.showLong("支付失败");
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
        boolean isVip = false;
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (mGoodInfo.getType_id().equals("4")) {
            userInfo.setIsVip(2);
            isVip = true;
        } else if (mGoodInfo.getType_id().equals("1")) {
            userInfo.setIsVip(1);
            isVip = true;
        } else if (mGoodInfo.getType_id().equals("3")) {
            userInfo.setIsVip(4);
            isVip = true;
        } else if (mGoodInfo.getType_id().equals("5")) {
            userInfo.setIsVip(3);
            isVip = true;
        } else if (mGoodInfo.getType_id().equals("2")) {//单次微课支付
            isVip = false;

        }

        Date date = new Date();
        userInfo.setVip_start_time(date.getTime() / 1000);
        if (mGoodInfo.getUse_time_limit() != null) {
            if (Integer.parseInt(mGoodInfo.getUse_time_limit()) > 12) {
                userInfo.setIsSVip(1);
            }
            int use_time_Limit = Integer.parseInt(mGoodInfo.getUse_time_limit());
            long vip_end_time = date.getTime() + use_time_Limit * 30 * (Config.MS_IN_A_DAY);
            userInfo.setVip_end_time(vip_end_time / 1000);
        }
        UserInfoHelper.saveUserInfo(userInfo);
        if (isVip) {
            RxBus.get().post(Constant.COMMUNITY_ACTIVITY_REFRESH, "form pay");
        } else {
            RxBus.get().post(Constant.PAY_SIGNAL_SUCCESS, "signal success");
        }
        VipDialogHelper.dismissVipDialog();

        if (UserInfoHelper.getUserInfo() != null && TextUtils.isEmpty(UserInfoHelper.getUserInfo().getMobile())) {
            BindPhoneFragment bindPhoneFragment = new BindPhoneFragment();
            bindPhoneFragment.show(getActivity().getSupportFragmentManager(), "");
        }
    }

    @Override
    public void shareAllow(Integer data) {
        if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
            sharePopupWindow.dismiss();
        }
        VipDialogHelper.dismissVipDialog();

        if (data > 0) {
            LogUtils.i("shareAllow --->");
            UserInfo userInfo = UserInfoHelper.getUserInfo();
            userInfo.setIsVip(1);

            userInfo.setVip_start_time(TimeUtils.getNowMills() / 1000);
//MS_IN_A_DAY
            long vip_end_time = TimeUtils.getNowMills() + data * (Config.MS_IN_A_DAY);
            long test_end_time = TimeUtils.getNowMills() + data * (Config.MS_IN_A_DAY);
            userInfo.setVip_end_time(vip_end_time / 1000);
            userInfo.setTest_end_time(test_end_time / 1000);

            UserInfoHelper.saveUserInfo(userInfo);
        }

        RxBus.get().post(Constant.COMMUNITY_ACTIVITY_REFRESH, "form pay");
    }

    @Override
    public void showVipGoodInfos(List<VipGoodInfo> vipGoodInfos) {
        payNewAdapter.setNewData(vipGoodInfos);
    }

    @Override
    public void showGoodInfoList(List<GoodInfo> goodInfos) {

        if (goodInfos != null && goodInfos.size() > 0) {
            mGoodInfo = goodInfos.get(0);
            timeLimitAdapter.setNewData(goodInfos);
        }
    }

    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }

    @Override
    public void showNoNet() {
        if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
            sharePopupWindow.dismiss();
        }
        VipDialogHelper.dismissVipDialog();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onShareSuccess() {

        mPresenter.getShareVipAllow(UserInfoHelper.getUid());

    }


}
