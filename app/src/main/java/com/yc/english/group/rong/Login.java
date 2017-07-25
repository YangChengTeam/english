package com.yc.english.group.rong;

import com.yc.english.group.constant.IMConstant;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by wanglin  on 2017/7/25 10:33.
 * 获取token并登陆
 */

public class Login {

    public static Observable<String> login(final String userName, final String userId, final String userPorait) {

        final RongCloud rongCloud = RongCloud.getInstance(IMConstant.APP_KEY, IMConstant.APP_SECRET);

        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                try {
                    return rongCloud.user.getToken(userId, userName, userPorait).toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        });
    }

}
