package com.yc.english.base.utils;

import android.content.Context;
import android.net.Uri;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.helper.EnginHelper;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * Created by zhangkai on 2017/8/9.
 */

public class RongIMUtil {
    public static void refreshUserInfo() {
        RongIM.getInstance().refreshUserInfoCache(getUserInfo());
    }

    public static UserInfo getUserInfo() {
        com.yc.english.main.model.domain.UserInfo userInfo = UserInfoHelper.getUserInfo();
        UserInfo ruserInfo = new UserInfo(userInfo.getUid(), userInfo.getNickname(), Uri.parse(userInfo.getAvatar()));
        return ruserInfo;
    }

    public static void setUserInfo() {
        RongIM.getInstance().setCurrentUserInfo(getUserInfo());
    }


    public static Observable<ResultInfo<com.yc.english.main.model.domain.UserInfo>> refreshObservable;

    public static void refreshUserInfo(final Context context,final String uid){
        if(refreshObservable == null) {
            refreshObservable = EnginHelper.getUserInfo(context, uid).debounce(1, TimeUnit.DAYS);
        }
        refreshObservable.subscribe(new
                                                                                                                 Action1<ResultInfo<com.yc.english.main
                 .model.domain
                .UserInfo>>() {
             @Override
             public void call(final ResultInfo<com.yc.english.main.model.domain.UserInfo> userInfoResultInfo) {
                 ResultInfoHelper.handleResultInfo(userInfoResultInfo, new ResultInfoHelper.Callback() {
                     @Override
                     public void resultInfoEmpty(String message) {

                     }

                     @Override
                     public void resultInfoNotOk(String message) {

                     }

                     @Override
                     public void reulstInfoOk() {
                         if(userInfoResultInfo.data != null) {
                             com.yc.english.main.model.domain.UserInfo userInfo = userInfoResultInfo.data;
                             UserInfo ruserInfo = new UserInfo(userInfo.getUid(), userInfo.getNickname(), Uri.parse(userInfo.getAvatar()));
                             RongIM.getInstance().refreshUserInfoCache(ruserInfo);
                         }
                     }
                 });
             }
         });
    }
}
