package com.yc.english.main.model.engin;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.model.bean.TokenInfo;
import com.yc.english.main.hepler.EnginHelper;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashEngin extends BaseEngin {

    public SplashEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<TokenInfo>> getTokenInfo(String userId) {
        return EnginHelper.getTokenInfo(mContext, userId);
    }
}
