package com.yc.english.base.helper;

import com.yc.english.base.dao.BookInfoDao;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.model.domain.BookInfo;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class BookHelper {

    private static BookInfoDao bookInfoDao = ReadApp.getDaoSession().getBookInfoDao();

    public static Observable<List<BookInfo>> bookList(int currentPage, int pageCount,int type) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, List<BookInfo>>() {

            @Override
            public List<BookInfo> call(String s) {
                return bookInfoDao.queryBuilder().list();
            }
        });
    }

}
