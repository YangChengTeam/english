package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

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
 * Created by wanglin  on 2017/8/8 15:53.
 */

public class GroupPublishTaskListEngine extends BaseEngin {
    public GroupPublishTaskListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<TaskAllInfoWrapper>> getPublishTaskList(String publisher, String class_id) {

        Map<String, String> params = new HashMap<>();
        params.put("publisher", publisher);
        if (!TextUtils.isEmpty(class_id))
            params.put("class_id", class_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstan.list_publish_task, new TypeReference<ResultInfo<TaskAllInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }
}
