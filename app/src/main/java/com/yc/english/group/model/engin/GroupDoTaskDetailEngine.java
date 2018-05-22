package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.TaskInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/8 16:09.
 */

public class GroupDoTaskDetailEngine extends BaseEngin {
    public GroupDoTaskDetailEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<TaskInfoWrapper>> doTask(String class_id, String user_id, String task_id, String desp, String imgs, String voices, String docs) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("user_id", user_id);
        params.put("task_id", task_id);
        if (!TextUtils.isEmpty(desp))
            params.put("desp", desp);
        if (!TextUtils.isEmpty(imgs))
            params.put("imgs", imgs);
        if (!TextUtils.isEmpty(voices))
            params.put("voices", voices);
        if (!TextUtils.isEmpty(docs))
            params.put("docs", docs);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.do_task, new TypeReference<ResultInfo<TaskInfoWrapper>>() {
        }.getType(), params, true, true, true);


    }
}
