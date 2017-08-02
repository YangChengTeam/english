package com.yc.english.group.model.engin;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;

import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.group.constant.NetConstan;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/2 16:40.
 * 申请加入班群
 */

public class GroupApplyJoinEngine  {

    public GroupApplyJoinEngine(Context context) {

    }



    public Observable<ResultInfo<String>> applyJoinGroup(String user_id,String sn){

        Map<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("sn",sn);


        return null;

    }

}
