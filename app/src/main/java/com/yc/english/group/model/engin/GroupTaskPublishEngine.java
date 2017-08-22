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
 * Created by wanglin  on 2017/8/7 10:42.
 */

public class GroupTaskPublishEngine extends BaseEngin {
    public GroupTaskPublishEngine(Context context) {
        super(context);
    }

    /**
     * 发布作业
     *
     * @param class_ids 班群ID,多个，逗号隔开
     * @param publisher 群主/发布人ID
     * @param desp
     * @return
     */
    public Observable<ResultInfo<TaskInfoWrapper>> publishTask(String class_ids, String publisher, String desp, String imgesUrl, String voiceUrl, String docsUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("class_ids", class_ids);
        params.put("publisher", publisher);
        if (!TextUtils.isEmpty(desp))
            params.put("desp", desp);
        if (!TextUtils.isEmpty(imgesUrl)) {
            params.put("imgs", imgesUrl);
        }
        if (!TextUtils.isEmpty(voiceUrl)) {
            params.put("voices", voiceUrl);
        }
        if (!TextUtils.isEmpty(docsUrl)) {
            params.put("docs", docsUrl);
        }

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.publish_task, new TypeReference<ResultInfo<TaskInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }



}
