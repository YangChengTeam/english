package com.yc.soundmark.index.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.soundmark.base.constant.UrlConfig;
import com.yc.soundmark.index.model.domain.IndexInfoWrapper;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2018/10/29 08:50.
 */
public class IndexEngine extends BaseEngine {
    public IndexEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<IndexInfoWrapper>> getIndexInfo() {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.contact_info, new TypeReference<ResultInfo<IndexInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }


}
