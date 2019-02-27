package com.yc.soundmark.base.fragment;

import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.kk.utils.ToastUtil;
import com.yc.english.R;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.pay.PayWayInfoHelper;
import com.yc.english.pay.alipay.IAliPay1Impl;
import com.yc.english.pay.alipay.IPayCallback;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.soundmark.base.adapter.BasePayAdapter;
import com.yc.soundmark.base.constant.BusAction;
import com.yc.soundmark.base.contract.BasePayContract;
import com.yc.soundmark.base.model.domain.GoodInfo;
import com.yc.soundmark.base.presenter.BasePayPresenter;
import com.yc.soundmark.base.utils.VipInfoHelper;
import com.yc.soundmark.category.utils.ItemDecorationHelper;
import com.yc.soundmark.pay.alipay.IWXPay1Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseDialogFragment;

/**
 * Created by wanglin  on 2018/10/29 17:18.
 */
public class BasePayFragment extends BaseDialogFragment<BasePayPresenter> implements BasePayContract.View {

    private RecyclerView paywayRecyclerView;

    private ImageView ivPaywayAli;

    private LinearLayout llAli;

    private ImageView ivPaywayWx;

    private LinearLayout llWx;

    private ImageView ivPayBtn;

    private ImageView ivClose;

    private IAliPay1Impl aliPay;
    private IWXPay1Impl wxPay;
    private GoodInfo currentGoodInfo;

    private String payway;
    private int currentPos = 0;
    private BasePhoneFragment basePhoneFragment;
    private ImageView preImageView;
    private BasePayAdapter basePayAdapter;

    @Override
    protected float getWidth() {
        return 0.9f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 9 / 10;
    }

    @Override
    protected void initView() {
        paywayRecyclerView = (RecyclerView) getView(R.id.payway_recyclerView);
        ivPaywayAli = (ImageView) getView(R.id.iv_payway_ali);
        llAli = (LinearLayout) getView(R.id.ll_ali);
        ivPaywayWx = (ImageView) getView(R.id.iv_payway_wx);
        llWx = (LinearLayout) getView(R.id.ll_wx);
        ivPayBtn = (ImageView) getView(R.id.iv_pay_btn);
        ivClose = (ImageView) getView(R.id.iv_close);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_pay;
    }

    @Override
    public void init() {

        aliPay = new IAliPay1Impl(getActivity());
        wxPay = new IWXPay1Impl(getActivity());
        mPresenter = new BasePayPresenter(getActivity(), this);


        List<GoodInfo> vipInfoList = VipInfoHelper.getVipInfoList();
        currentGoodInfo = getGoodInfo(vipInfoList);
        mPresenter.isBindPhone();


        paywayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        basePayAdapter = new BasePayAdapter(vipInfoList);
        paywayRecyclerView.setAdapter(basePayAdapter);
        paywayRecyclerView.setHasFixedSize(true);
        paywayRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 15));


