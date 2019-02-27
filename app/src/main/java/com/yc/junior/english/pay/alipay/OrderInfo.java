package com.yc.junior.english.pay.alipay;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by zhangkai on 2017/3/17.
 */

public class OrderInfo implements Serializable {

    private static final long serialVersionUID = -7060210533610464481L;

    private float money; //价格 单位元

    private String name; //会员类型名 也即商品名


    @JSONField(name = "charge_order_sn")
    private String order_sn; //订单号

//    @JSONField(name = "order_sn")
//    private String order_sn; //订单号

    private String message;


    private String type;//支付类型

    @JSONField(name = "params")
    private PayInfo payInfo;

    private String goodId;

    public OrderInfo() {
    }


    public OrderInfo(float money, String name, String order_sn) {
        this.money = money;
        this.name = name;
        this.order_sn = order_sn;
    }


    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }
}
