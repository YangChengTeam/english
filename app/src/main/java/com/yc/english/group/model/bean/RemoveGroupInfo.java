package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/8/4 17:37.
 * 移除班群信息
 */

public class RemoveGroupInfo {

    private String master_id;
    private String class_id;

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }
}
