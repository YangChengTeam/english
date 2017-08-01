package com.yc.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.main.model.domain.UserInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class RegisterEngin {
    private Context mContext;
    public RegisterEngin(Context context) {
        mContext = context;
    }

    public Observable<ResultInfo<String>> sendCode(String mobile){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.SEND_CODE_URL, new TypeReference<ResultInfo<String>>(){}
                        .getType(), params,
               true, true, true);
    }


    public Observable<ResultInfo<UserInfo>> register(String mobile, String pwd, String code){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("pwd", pwd);
        params.put("code", code);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.REGISTER_URL, new TypeReference<ResultInfo<String>>(){}
                        .getType(),
                params,
                true,
                true, true);
    }


}
