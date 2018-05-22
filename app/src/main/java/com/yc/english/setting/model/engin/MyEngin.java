package com.yc.english.setting.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.EmptyUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.setting.model.bean.MyOrderInfo;
import com.yc.english.setting.model.bean.ScoreInfo;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class MyEngin extends BaseEngin {
    public MyEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> postMessage(String message) {
        Map<String, String> params = new HashMap<>();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (!EmptyUtils.isEmpty(userInfo)) {
            params.put("user_id", userInfo.getUid());
        }
        params.put("content", message);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.POST_MESSAGE_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    public Observable<ResultInfo<UserInfo>> updateMessage(String avatar, String nick_name, String school) {
        Map<String, String> params = new HashMap<>();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (!EmptyUtils.isEmpty(userInfo)) {
            params.put("user_id", userInfo.getUid());
        }
        params.put("face", avatar);
        params.put("nick_name", nick_name);
        params.put("school", school);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.UPD_MESSAGE_URL, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), params, false, true, true);
    }

    public Observable<ResultInfo<String>> updatePassword(String pwd, String newPwd) {
        Map<String, String> params = new HashMap<>();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (!EmptyUtils.isEmpty(userInfo)) {
            params.put("user_id", userInfo.getUid());
        }
        params.put("pwd", pwd);
        params.put("new_pwd", newPwd);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.UPD_PWD_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    public Observable<ResultInfo<List<MyOrderInfo>>> getMyOrderInfo(int page, int limit) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
        params.put("page", page + "");
        params.put("limit", limit + "");
        params.put("type", "1");
        params.put("app_id", "1");

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.MY_ORDER_URL, new TypeReference<ResultInfo<List<MyOrderInfo>>>() {
        }.getType(), params, true, true, true);
    }

    public Observable<ResultInfo<ScoreInfo>> getAbilityScore(String uid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.Ability_score_url, new TypeReference<ResultInfo<ScoreInfo>>() {
        }.getType(), params, true, true, true);
    }

}
