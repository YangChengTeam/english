package com.yc.junior.english.read.common;

/**
 * Created by wanglin  on 2019/4/24 14:49.
 */
public interface OnUiUpdateManager {

    void onCompleteUI();

    void onErrorUI(int what, int extra, String msg);

    void onStopUI();

    void onStartUI(int duration);

}
