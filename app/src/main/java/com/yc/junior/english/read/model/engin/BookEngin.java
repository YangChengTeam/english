package com.yc.junior.english.read.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.base.dao.BookInfoDao;
import com.yc.junior.english.base.dao.GradeInfoDao;
import com.yc.junior.english.base.helper.BookHelper;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.read.common.ReadApp;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.BookInfoWarpper;
import com.yc.junior.english.read.model.domain.CourseVersionInfoList;
import com.yc.junior.english.read.model.domain.GradeInfo;
import com.yc.junior.english.read.model.domain.GradeInfoList;
import com.yc.junior.english.read.model.domain.URLConfig;
import com.yc.junior.english.read.model.domain.UnitInfoList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by admin on 2017/8/7.
 */

public class BookEngin extends BaseEngin {

    private BookInfoDao bookInfoDao = ReadApp.getDaoSession().getBookInfoDao();

    private GradeInfoDao gradeInfoDao = ReadApp.getDaoSession().getGradeInfoDao();

    public BookEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookInfoWarpper>> getBookInfoId(String bookId) {
        return BookHelper.getBookInfoId(context, bookId);
    }

    public Observable<ArrayList<BookInfo>> bookList(int currentPage, int pageCount, int type) {
        return BookHelper.bookList(currentPage, pageCount, type);
    }

    public Observable<ResultInfo<GradeInfoList>> gradeList(Context context) {

        return HttpCoreEngin.get(context).rxpost(URLConfig.GRADE_LIST_URL, new TypeReference<ResultInfo<GradeInfoList>>() {
                }.getType(), null,
                true, true,
                true);
    }

    public Observable<ResultInfo<CourseVersionInfoList>> getCVListByGradeId(Context context, String gradeId, String partType) {
        Map<String, String> params = new HashMap<>();
        params.put("grade", gradeId);
        params.put("part_type", partType);
        return HttpCoreEngin.get(context).rxpost(URLConfig.COURSE_VERSION_LIST_URL, new TypeReference<ResultInfo<CourseVersionInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }

    public Observable<ResultInfo<UnitInfoList>> bookUnitInfo(int currentPage, int pageCount, String bookId) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("book_id", bookId);
        return HttpCoreEngin.get(context).rxpost(URLConfig.WORD_UNIT_LIST_URL, new TypeReference<ResultInfo<UnitInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }


    public Observable<ArrayList<BookInfo>> addBook(BookInfo bookInfo) {
        return BookHelper.addBook(bookInfo);
    }

    public Observable<ArrayList<BookInfo>> deleteBook(BookInfo bookInfo) {
        return BookHelper.deleteBook(bookInfo);
    }

    public List<GradeInfo> getGradeListFromDB() {
        return (ArrayList<GradeInfo>) gradeInfoDao.queryBuilder().list();
    }

    public void saveGradeListToDB(List<GradeInfo> list) {
        if (list != null) {
            for (GradeInfo info : list) {
                gradeInfoDao.insertOrReplace(info);
            }
        }
    }

}
