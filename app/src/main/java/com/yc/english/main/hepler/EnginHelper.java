package com.yc.english.main.hepler;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.TokenInfo;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.main.model.domain.UserInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class EnginHelper {
    public static Observable<ResultInfo<TokenInfo>> getTokenInfo(Context context, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        return HttpCoreEngin.get(context).rxpost(NetConstan.get_token, new TypeReference<ResultInfo<TokenInfo>>() {
        }.getType(), params, true, true, true);
    }

    public static Observable<ResultInfo<UserInfo>> login(Context context, String username, String pwd){
        Map<String, String> params = new HashMap<>();
        params.put("user_name", username);
        params.put("pwd", pwd);
        return HttpCoreEngin.get(context).rxpost(URLConfig.LOGIN_URL, new TypeReference<ResultInfo<UserInfo>>(){}
                        .getType(), params,
                true, true,
                true);
    }

}
