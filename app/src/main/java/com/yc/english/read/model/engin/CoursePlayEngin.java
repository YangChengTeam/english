package com.yc.english.read.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.read.model.domain.EnglishCourseInfo;
import com.yc.english.read.model.domain.URLConfig;

import java.util.HashMap;
import java.util.List;
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

    public Observable<List<EnglishCourseInfo>> getCourseList(int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_LIST_URL, new TypeReference<ResultInfo<EnglishCourseInfo>>() {
                }.getType(), params,
                true, true,
                true);
    }

}
