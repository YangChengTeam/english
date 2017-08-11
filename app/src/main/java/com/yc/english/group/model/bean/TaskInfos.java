package com.yc.english.group.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 10:11.
 */

public class TaskInfos {
    private int code;
    private List<TaskAllInfoWrapper> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<TaskAllInfoWrapper> getList() {
        return list;
    }

    public void setList(List<TaskAllInfoWrapper> list) {
        this.list = list;
    }
}
