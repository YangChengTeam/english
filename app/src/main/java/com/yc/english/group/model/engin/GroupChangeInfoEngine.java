package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.EmptyUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/4 14:18.
 */

public class GroupChangeInfoEngine extends BaseEngin {
    public GroupChangeInfoEngine(Context context) {
        super(context);
    }

    /**
     * @param class_id  班级ID 必传
     * @param name      班级名称 修改班群名称时传该字段
     * @param face      班群图像 修改班群图像时传该字段
     * @param vali_type 验证类型，0：不验证加入，1：验证加入，2：拒绝加入
     * @return
     */
    public Observable<ResultInfo<String>> changeGroupInfo(String class_id, String name, String face, String vali_type) {
        Map<String, String> params = new HashMap<>();
        params.put("id", class_id);
        if (!TextUtils.isEmpty(name))
            params.put("name", name);
        if (!TextUtils.isEmpty(face))
            params.put("face", face);
        if (!TextUtils.isEmpty(vali_type))
            params.put("vali_type", vali_type);

        return HttpCoreEngin.get(mContext).rxpost(NetConstan.change_group_info, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, false, true, true);

    }

}
