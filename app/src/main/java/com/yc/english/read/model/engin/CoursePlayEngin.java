package com.yc.english.read.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.read.model.domain.EnglishCourseInfoList;
import com.yc.english.read.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by admin on 2017/8/7.
 */

public class CoursePlayEngin extends BaseEngin {

    public CoursePlayEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<EnglishCourseInfoList>> getCourseListByUnitId(int currentPage, int pageCount, String unitId) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        params.put("unitId", unitId);
        return HttpCoreEngin.get(context).rxpost(URLConfig.SENTENCE_LIST_URL, new TypeReference<ResultInfo<EnglishCourseInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }

}
