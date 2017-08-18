package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.TokenInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/7/31 15:06.
 */

public class TokenEngine extends BaseEngin<ResultInfo<TokenInfo>> {
    public TokenEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstan.get_token;
    }

    public Observable<ResultInfo<TokenInfo>> getTokenInfo(String userId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        return rxpost(new TypeReference<ResultInfo<TokenInfo>>() {
        }.getType(), params, true, true, true);
    }
}
