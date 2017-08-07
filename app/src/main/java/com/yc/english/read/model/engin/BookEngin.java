package com.yc.english.read.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.dao.BookInfoDao;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.BookUnitInfo;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;
import com.yc.english.read.model.domain.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by admin on 2017/8/7.
 */

public class BookEngin extends BaseEngin {

    private BookInfoDao bookInfoDao = ReadApp.getDaoSession().getBookInfoDao();

    public BookEngin(Context context) {
        super(context);
    }

    public Observable<List<BookInfo>> bookList(int currentPage, int pageCount) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, List<BookInfo>>() {

            @Override
            public List<BookInfo> call(String s) {
                return bookInfoDao.queryBuilder().list();
            }
        });
    }

    public Observable<List<GradeInfo>> gradeList(Context context) {

        return HttpCoreEngin.get(context).rxpost(URLConfig.GRADE_LIST_URL, new TypeReference<ResultInfo<GradeInfo>>() {
                }.getType(), null,
                true, true,
                true);
    }

    public Observable<List<CourseVersionInfo>> getCVListByGradeId(Context context, String gradeId) {
        Map<String, String> params = new HashMap<>();
        params.put("gradeId", gradeId);
        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_VERSION_LIST_URL, new TypeReference<ResultInfo<List<CourseVersionInfo>>>() {
                }.getType(), params,
                true, true,
                true);
    }

    public Observable<BookUnitInfo> bookUnitInfo(int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_VERSION_LIST_URL, new TypeReference<ResultInfo<BookUnitInfo>>() {
                }.getType(), params,
                true, true,
                true);
    }

}
