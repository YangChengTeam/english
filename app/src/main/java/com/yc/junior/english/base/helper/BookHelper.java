package com.yc.junior.english.base.helper;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.dao.BookInfoDao;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.BookInfoWarpper;
import com.yc.english.read.model.domain.URLConfig;
import com.yc.junior.english.read.common.ReadApp;
import com.yc.junior.english.read.model.domain.BookInfo;
import com.yc.junior.english.read.model.domain.BookInfoWarpper;
import com.yc.junior.english.read.model.domain.URLConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class BookHelper {

    private static BookInfoDao bookInfoDao = ReadApp.getDaoSession().getBookInfoDao();

    public static Observable<ResultInfo<BookInfoWarpper>> getBookInfoId(Context context, String bookId) {
        Map<String, String> params = new HashMap<>();
        params.put("book_id", bookId);
        return HttpCoreEngin.get(context).rxpost(URLConfig.BOOK_INFO_URL, new TypeReference<ResultInfo<BookInfoWarpper>>() {
                }.getType(), params,
                true, true,
                true);
    }

    public static Observable<ArrayList<BookInfo>> bookList(int currentPage, int pageCount, int type) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, ArrayList<BookInfo>>() {

            @Override
            public ArrayList<BookInfo> call(String s) {
                return (ArrayList<BookInfo>) bookInfoDao.queryBuilder().list();
            }
        });

    }

    public static Observable<ArrayList<BookInfo>> addBook(final BookInfo aBookInfo) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, ArrayList<BookInfo>>() {
            @Override
            public ArrayList<BookInfo> call(String s) {
                bookInfoDao.insert(aBookInfo);

                return (ArrayList<BookInfo>) bookInfoDao.queryBuilder().list();
            }
        });
    }

    public static Observable<ArrayList<BookInfo>> deleteBook(final BookInfo dBookInfo) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, ArrayList<BookInfo>>() {
            @Override
            public ArrayList<BookInfo> call(String s) {
                bookInfoDao.delete(dBookInfo);
                return (ArrayList<BookInfo>) bookInfoDao.queryBuilder().list();
            }
        });
    }

}
