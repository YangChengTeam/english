package com.yc.english;

import android.app.Application;
import android.os.Build;


import com.blankj.utilcode.util.Utils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.group.common.GroupApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class EnglishApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GroupApp.init(this);
        Utils.init(this);
        init();
    }

    private void init(){
        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().appInfo != null) {
            params.put("app_version", GoagalInfo.get().appInfo.getVersionName() + "");
        }
        HttpConfig.setDefaultParams(params);
    }


}
