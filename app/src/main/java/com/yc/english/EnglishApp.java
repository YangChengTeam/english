package com.yc.english;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.share.UMShareImpl;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yc.english.base.helper.EnginHelper;
import com.yc.english.base.model.ShareInfo;
import com.yc.english.base.utils.RongIMUtil;
import com.yc.english.base.utils.SpeechUtils;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.group.common.GroupApp;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.read.common.ReadApp;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class EnglishApp extends MultiDexApplication {


    @Override
    public void onCreate() {
        gEnglishApp = this;
        super.onCreate();

        Utils.init(EnglishApp.this);

        Observable.just("").observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                GroupApp.init(EnglishApp.this);
                ReadApp.init(EnglishApp.this);
                SpeechUtils.setDefaultAppid(EnglishApp.this);
                init();
            }
        });
        SpeechUtils.setAppids(this);
    }


    private void init() {
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

        setHttpDefaultParams();

        UserInfoHelper.login(this);

        UMShareImpl.Builder builder = new UMShareImpl.Builder();

        builder.setWeixin("wx97247860e3d30d2f", "68931a7e136b97bebeb46754082aae0a")
                .setQQ("1106261461", "p1PGwoz27nVHqoC5")
                .setDebug(true)
                .build(this);


        EnginHelper.getShareInfo(getApplicationContext()).subscribe(new Action1<ResultInfo<ShareInfo>>() {
            @Override
            public void call(ResultInfo<ShareInfo> shareInfoResultInfo) {
                if (shareInfoResultInfo != null && shareInfoResultInfo.data != null && shareInfoResultInfo.data.getInfo()
                        != null) {
                    SharePopupWindow.setmShareInfo(shareInfoResultInfo.data.getInfo());
                }
            }
        });


    }

    private static EnglishApp gEnglishApp;

    public static EnglishApp get() {
        return gEnglishApp;
    }

    public void setHttpDefaultParams() {
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
        if (!SPUtils.getInstance().getString("period", "").isEmpty()) {
            params.put("period", SPUtils.getInstance().getString("period", ""));
        }
        if (SPUtils.getInstance().getInt("grade", 0) != 0) {
            params.put("default_grade", SPUtils.getInstance().getInt("grade", 0) + "");
        }
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sys_version", sv);
        if (GoagalInfo.get().appInfo != null) {
            params.put("app_version", GoagalInfo.get().appInfo.getVersionCode() + "");
        }
        HttpConfig.setDefaultParams(params);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RongIMUtil.disconnect();
    }


}
