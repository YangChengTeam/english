package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/8 16:18.
 */

public class GroupUpdatePublishTaskEngine extends BaseEngin {
    public GroupUpdatePublishTaskEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> updatePublishTask(String id, String publisher, String desp, String imgs, String voices, String docs) {

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("publisher", publisher);
        if (!TextUtils.isEmpty(desp)) params.put("desp", desp);
        if (!TextUtils.isEmpty(imgs)) params.put("imgs", imgs);
        if (!TextUtils.isEmpty(voices)) params.put("voices", voices);
        if (!TextUtils.isEmpty(docs)) params.put("docs", docs);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.update_publish_task, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);


    }
}
