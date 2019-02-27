package com.yc.soundmark.category.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.soundmark.base.constant.UrlConfig;
import com.yc.soundmark.category.model.domain.CourseInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class WeiKeDetailEngine extends BaseEngine {


    public WeiKeDetailEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<CourseInfo>> getWeikeCategoryInfo(String id, int page, int page_size) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");

        params.put("page_size", page_size + "");
        params.put("id", id);


        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.weike_info_url, new TypeReference<ResultInfo<CourseInfo>>() {
                }.getType(),
                params,
                true,
                true, true);
    }


}
