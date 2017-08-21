package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.MemberInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/11 15:38.
 */

public class GroupCommonClassEngine extends BaseEngin {
    public GroupCommonClassEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<ClassInfoList>> getCommonClassList() {

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.index_comm_class_list, new TypeReference<ResultInfo<ClassInfoList>>() {
        }.getType(), null, true, true, true);
    }


    public Observable<ResultInfo<MemberInfo>> isGroupMember(String class_id, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("user_id", user_id);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.is_member, new TypeReference<ResultInfo<MemberInfo>>() {
        }.getType(), params, true, true, true);
    }
}
