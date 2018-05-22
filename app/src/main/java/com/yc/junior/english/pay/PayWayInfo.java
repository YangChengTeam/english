package com.yc.junior.english.pay;

/**
 * Created by wanglin  on 2017/11/7 11:23.
 */

public class PayWayInfo {


    /**
     * pay_way_name : alipay
     * pay_way_title : 支付宝
     */

    private String pay_way_name;
    private String pay_way_title;

    public String getPay_way_name() {
        return pay_way_name;
    }

    public void setPay_way_name(String pay_way_name) {
        this.pay_way_name = pay_way_name;
    }

    public String getPay_way_title() {
        return pay_way_title;
    }

    public void setPay_way_title(String pay_way_title) {
        this.pay_way_title = pay_way_title;
    }

    public PayWayInfo(String pay_way_name) {
        this.pay_way_name = pay_way_name;
    }

    public PayWayInfo() {
    }
}
