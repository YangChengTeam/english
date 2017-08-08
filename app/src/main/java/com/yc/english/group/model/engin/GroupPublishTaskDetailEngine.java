package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.contract.GroupDoTaskDetailContract;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/8 16:32.
 */

public class GroupPublishTaskDetailEngine extends BaseEngin {
    public GroupPublishTaskDetailEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> getPublishTaskDetail(String id, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("user_id", user_id);
        return HttpCoreEngin.get(mContext).rxpost(NetConstan.detail_publish_task, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);


    }
}
