package com.yc.english.base.utils;

/**
 * Created by wanglin  on 2019/4/25 10:19.
 */
public interface PermissionUIListener {

    void onPermissionGranted();//权限全获取

    void onPermissionDenyed();//权限被拒绝
}
