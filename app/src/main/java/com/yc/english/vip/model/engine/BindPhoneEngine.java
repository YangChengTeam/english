package com.yc.english.vip.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2019/5/21 16:38.
 */
public class BindPhoneEngine extends BaseEngine {
    public BindPhoneEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> bindPhone(String phone, String code, String pwd) {

        Map<String, String> params = new HashMap<>();

        params.put("mobile", phone);
        params.put("user_id", UserInfoHelper.getUid());
        params.put("code", code);
        params.put("pwd", pwd);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.BIND_PHONE_URL, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }
}
