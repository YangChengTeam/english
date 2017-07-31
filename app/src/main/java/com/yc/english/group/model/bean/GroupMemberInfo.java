package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/7/26 15:03.
 * 群成员信息
 */

public class GroupMemberInfo {
    private String name;
    private String imgUrl;
    private String groupName;//哪个学校
    private boolean isGroupOwner;//是否是群主


    public GroupMemberInfo() {
    }

    public GroupMemberInfo(String name, String imgUrl, boolean isGroupOwner) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.isGroupOwner = isGroupOwner;
    }

    public GroupMemberInfo(String name, String imgUrl, String groupName) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isGroupOwner() {
        return isGroupOwner;
    }

    public void setGroupOwner(boolean groupOwner) {
        isGroupOwner = groupOwner;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
