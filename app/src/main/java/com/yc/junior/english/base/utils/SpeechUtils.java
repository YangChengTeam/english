package com.yc.junior.english.base.utils;

import android.content.Context;

import com.iflytek.cloud.SpeechUtility;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.EnginHelper;
import com.yc.junior.english.read.common.AppidsInfo;

import java.util.List;

import rx.functions.Action1;
import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by zhangkai on 2017/8/24.
 */

public class SpeechUtils {
    private static List<String> appids;

    private static int index = 0;
    private static String appid = "";

    public static int getIndex() {
        index = SPUtils.getInstance().getInt("index", -1);
        return index;
    }

    public static void setIndex(int index) {
        SPUtils.getInstance().put("index", index);
        SpeechUtils.index = index;
    }

    public static String getAppid() {
        appid = SPUtils.getInstance().getString("appid", "");
        return appid;
    }

    public static void setAppid(String appid) {
        SPUtils.getInstance().put("appid", appid);
        SpeechUtils.appid = appid;
    }

    public static List<String> getAppids() {
        return appids;
    }

    public static void setAppids(Context context) {
        EnginHelper.getAppids(context).subscribe(new Action1<ResultInfo<AppidsInfo>>() {
            @Override
            public void call(ResultInfo<AppidsInfo> appidsInfoResultInfo) {
                if (appidsInfoResultInfo != null && appidsInfoResultInfo.data != null && appidsInfoResultInfo.data
                        .list != null) {
                    SpeechUtils.appids = appidsInfoResultInfo.data
                            .list;
                }
            }
        });
    }

    public static void setDefaultAppid(Context context) {
        int index = getIndex();
        if (index == -1) {
            SpeechUtility.createUtility(context, "appid=" + context.getString(R.string.app_id));
        } else {
            String appid = getAppid();
            SpeechUtility.createUtility(context, "appid=" + appid);
        }
    }


    public static void resetAppid(Context context) {
        List<String> appids = getAppids();
        int index = getIndex() + 1;
        if (appids.size() <= index) {
            return;
        } else {
            index = -1;
        }
        String appid = appids.get(index);
        setIndex(index);
        setAppid(appid);
        SpeechUtility.createUtility(context, "appid=" + appid);
    }
}
