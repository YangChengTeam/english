package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.contract.GroupDoTaskDetailContract;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.model.bean.StudentTaskInfo;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.bean.TaskPublishDetailInfo;

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


    public Observable<ResultInfo<StudentTaskInfo>> getIsReadTaskList(String class_id, String task_id) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("task_id", task_id);

        return HttpCoreEngin.get(mContext).rxpost(NetConstan.isRead_member_list, new TypeReference<ResultInfo<StudentTaskInfo>>() {
        }.getType(), params, true, true, true);

    }

}
