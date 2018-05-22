package com.yc.junior.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.group.constant.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/10 21:22.
 */

public class GroupScoreTaskEngine extends BaseEngin {
    public GroupScoreTaskEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> taskScore(String id, String score) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("score", score);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.task_score, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }

}
