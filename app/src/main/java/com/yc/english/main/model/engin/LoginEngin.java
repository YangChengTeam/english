package com.yc.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.main.model.domain.UserInfo;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class LoginEngin extends BaseEngin {

    public LoginEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<UserInfo>> login(String username, String pwd){
        Map<String, String> params = new HashMap<>();
        params.put("user_name", username);
        params.put("pwd", pwd);
        return rxpost(new TypeReference<ResultInfo<UserInfo>>(){}.getType(), params, true, true, true);
    }

    @Override
    public String getUrl() {
        return URLConfig.LOGIN_URL;
    }
}
