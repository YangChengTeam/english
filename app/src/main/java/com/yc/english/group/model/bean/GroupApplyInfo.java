package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/8/4 16:15.
 * 申请加入群的用户信息和群信息
 */

public class GroupApplyInfo {
    private String class_id;
    private String user_id;
    private String vali_type;
    private String class_name;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVali_type() {
        return vali_type;
    }

    public void setVali_type(String vali_type) {
        this.vali_type = vali_type;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
