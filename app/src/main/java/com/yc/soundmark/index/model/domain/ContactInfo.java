package com.yc.soundmark.index.model.domain;

/**
 * Created by wanglin  on 2018/10/29 09:31.
 */
public class ContactInfo {
    /**
     * tel : null
     * qq : null
     */

    private String tel;
    private String qq;
    private String weixin;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
