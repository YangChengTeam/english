package com.yc.english.read.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.dao.BookInfoDao;
import com.yc.english.base.helper.BookHelper;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.model.domain.BookInfoWarpper;
import com.yc.english.read.model.domain.URLConfig;
import com.yc.english.read.model.domain.WordInfoList;
import com.yc.english.read.model.domain.WordUnitInfo;
import com.yc.english.read.model.domain.WordUnitInfoList;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by admin on 2017/8/7.
 */

public class WordEngin extends BaseEngine {

    private BookInfoDao bookInfoDao = ReadApp.getDaoSession().getBookInfoDao();

    public WordEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<BookInfoWarpper>> getBookInfoId(String bookId) {
        return BookHelper.getBookInfoId(context, bookId);
    }

    public Observable<WordUnitInfo> wordUnitInfo(int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        return HttpCoreEngin.get(context).rxpost(URLConfig.WORD_UNIT_LIST_URL, new TypeReference<ResultInfo<WordUnitInfo>>() {
                }.getType(), params,
                true, true,
                true);
    }

    public Observable<ResultInfo<WordUnitInfoList>> getWordUnitByBookId(int currentPage, int pageCount, String bookId) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        params.put("book_id", bookId);
        return HttpCoreEngin.get(context).rxpost(URLConfig.WORD_UNIT_LIST_URL, new TypeReference<ResultInfo<WordUnitInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }


    public Observable<ResultInfo<WordInfoList>> getWordListByUnitId(int currentPage, int pageCount, String unitId) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        params.put("unit_id", unitId);
        return HttpCoreEngin.get(context).rxpost(URLConfig.WORD_LIST_URL, new TypeReference<ResultInfo<WordInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }


}
