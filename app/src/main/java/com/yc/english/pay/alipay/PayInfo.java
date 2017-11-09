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

    //原生微信渠道信息
    private String mch_id;
    private String nonce_str;
    private String prepay_id;
    private String result_code;
    private String return_code;
    private String return_msg;
    private String sign;
    private String trade_type;

    private String timestamp;

    private String md5signstr;

    private String rsmd5;

    private String starttime;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMd5signstr() {
        return md5signstr;
    }

    public void setMd5signstr(String md5signstr) {
        this.md5signstr = md5signstr;
    }

    public String getRsmd5() {
        return rsmd5;
    }

    public void setRsmd5(String rsmd5) {
        this.rsmd5 = rsmd5;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
}
