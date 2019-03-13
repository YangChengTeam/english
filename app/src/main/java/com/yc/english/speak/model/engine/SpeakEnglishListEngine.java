package com.yc.english.speak.model.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.speak.model.bean.SpeakAndReadInfoWrapper;
import com.yc.english.speak.model.bean.SpeakEnglishWarpper;
import com.yc.english.speak.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2017/10/13 09:08.
 */

public class SpeakEnglishListEngine extends BaseEngine {


    public SpeakEnglishListEngine(Context context) {
        super(context);
    }

    /**
     * 获取听说英语列表
     *
     * @param type_id 说英语 type_id = 1 听英语 type_id = 2
     * @return
     */
    public Observable<ResultInfo<SpeakAndReadInfoWrapper>> getReadAndSpeakList(String type_id, String page, String cnt) {
        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        if (!TextUtils.isEmpty(page)) {
            params.put("page", page);
        }
        if (!TextUtils.isEmpty(cnt)) {
            params.put("cnt", cnt);
        }

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.read_getReadList, new TypeReference<ResultInfo<SpeakAndReadInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取说英语详情
     *
     * @param context
     * @param id
     * @return
     */
    public Observable<ResultInfo<SpeakEnglishWarpper>> getListenReadDetail(Context context, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return HttpCoreEngin.get(context).rxpost(URLConfig.LISTEN_AND_READ_ENGLISH_URL, new TypeReference<ResultInfo<SpeakEnglishWarpper>>() {
        }.getType(), params, true, true, true);
    }

}
