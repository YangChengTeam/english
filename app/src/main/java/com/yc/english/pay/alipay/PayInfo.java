package com.yc.english.pay.alipay;

import java.io.Serializable;

/**
 * Created by zhangkai on 2017/4/14.
 */

public class PayInfo implements Serializable {

    private static final long serialVersionUID = -7260210533610464481L;

    //基本信息
    private String partnerid; //商户id
    private String appid;    //应用id

    //支付方式相关信息
    private String payway_account_name;  //支付方式帐号名

    //原生支付宝支付信息
    private String email;  //邮箱
    private String privatekey; //密钥

    private String notify_url;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPayway_account_name() {
        return payway_account_name;
    }

    public void setPayway_account_name(String payway_account_name) {
        this.payway_account_name = payway_account_name;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
