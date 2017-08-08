package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/8 17:40.
 */

public class GroupUpdateDoTaskEngine extends BaseEngin {
    public GroupUpdateDoTaskEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> updateDoTask(String id, String user_id, String desp, String imgs, String voices, String docs) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("user_id", user_id);
        if (!TextUtils.isEmpty(desp))
            params.put("desp", desp);
        if (!TextUtils.isEmpty(imgs))
            params.put("imgs", imgs);
        if (!TextUtils.isEmpty(voices))
            params.put("voices", voices);
        if (!TextUtils.isEmpty(docs))
            params.put("docs", docs);
        return HttpCoreEngin.get(mContext).rxpost(NetConstan.update_do_task, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }
}
