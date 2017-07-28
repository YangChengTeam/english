package com.yc.english.group.rong;

import com.yc.english.group.constant.IMConstant;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.rong.models.GroupUserQueryResult;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by wanglin  on 2017/7/25 10:33.
 * 获取token并登陆
 */

public class ImUtils {
    private static RongCloud rongCloud = RongCloud.getInstance(IMConstant.APP_KEY, IMConstant.APP_SECRET);

    /**
     * 登录获取token
     *
     * @param userName
     * @param userId
     * @param userPorait
     * @return
     */
    public static Observable<String> login(final String userName, final String userId, final String userPorait) {


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

    /**
     * 创建班级群组并同步群组
     *
     * @param userIds
     * @param groupId
     * @param groupName
     * @return
     */
    public static Observable<CodeSuccessResult> createGroup(final String[] userIds, final String groupId, final String groupName) {
        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, CodeSuccessResult>() {
            @Override
            public CodeSuccessResult call(String s) {
                try {
                    CodeSuccessResult codeSuccessResult = rongCloud.group.create(userIds, groupId, groupName);
                    if (codeSuccessResult.getCode() == 200) {
                        GroupInfo groupInfo = new GroupInfo(groupId + "", groupName);
                        final GroupInfo[] groupSyncGroupInfo = {groupInfo};
                        return rongCloud.group.sync(userIds[0], groupSyncGroupInfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
                return null;
            }
        });

    }

    /**
     * 根据群号查询群组
     *
     * @param groupId
     * @return
     */
    public static Observable<GroupUserQueryResult> queryGroup(String groupId) {
        return Observable.just(groupId).subscribeOn(Schedulers.io()).map(new Func1<String, GroupUserQueryResult>() {
            @Override
            public GroupUserQueryResult call(String s) {
                try {
                    return rongCloud.group.queryUser(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

    }

}