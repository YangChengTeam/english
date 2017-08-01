package com.yc.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.TokenInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashEngin {
    private Context mContext;

    public SplashEngin(Context context) {
        mContext = context;
    }

    public Observable<ResultInfo<TokenInfo>> getTokenInfo(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        return HttpCoreEngin.get(mContext).rxpost(NetConstan.get_token, new TypeReference<ResultInfo<TokenInfo>>() {
        }.getType(), params, true, true, true);
    }
}
