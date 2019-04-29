package com.yc.junior.english.main.model.domain;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * Created by zhangkai on 2017/8/1.
 */

public class UserInfo {
    @JSONField(name = "id")
    private String uid;
    private String mobile;
    private String name;
    private String pwd;
    @JSONField(name = "nick_name")
    private String nickname;
    private String school;
    private String token;
    private boolean isLogin;

    @JSONField(name = "face")
    private String avatar;
    @JSONField(name = "is_vip")
    private int isVip;// 0 未开通会员 1.普通会员  2 Svip 3.微课 4.点读
    private String vip;
    private long vip_end_time;
    private long vip_start_time;
    private long test_end_time;//通过分享获得的VIP时间

    private String pwd_text;//原始密码

    private int yb_vip;//是否是音标vip

    private int isSVip;//是否永久会员 1 是 0 否

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public long getVip_end_time() {
        return vip_end_time;
    }

    public void setVip_end_time(long vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public long getVip_start_time() {
        return vip_start_time;
    }

    public void setVip_start_time(long vip_start_time) {
        this.vip_start_time = vip_start_time;
    }

    public long getTest_end_time() {
        return test_end_time;
    }

    public void setTest_end_time(long test_end_time) {
        this.test_end_time = test_end_time;
    }

    public String getPwd_text() {
        return pwd_text;
    }

    public void setPwd_text(String pwd_text) {
        this.pwd_text = pwd_text;
    }

    public int getYb_vip() {
        return yb_vip;
    }

    public void setYb_vip(int yb_vip) {
        this.yb_vip = yb_vip;
    }

    public int getIsSVip() {
        return isSVip;
    }

    public void setIsSVip(int isSVip) {
        this.isSVip = isSVip;
    }
}
