package com.yc.junior.english.setting.model.bean;

/**
 * Created by admin on 2018/2/5.
 * 分享状态信息
 */

public class ShareStateInfo {

    private int status;//0：否；1：是

    private int days;//体验时间（单位：天）

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
