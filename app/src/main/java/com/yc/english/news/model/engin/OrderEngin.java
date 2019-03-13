package com.yc.english.news.model.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.news.model.domain.OrderParams;
import com.yc.english.news.model.domain.URLConfig;
import com.yc.english.pay.alipay.OrderInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.blankj.utilcode.util.LogUtils;

/**
 *
 */

public class OrderEngin extends BaseEngine {

    private Context mContext;

    public OrderEngin(Context context) {
        super(context);
        mContext = context;
    }

    public Observable<ResultInfo<OrderInfo>> createOrder(OrderParams orderParams) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
        params.put("user_name", UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getName() : "");
        params.put("app_id", "1");
        params.put("title", "商品购买");
        params.put("price_total", orderParams.getPriceTotal());
        params.put("money", orderParams.getPriceTotal());
        params.put("pay_way_name", orderParams.getPayWayName());
        params.put("goods_list", JSON.toJSONString(orderParams.getGoodsList()));

        LogUtils.e("请求地址--->" + URLConfig.ORDER_URL);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ORDER_URL, new TypeReference<ResultInfo<OrderInfo>>() {
                }.getType(),
                params,
                true,
                true, true);
    }

    public Observable<ResultInfo> orderPay(String orderSn) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
        params.put("app_id", "1");
        params.put("order_sn", orderSn);

        LogUtils.e("订单支付请求地址--->" + URLConfig.ORDER_PAY_URL);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ORDER_PAY_URL, new TypeReference<ResultInfo>() {
                }.getType(),
                params,
                true,
                true, true);
    }


}
