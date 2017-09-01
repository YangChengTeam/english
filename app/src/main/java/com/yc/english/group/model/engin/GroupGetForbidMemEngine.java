package com.yc.english.group.model.engin;

import android.content.Context;

import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.rong.models.ListGagGroupUserResult;
import com.yc.english.group.rong.util.RongIMUtil;

import rx.Observable;

/**
 * Created by wanglin  on 2017/9/1 10:16.
 */

public class GroupGetForbidMemEngine extends BaseEngin {
    public GroupGetForbidMemEngine(Context context) {
        super(context);
    }


    /**
     * 获取禁言成员名单
     *
     * @param groupId
     * @return
     */
    public Observable<ListGagGroupUserResult> lisGagUser(String groupId) {
        return RongIMUtil.lisGagUser(groupId);
    }
}
