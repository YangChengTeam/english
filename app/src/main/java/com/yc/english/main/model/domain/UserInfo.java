package com.yc.english.main.model.domain;

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
}
