package com.yc.junior.english.composition.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.composition.model.bean.FodderInfoWrapper;
import com.yc.junior.english.main.model.domain.URLConfig;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2019/3/25 09:04.
 */
public class FodderEngine extends BaseEngine {
    public FodderEngine(Context context) {
        super(context);
    }



    public Observable<ResultInfo<FodderInfoWrapper>> getFodderIndexInfo() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ZW_SUCAINAV_URL, new TypeReference<ResultInfo<FodderInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }
}
