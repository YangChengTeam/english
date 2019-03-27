package com.yc.english.composition.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.english.main.model.domain.URLConfig;


import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2019/3/26 17:15.
 */
public class CompositionSearchEngine extends BaseEngine {
    public CompositionSearchEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<CompositionInfoWrapper>> searchCompositionInfos(String title, String grade_id, String topic, String ticai, String type, int page, int pagesize) {
        Map<String, String> params = new HashMap<>();
        params.put("titlezw", title);
        params.put("grade_id", grade_id);
        params.put("topic", topic);
        params.put("ticai", ticai);
        params.put("type", type);
        params.put("page", page + "");
        params.put("page_size", pagesize + "");

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ZW_SEARCH_URL, new TypeReference<ResultInfo<CompositionInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }
}
