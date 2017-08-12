package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/8 16:02.
 */

public class GroupDoTaskListEngine extends BaseEngin {
    public GroupDoTaskListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<TaskAllInfoWrapper>> getDoTaskList(String class_id, String user_id) {

        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("user_id", user_id);
        return HttpCoreEngin.get(mContext).rxpost(NetConstan.list_do_task, new TypeReference<ResultInfo<TaskAllInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }
}
