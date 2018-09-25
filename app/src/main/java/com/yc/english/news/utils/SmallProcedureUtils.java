package com.yc.english.news.utils;

import android.app.Activity;

import com.kk.utils.LogUtil;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by wanglin  on 2018/8/30 09:50.
 */
public class SmallProcedureUtils {

    public static void switchSmallProcedure(Activity activity, String strs, String appId) {
        IWXAPI api = WXAPIFactory.createWXAPI(activity, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = strs; // 填小程序原始id
//                    req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);

    }
}
