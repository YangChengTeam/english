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
 * Created by wanglin  on 2017/8/2 18:10.
 */

public class GroupApplyVerifyEngine extends BaseEngin {
    public GroupApplyVerifyEngine(Context context) {
        super(context);
    }


    /**
     * 同意加群请求
     *
     * @param class_id
     * @param master_id
     * @param user_ids
     * @return
     */
    public Observable<ResultInfo<String>> acceptApply(String class_id, String master_id, String user_ids) {

        Map<String, Object> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("master_id", master_id);
        params.put("members", user_ids);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.agree_join_group, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);

    }

}
