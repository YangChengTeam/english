package com.yc.junior.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.group.constant.NetConstant;
import com.yc.junior.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.junior.english.group.model.bean.StudentLookTaskInfo;

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


    public Observable<ResultInfo<StudentLookTaskInfo>> getIsReadTaskList(String class_id, String task_id) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("task_id", task_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.isRead_member_list, new TypeReference<ResultInfo<StudentLookTaskInfo>>() {
        }.getType(), params, true, true, true);

    }

    public Observable<ResultInfo<StudentFinishTaskInfo>> getIsFinishTaskList(String class_id, String task_id) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("task_id", task_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.isDone_member_list, new TypeReference<ResultInfo<StudentFinishTaskInfo>>() {
        }.getType(), params, true, true, true);

    }

}
