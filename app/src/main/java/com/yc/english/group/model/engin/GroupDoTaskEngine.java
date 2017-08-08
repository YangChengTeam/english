package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.contract.GroupDoTaskListContract;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/8 17:30.
 */

public class GroupDoTaskEngine extends BaseEngin {
    public GroupDoTaskEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> doTask(String class_id, String user_id, String task_id, String desp, String imgs, String voices, String docs) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("user_id", user_id);
        params.put("task_id", task_id);
        params.put("desp", desp);
        params.put("imgs", imgs);
        params.put("voices", voices);
        params.put("docs", docs);

        return HttpCoreEngin.get(mContext).rxpost(NetConstan.do_task, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);


    }
}
