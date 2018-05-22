package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/4 19:37.
 */

public class GroupTransferGroupEngine extends BaseEngin {
    public GroupTransferGroupEngine(Context context) {
        super(context);
    }

    /**
     * （1）class_id:班级ID
     * (2) master_id:群主ID
     * (3) user_name:用户名/手机号
     *
     * @param class_id
     * @param master_id
     * @param user_name
     * @return
     */
    public Observable<ResultInfo<String>> transferGroup(String class_id, String master_id, String user_name) {

        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("master_id", master_id);
        params.put("user_name", user_name);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.transfer_group, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }
}
