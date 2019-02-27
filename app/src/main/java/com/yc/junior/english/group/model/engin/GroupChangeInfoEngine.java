package com.yc.junior.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.junior.english.group.constant.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/4 15:48.
 */

public class GroupChangeInfoEngine extends BaseEngin {
    public GroupChangeInfoEngine(Context context) {
        super(context);
    }


    /**
     * 解算班群
     *
     * @return
     */
    public Observable<ResultInfo<RemoveGroupInfo>> resolvingGroup(String class_id, String master_id) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("master_id", master_id);
        return HttpCoreEngin.get(mContext).rxpost(NetConstant.remove_group, new TypeReference<ResultInfo<RemoveGroupInfo>>() {
        }.getType(), params, true, true, true);
    }
}
