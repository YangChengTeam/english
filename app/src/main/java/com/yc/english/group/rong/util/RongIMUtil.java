package com.yc.english.group.rong.util;

import android.text.TextUtils;

import com.yc.english.group.constant.RongConstant;
import com.yc.english.group.rong.RongCloud;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.ListGagGroupUserResult;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;
import rx.Observable;
import rx.Scheduler;
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
     * @param userId:用户                           Id。（必传）
     * @param groupId:群组                          Id。（必传）
     * @param minute:禁言时长，以分钟为单位，最大值为43200分钟。（必传）
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
     *
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

    /**
     * 查询被禁言群成员方法
     *
     * @param groupId:群组Id。（必传）
     * @return ListGagGroupUserResult
     **/

    public static Observable<ListGagGroupUserResult> lisGagUser(String groupId) {

        return Observable.just(groupId).subscribeOn(Schedulers.io()).map(new Func1<String, ListGagGroupUserResult>() {
            @Override
            public ListGagGroupUserResult call(String s) {
                try {
                    return rongCloud.group.lisGagUser(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * 插入系统消息
     *
     * @param contentMessage 消息的内容
     * @param groupId
     */
    public static void insertMessage(String contentMessage, String groupId) {
        InformationNotificationMessage message = InformationNotificationMessage.obtain(contentMessage);
        Message message1 = Message.obtain(groupId, Conversation.ConversationType.GROUP, message);
        RongIM.getInstance().sendMessage(message1, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });

    }


}
