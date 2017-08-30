package com.yc.english.group.rong.util;

import com.yc.english.group.constant.RongConstant;
import com.yc.english.group.rong.RongCloud;
import com.yc.english.group.rong.models.CodeSuccessResult;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/8/30 09:37.
 */

public class RongIMUtil {

    public static RongCloud rongCloud = RongCloud.getInstance(RongConstant.appKey, RongConstant.appID);

    /**
     * 添加禁言成员
     *
     * @param  userId:用户 Id。（必传）
     * @param  groupId:群组 Id。（必传）
     * @param  minute:禁言时长，以分钟为单位，最大值为43200分钟。（必传）
     * @return
     */

    public static Observable<CodeSuccessResult> addGagUser(final String userId, final String groupId, final String minute) {

        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, CodeSuccessResult>() {
            @Override
            public CodeSuccessResult call(String s) {
                try {
                    return rongCloud.group.addGagUser(userId, groupId, minute);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });


    }

    /**
     * 解禁群成员
     * @param userId
     * @param groupId
     * @return
     */
    public static Observable<CodeSuccessResult> rollBackGagUser(final String[] userId, final String groupId) {

        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, CodeSuccessResult>() {
            @Override
            public CodeSuccessResult call(String s) {
                try {
                    return rongCloud.group.rollBackGagUser(userId, groupId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }


}
