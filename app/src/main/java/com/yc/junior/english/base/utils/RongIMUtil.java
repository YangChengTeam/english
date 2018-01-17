package com.yc.junior.english.base.utils;

import android.content.Context;
import android.net.Uri;

import com.blankj.utilcode.util.StringUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.EnginHelper;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.main.hepler.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/9.
 */

public class RongIMUtil {
    public static void refreshUserInfo() {
        RongIM.getInstance().refreshUserInfoCache(getUserInfo());
    }

    public static UserInfo getUserInfo() {
        com.yc.junior.english.main.model.domain.UserInfo userInfo = UserInfoHelper.getUserInfo();

        Uri uri = null;
        if (!StringUtils.isEmpty(userInfo.getAvatar())) {
            uri = Uri.parse(userInfo.getAvatar());
        }
        UserInfo ruserInfo = new UserInfo(userInfo.getUid(), userInfo.getNickname(), uri);
        return ruserInfo;
    }

    public static void setUserInfo() {
        RongIM.getInstance().setCurrentUserInfo(getUserInfo());
    }


    public static Observable<ResultInfo<com.yc.junior.english.main.model.domain.UserInfoWrapper>> refreshObservable;

    public static void refreshUserInfo(final Context context, final String uid) {
        if (refreshObservable == null) {
            refreshObservable = EnginHelper.getUserInfo(context, uid).debounce(1, TimeUnit.DAYS);
        }
        refreshObservable.subscribe(new
                                            Action1<ResultInfo<com.yc.junior.english.main
                                                    .model.domain
                                                    .UserInfoWrapper>>() {
                                                @Override
                                                public void call(final ResultInfo<com.yc.junior.english.main.model.domain.UserInfoWrapper> userInfoResultInfo) {
                                                    ResultInfoHelper.handleResultInfo(userInfoResultInfo, new ResultInfoHelper.Callback() {
                                                        @Override
                                                        public void resultInfoEmpty(String message) {

                                                        }

                                                        @Override
                                                        public void resultInfoNotOk(String message) {

                                                        }

                                                        @Override
                                                        public void reulstInfoOk() {
                                                            if (userInfoResultInfo.data != null) {
                                                                com.yc.junior.english.main.model.domain.UserInfo userInfo =
                                                                        userInfoResultInfo.data.getInfo();
                                                                UserInfo ruserInfo = new UserInfo(userInfo.getUid(), userInfo.getNickname(), Uri.parse(userInfo.getAvatar()));
                                                                RongIM.getInstance().refreshUserInfoCache(ruserInfo);
                                                            }
                                                        }
                                                    });
                                                }
                                            });
    }

    public static void disconnect() {
        RongIM.getInstance().disconnect();
    }


    public static boolean isConnect() {
        return RongIM.getInstance().getCurrentConnectionStatus().getValue() == 0;
    }

    public static void reconnect(Context context) {
//        if (!isConnect()) {
//            TipsHelper.tips(context, "正在重连连接");
//            com.yc.junior.english.main.model.domain.UserInfo userInfo = UserInfoHelper.getUserInfo();
//            UserInfoHelper.connect(context, userInfo.getUid());
//        }
    }

}