//        basePayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                clickItem(position);
//
//            }
//        });
//
//        basePayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                clickItem(position);
//                return false;
//            }
//        });

        ivPaywayAli.setSelected(true);
        initListener();

    }


    private void clickItem(int position) {
        ImageView mIvSelect = basePayAdapter.getIv(position);
        boolean isBuy = (boolean) mIvSelect.getTag();
        if (isBuy) {
            return;
        }

        if (preImageView == null)
            preImageView = basePayAdapter.getIv(getPosition());

        if (preImageView != mIvSelect && !((boolean) preImageView.getTag())) {
            preImageView.setImageResource(R.mipmap.vip_info_unselect);
        }
        mIvSelect.setImageResource(R.mipmap.vip_info_selected);
        preImageView = mIvSelect;
        currentGoodInfo = basePayAdapter.getItem(position);
    }

    private void initListener() {
        RxView.clicks(llAli).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPaywayState();
                if (!ivPaywayAli.isSelected()) {
                    ivPaywayAli.setSelected(true);
                    currentPos = 0;
                }
            }
        });

        RxView.clicks(llWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPaywayState();
                if (!ivPaywayWx.isSelected()) {
                    ivPaywayWx.setSelected(true);
                    currentPos = 1;
                }
            }
        });
        RxView.clicks(ivPayBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // todo  支付
                if (UserInfoHelper.isSuperVip()) {//已购买所有项目
                    createRewardDialog();
                    return;
                }

                payway = getPaywayName(currentPos);
                List<OrderGood> list = new ArrayList<>();
                OrderGood orderGood = new OrderGood();
                orderGood.setGood_id(currentGoodInfo.getId());
                orderGood.setNum(1);
                list.add(orderGood);

                if (currentGoodInfo != null) {
                    mPresenter.createOrder(payway, currentGoodInfo.getReal_price(), currentGoodInfo.getId(), currentGoodInfo.getTitle(), list);

                } else {
                    ToastUtil.toast2(getActivity(), "支付错误");
                }

            }
        });

        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

    }

    private void resetPaywayState() {
        ivPaywayAli.setSelected(false);
        ivPaywayWx.setSelected(false);
    }


    private void aliPay(OrderInfo orderInfo) {


        aliPay.pay(orderInfo, iPayCallback);
    }

    private void wxPay(OrderInfo orderInfo) {
        wxPay.pay(orderInfo, iPayCallback);
    }

    private IPayCallback iPayCallback = new IPayCallback() {
        @Override
        public void onSuccess(OrderInfo orderInfo) {
            //保存vip
//            UserInfoHelper.saveVip(orderInfo.getGoodId());

            UserInfo userInfo = UserInfoHelper.getUserInfo();
            userInfo.setYb_vip(1);
            UserInfoHelper.saveUserInfo(userInfo);


            //支付弹窗消失
            dismissAllowingStateLoss();

//            if (!isBind) {
//                if (basePhoneFragment == null)
//                    basePhoneFragment = new BasePhoneFragment();
//                if (!basePhoneFragment.isVisible()) {
//                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                    ft.add(basePhoneFragment, null);
//                    ft.commitAllowingStateLoss();
//                }
//            }
            RxBus.get().post(BusAction.PAY_SUCCESS, "pay_success");

        }

        @Override
        public void onFailure(OrderInfo orderInfo) {

        }
    };


    private String getPaywayName(int position) {
        List<PayWayInfo> payWayInfoList = PayWayInfoHelper.getPayWayInfoList();
        if (payWayInfoList != null && payWayInfoList.size() > 0 && position < payWayInfoList.size()) {
            return payWayInfoList.get(position).getPay_way_name();
        }
        return "";
    }

    @Override
    public void showOrderInfo(OrderInfo orderInfo) {

        if (TextUtils.equals(payway, "alipay"))
            aliPay(orderInfo);
        else {
            wxPay(orderInfo);
        }
    }

    private boolean isBind;

    @Override
    public void showBindSuccess() {
        isBind = true;
    }

    @Override
    public void showVipInfoList(List<GoodInfo> goodInfoList) {
        currentGoodInfo = getGoodInfo(goodInfoList);
        basePayAdapter.setNewData(goodInfoList);
    }

    @Override
    public void showLoadingDialog(String mess) {
        ((BaseActivity) getActivity()).showLoadingDialog(mess);
    }

    @Override
    public void dismissDialog() {
        ((BaseActivity) getActivity()).dismissDialog();
    }


    private void createRewardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("你已经购买了所有项目");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }

    private GoodInfo getGoodInfo(List<GoodInfo> goodInfoList) {
        GoodInfo goodInfo = null;
        if (goodInfoList != null && goodInfoList.size() > 0) {
            goodInfo = goodInfoList.get(getPosition());
            if (UserInfoHelper.isSuperVip()) {
                goodInfo = null;
            }

        }
        return goodInfo;
    }


    private int getPosition() {
        int pos = 0;
//        if (UserInfoHelper.isPhonogramVip()) {
//            pos = 1;
//        }
//        if (UserInfoHelper.isPhonicsVip() && UserInfoHelper.isPhonogramVip() || UserInfoHelper.isPhonogramOrPhonicsVip()) {
//            pos = 3;
//        }

        return pos;
    }

}
