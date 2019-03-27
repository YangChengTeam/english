package com.yc.junior.english.composition.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.junior.english.main.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2019/3/22 18:21.
 */
public class EssayEngine extends BaseEngine {
    public EssayEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<CompositionInfoWrapper>> getCompositionIndexInfo() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ZW_INDEX_URL, new TypeReference<ResultInfo<CompositionInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }

    public Observable<ResultInfo<CompositionInfoWrapper>> getCompositionInfos(String attrid, int page, int pageSize) {
        Map<String, String> params = new HashMap<>();

        params.put("attrid", attrid);
        params.put("page", page + "");
        params.put("page_size", pageSize + "");

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ZW_LISTS_URL, new TypeReference<ResultInfo<CompositionInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }
}
