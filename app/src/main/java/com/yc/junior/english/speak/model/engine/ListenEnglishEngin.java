package com.yc.junior.english.speak.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.speak.model.bean.ListenEnglishWarpper;
import com.yc.junior.english.speak.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by admin on 2017/10/16.
 */

public class ListenEnglishEngin extends BaseEngin {

    public ListenEnglishEngin(Context context) {
        super(context);
    }

    /**
     * 获取听英语详情
     *
     * @param context
     * @param id
     * @return
     */
    public Observable<ResultInfo<ListenEnglishWarpper>> getListenReadDetail(Context context, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return HttpCoreEngin.get(context).rxpost(URLConfig.LISTEN_AND_READ_ENGLISH_URL, new TypeReference<ResultInfo<ListenEnglishWarpper>>() {
        }.getType(), params, true, true, true);
    }

}
