package com.yc.english.base.helper;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.URLConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/2.
 */

public class BookHelper {

    public static Observable<ResultInfo<List<BookInfo>>> bookList(Context context, int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");
        return HttpCoreEngin.get(context).rxpost(URLConfig.BOOK_LIST_URL, new TypeReference<ResultInfo<List<BookInfo>>>() {
                }.getType(), params,
                true, true,
                true);
    }

}
