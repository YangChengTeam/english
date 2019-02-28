package com.yc.soundmark.pay;

import com.yc.junior.english.pay.alipay.OrderInfo;

/**
 * Created by wanglin  on 2018/11/26 17:25.
 */
public interface PayListener {

    void onPayResult(OrderInfo orderInfo);
}
