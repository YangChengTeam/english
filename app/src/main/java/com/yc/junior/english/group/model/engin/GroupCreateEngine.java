package com.yc.junior.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.junior.english.group.constant.NetConstant;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/1 15:07.
 */

public class GroupCreateEngine extends BaseEngin<ResultInfo<ClassInfoWarpper>> {
    public GroupCreateEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstant.create_group;
    }

    public Observable<ResultInfo<ClassInfoWarpper>> createGroup(String user_id, String groupName, String face, String type) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("name", groupName);
        if (!TextUtils.isEmpty(face))
            params.put("face", face);
        params.put("type", type);
        return rxpost(new TypeReference<ResultInfo<ClassInfoWarpper>>() {
        }.getType(), params, true, true, true);

    }


}
