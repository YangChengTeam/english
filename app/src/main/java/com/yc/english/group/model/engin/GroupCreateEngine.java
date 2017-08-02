package com.yc.english.group.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/1 15:07.
 */

public class GroupCreateEngine extends BaseEngin<ResultInfo<ClassInfo>> {
    public GroupCreateEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return NetConstan.create_group;
    }

    public Observable<ResultInfo<ClassInfo>> createGroup(String user_id, String groupName, String face) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("name", groupName);
        if (!TextUtils.isEmpty(face))
            params.put("face", face);
        return rxpost(new TypeReference<ResultInfo<ClassInfo>>() {
        }.getType(), params, true, true, true);

    }
}
