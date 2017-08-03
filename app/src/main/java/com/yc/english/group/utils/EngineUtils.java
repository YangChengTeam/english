package com.yc.english.group.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.StudentInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/2 19:30.
 */

public class EngineUtils {


    /**
     * 获取班群成员列表
     * status ：1 已审核
     * status ：0 未审核
     * 查询好友请求列表时不用传sn
     * 查询具体某个班群时不用传master_id
     *
     *
     * @param
     * @param status
     * @return sn群号
     */
    public static Observable<ResultInfo<StudentInfoWrapper>> getMemberList(Context context,  String sn, String status, String master_id) {

        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        if (!TextUtils.isEmpty(master_id))
            params.put("master_id", master_id);
        if (!TextUtils.isEmpty(sn))
            params.put("sn", sn);

        return HttpCoreEngin.get(context).rxpost(NetConstan.memeber_list, new TypeReference<ResultInfo<StudentInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }


    /**
     * 根据群号查找群
     *
     * @param context
     * @param sn      群号
     * @return
     */
    public static Observable<ResultInfo<ClassInfoWarpper>> queryGroupById(Context context, String sn) {
        Map<String, String> params = new HashMap<>();
        params.put("sn", sn);

        return HttpCoreEngin.get(context).rxpost(NetConstan.query_group_by_groupId, new TypeReference<ResultInfo<ClassInfoWarpper>>() {
        }.getType(), params, true, true, true);

    }

}
