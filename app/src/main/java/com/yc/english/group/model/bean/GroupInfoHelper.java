package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/8/15 14:00.
 */

public class GroupInfoHelper {

    private static String groupId;

    public static String getGroupId() {
        return groupId;
    }

    public static void setGroupId(String groupId) {
        GroupInfoHelper.groupId = groupId;
    }
}
