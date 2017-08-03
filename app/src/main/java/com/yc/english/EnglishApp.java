package com.yc.english;

import android.app.Application;
import android.os.Build;

import com.blankj.utilcode.util.Utils;
import com.iflytek.cloud.SpeechUtility;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yc.english.group.common.GroupApp;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class EnglishApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Observable.just("").observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                GroupApp.init(EnglishApp.this);
                Utils.init(EnglishApp.this);
                SpeechUtility.createUtility(EnglishApp.this, "appid=" + getString(R.string.app_id));
                init();
            }
        });
    }

    private void init(){
        //腾迅自动更新
        Bugly.init(getApplicationContext(), "965a5326ab", false);

        //友盟统计
        UMGameAgent.setDebugMode(true);
        UMGameAgent.init(this);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //全局信息初始化
        GoagalInfo.get().init(getApplicationContext());
        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAy/M1AxLZjZOyJToExpn1\n" +
                "hudAWySRzS+aGwNVdX9QX6vK38O7WUA7h/bYqBu+6tTRC6RnL9BksMrIf5m6D3rt\n" +
                "faYYmxfe/FI4ZD5ybIhFuRUi95e/J2vQVElsSNqSz7ewXquZpKZAqlzH4hGgOqmO\n" +
                "THlrwiQwX66bS7x7kDmvxMd5ZRGhTvz62kpKb/orcnWQ1KElNc/bIzTtv3jsrMgH\n" +
                "FVdFZfev91ew4Kf1YJbqGBGKslBsIoGsgTxI94T6d6XEFxSzdvrRwKhOobXIaOhZ\n" +
                "o3GBCZIA/1ZOwLK6RyrWdprz+60xifcYIkILdZ7yPazSfHCVHFY6o/fQjK4dxQDW\n" +
                "Gw0fxN9QX+v3+48nW7QIBx4KNYNIW/eetGhXpOwV4PjNt15fcwJkKsx2W3VQuh93\n" +
                "jdYB4xMyDUnRwb9np/QR1rmbzSm5ySGkmD7NAj03V+O82Nx4uxsdg2H7EQdVcY7e\n" +
                "6dEdpLYp2p+VkDd9t/5y1D8KtC35yDwraaxXveTMfLk8SeI/Yz4QaX6dolZEuUWa\n" +
                "tLaye2uA0w25Ee35irmaNDLhDr804B7U7M4kkbwY7ijvvhnfb1NwFY5lw/2/dZqJ\n" +
                "x2gH3lXVs6AM4MTDLs4BfCXiq2WO15H8/4Gg/2iEk8QhOWZvWe/vE8/ciB2ABMEM\n" +
                "vvSb829OOi6npw9i9pJ8CwMCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");

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
        params.put("device_type", "2");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sys_version", sv);
        if (GoagalInfo.get().appInfo != null) {
            params.put("app_version", GoagalInfo.get().appInfo.getVersionName() + "");
        }
        HttpConfig.setDefaultParams(params);

        UserInfoHelper.login(this);

    }


}
