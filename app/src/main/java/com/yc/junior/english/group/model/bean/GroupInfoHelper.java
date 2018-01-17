package com.yc.junior.english.group.model.bean;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.junior.english.group.constant.GroupConstant;
import com.yc.junior.english.group.rong.models.GroupInfo;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by wanglin  on 2017/8/15 14:00.
 */

public class GroupInfoHelper {

    private static ClassInfo classInfo;


    public static ClassInfo getClassInfo() {

        if (classInfo == null) {
            try {
                String str = SPUtils.getInstance().getString(GroupConstant.CLASS_INFO);

                classInfo = parseObject(str, ClassInfo.class);
            } catch (Exception e) {
                LogUtils.e("-->" + e.getMessage());
            }

        }
        return classInfo;
    }

    public static void setClassInfo(ClassInfo classInfo) {
        try {
            GroupInfoHelper.classInfo = classInfo;
            SPUtils.getInstance().put(GroupConstant.CLASS_INFO, JSONObject.toJSONString(classInfo));
        } catch (Exception e) {
            LogUtils.e("-->" + e.getMessage());
        }

    }

    private static GroupInfo groupInfo;

    public static GroupInfo getGroupInfo() {
        if (groupInfo == null) {
            try {
                String str = SPUtils.getInstance().getString(GroupConstant.GROUP_INFO);
                groupInfo = JSONObject.parseObject(str, GroupInfo.class);
            } catch (Exception e) {
                LogUtils.e("-->" + e.getMessage());
            }
        }
        return groupInfo;
    }

    public static void setGroupInfo(GroupInfo groupInfo) {
        try {
            GroupInfoHelper.groupInfo = groupInfo;
            SPUtils.getInstance().put(GroupConstant.GROUP_INFO, JSONObject.toJSONString(groupInfo));
        } catch (Exception e) {
            LogUtils.e("-->" + e.getMessage());
        }

    }
}
