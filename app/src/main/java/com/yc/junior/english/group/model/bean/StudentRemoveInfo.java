package com.yc.junior.english.group.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/4 11:03.
 * 移除学生的信息
 */

public class StudentRemoveInfo {

    private String class_id;
    private List<String> members;

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
