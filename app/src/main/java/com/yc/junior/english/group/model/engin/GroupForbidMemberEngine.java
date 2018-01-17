package com.yc.junior.english.group.model.engin;

import android.content.Context;

import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.group.rong.models.CodeSuccessResult;
import com.yc.junior.english.group.rong.util.RongIMUtil;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/30 09:54.
 */

public class GroupForbidMemberEngine extends BaseEngin {

    public GroupForbidMemberEngine(Context context) {
        super(context);
    }

    public Observable<CodeSuccessResult> rollBackMember(String[] userId, String groupId) {
        return RongIMUtil.rollBackGagUser(userId, groupId);
    }
}
