package com.yc.junior.english.base.helper;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.base.model.ShareInfo;
import com.yc.junior.english.main.model.domain.URLConfig;
import com.yc.junior.english.main.model.domain.UserInfoWrapper;
import com.yc.junior.english.read.common.AppidsInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class EnginHelper {


    public static Observable<ResultInfo<UserInfoWrapper>> login(Context context, String username, String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", username);
        params.put("pwd", pwd);
        return HttpCoreEngin.get(context).rxpost(URLConfig.LOGIN_URL, new TypeReference<ResultInfo<UserInfoWrapper>>() {
                }
                        .getType(), params,
                true, true,
                true);
    }

    public static Observable<ResultInfo<String>> sendCode(Context context, String url, String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        return HttpCoreEngin.get(context).rxpost(url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    public static Observable<ResultInfo<UserInfoWrapper>> getUserInfo(Context context, String userid) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userid);
        return HttpCoreEngin.get(context).rxpost(URLConfig.GET_USER_INFO_URL, new TypeReference<ResultInfo<UserInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }

    public static Observable<ResultInfo<ShareInfo>> getShareInfo(Context context) {
        return HttpCoreEngin.get(context).rxpost(URLConfig.SHARE_INFO_URL, new TypeReference<ResultInfo<ShareInfo>>() {
        }.getType(), null, true, true, true);
    }

    public static Observable<ResultInfo<AppidsInfo>> getAppids(Context context) {
        return HttpCoreEngin.get(context).rxpost(URLConfig.APPIDS_URL, new TypeReference<ResultInfo<AppidsInfo>>() {
        }.getType(), null, true, true, true);
    }
}
