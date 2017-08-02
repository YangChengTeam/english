package com.yc.english.main.hepler;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class UserInfoHelper {
    public static UserInfo getUserInfo() {
        String userinfoStr = SPUtils.getInstance().getString(Constant.USER_INFO);
        UserInfo userInfo = null;
        try {
            userInfo = JSON.parseObject(userinfoStr, UserInfo.class);
        } catch (Exception e) {
            LogUtils.e("error->" + e);
        }
        return userInfo;
    }

    public static void saveUserInfo(UserInfo userInfo) {
        try {
            String userinfoStr = toJsonStr(userInfo);
            SPUtils.getInstance().put(Constant.USER_INFO, userinfoStr);
        } catch (Exception e) {
            LogUtils.e("error->" + e);
        }
    }

    private static String toJsonStr(UserInfo userInfo) {
        return "{\"mobile\":\"" + userInfo.getMobile() + "\", \"nick_name\":\"" + userInfo.getNickname() + "\", \"name\":\""
                + userInfo.getName() + "\", \"face\":\""
                + userInfo.getAvatar() + "\", \"school\":\""
                + userInfo.getSchool() + "\", \"pwd\":\""
                + userInfo.getPwd() + "\", \"id\":\""
                + userInfo.getUid() + "\"}";
    }
}
