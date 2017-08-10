package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.ClassInfoList;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/3 12:00.
 */

public class GroupMyGroupListEngine extends BaseEngin {

    public GroupMyGroupListEngine(Context context) {
        super(context);
    }

}
