package com.yc.english.pay.alipay;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by zhangkai on 2017/4/19.
 */

public class IWXPay1Impl extends IPayImpl {

    private IWXAPI msgApi;

    public IWXPay1Impl(Activity context) {
        super(context);
        msgApi = WXAPIFactory.createWXAPI(context, null);
    }

    @Override
    public void pay(final OrderInfo orderInfo, IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtils.showLong("支付失败");
            return;
        }

        PayInfo payInfo = orderInfo.getPayInfo();
        if ("SUCCESS".equals(payInfo.getResult_code())) {
            uOrderInfo = orderInfo;
            uiPayCallback = iPayCallback;
            wxpay(payInfo);
        } else {
            ToastUtils.showLong("预下订单请求失败");
        }
    }

    private void wxpay(PayInfo payInfo) {
        PayReq request = new PayReq();
        request.appId = payInfo.getAppid();
        request.partnerId = payInfo.getMch_id();
        request.prepayId = payInfo.getPrepay_id();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = payInfo.getNonce_str();
        request.timeStamp = payInfo.getTimestamp();
        request.sign = payInfo.getSign();
        IPayImpl.appid = payInfo.getAppid();
        msgApi.registerApp(payInfo.getAppid());
        msgApi.sendReq(request);
    }

}
