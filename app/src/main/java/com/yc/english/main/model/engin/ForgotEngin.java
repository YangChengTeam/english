package com.yc.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.helper.EnginHelper;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.main.model.domain.UserInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class ForgotEngin extends BaseEngin {

    public ForgotEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> sendCode(String mobile){
        return EnginHelper.sendCode(mContext, URLConfig.FORGOT_SEND_CODE_URL, mobile);
    }

    public Observable<ResultInfo<UserInfo>> resetPassword(String mobile, String pwd, String code){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("pwd", pwd);
        params.put("code", code);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.FORGOT_URL, new TypeReference<ResultInfo<UserInfo>>(){}
                        .getType(),
                params,
                true,
                true, true);
    }

}
