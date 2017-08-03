package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.ClassInfoWarpper;


import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/2 16:40.
 * 申请加入班群
 */

public class GroupApplyJoinEngine extends BaseEngin {

    public GroupApplyJoinEngine(Context context) {
        super(context);

    }

    public Observable<ResultInfo<String>> applyJoinGroup(String user_id, String sn) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("sn", sn);
        return HttpCoreEngin.get(mContext).rxpost(NetConstan.apply_join_group, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }



}
