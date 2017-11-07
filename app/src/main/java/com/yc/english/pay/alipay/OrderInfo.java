package com.yc.english.pay.alipay;

import android.content.Context;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by zhangkai on 2017/3/17.
 */

public class OrderInfo implements Serializable {
    private int good_id; //会员类型 也即商品id

    private float money; //价格 单位元

    private String name; //会员类型名 也即商品名

    private String order_sn; //订单号

    private String message;  //订单回调消息

    private String payway; //订单支付方式 原生支付宝 现代支付

    private String addtime; //订单创建时间

    private Context context; //支付上下文 用于异步回调

    private String type;//支付类型

    private int good_num;//商品数量
@JSONField(name = "params")
    private PayInfo payInfo;


    public OrderInfo(){}

    public OrderInfo(int good_id, float money, String name, String order_sn, String payway, String addtime, String type, int good_num) {
        this.good_id = good_id;
        this.money = money;
        this.name = name;
        this.order_sn = order_sn;
        this.payway = payway;
        this.addtime = addtime;
        this.type = type;
        this.good_num = good_num;

    }



    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getGood_num() {
        return good_num;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }
}
