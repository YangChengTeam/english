package com.yc.junior.english.main.model.engin;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.group.model.bean.TokenInfo;
import com.yc.junior.english.base.helper.EnginHelper;

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
