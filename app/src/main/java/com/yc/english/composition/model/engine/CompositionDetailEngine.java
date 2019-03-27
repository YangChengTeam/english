package com.yc.english.composition.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.composition.model.bean.CompositionDetailInfoWrapper;
import com.yc.english.main.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2019/3/26 14:26.
 */
public class CompositionDetailEngine extends BaseEngine {
    public CompositionDetailEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<CompositionDetailInfoWrapper>> getCompositionDetail(String zwid) {
        Map<String, String> params = new HashMap<>();
        params.put("zwid", zwid);
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ZW_DETAIL_URL, new TypeReference<ResultInfo<CompositionDetailInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }
}
