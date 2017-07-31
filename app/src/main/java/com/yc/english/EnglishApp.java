package com.yc.english;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.iflytek.cloud.SpeechUtility;
import com.yc.english.group.common.GroupApp;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class EnglishApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GroupApp.init(this);
        Utils.init(this);
        SpeechUtility.createUtility(EnglishApp.this, "appid=" + getString(R.string.app_id));
    }


}
