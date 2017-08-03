package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/2 19:27.
 */

public class GroupDeleteMemberEngine extends BaseEngin {
    public GroupDeleteMemberEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> deleteMember(String class_id, String master_id, String[] members) {
        Map<String, Object> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("master_id", master_id);
        params.put("members", members);

        return HttpCoreEngin.get(mContext).rxpost(NetConstan.del_group_member, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }
}
