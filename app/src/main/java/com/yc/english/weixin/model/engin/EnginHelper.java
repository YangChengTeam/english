package com.yc.english.weixin.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.news.bean.CourseInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class EnginHelper {
    public static Observable<ResultInfo<com.yc.english.weixin.model.domain.CourseInfoWrapper>> getWeixinList(Context context, String type_id, String page,
                                                                                                             String
                                                                                                                     page_size) {
        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        params.put("flag", "0");
        params.put("page", page);
        params.put("page_size", page_size);
        return HttpCoreEngin.get(context).rxpost(URLConfig.NEWS_URL, new TypeReference<ResultInfo<com.yc.english.weixin.model.domain.CourseInfoWrapper>>() {
                }
                        .getType(),
                params,
                true,
                true, true);
    }

    public static Observable<ResultInfo<CourseInfoWrapper>> getWeixinInfo(Context context, String news_id) {
        Map<String, String> params = new HashMap<>();
        params.put("news_id", news_id);
        return HttpCoreEngin.get(context).rxpost(URLConfig.NEWS_INFO_URL, new TypeReference<ResultInfo<CourseInfoWrapper>>() {
                }
                        .getType(),
                params,
                true,
                true, true);
    }
}
