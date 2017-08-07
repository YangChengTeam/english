package com.yc.english.group.constant;

/**
 * Created by wanglin  on 2017/7/31 11:05.
 */

public interface GroupConstant {
    /**
     * 好友验证条件
     */
    int CONDITION_ALL_ALLOW = 0;//允许所有人加入
    int CONDITION_VERIFY_JOIN = 1;//通过验证才能加入
    int CONDITION_ALL_FORBID = 2;//不允许任何人加入


    String VERIFY_RESULT = "verify_result";

    String SYNC_GROUP_RESULT = "sync_group_result";

}
