package com.yc.soundmark.study.listener;

/**
 * Created by wanglin  on 2018/11/1 16:19.
 */
public interface OnUIApplyControllerListener extends OnUIControllerListener {


    void recordBeforeUpdateUI();//录音前更新界面

    void recordAfterUpdataUI();//录音后更新界面

    void playRecordBeforeUpdateUI();//播放录音前更新界面

    void playRecordAfterUpdateUI();//播放录音后更新界面

    void showEvaluateResult(String percent);//显示评测结果

    void playErrorUpdateUI();//播放错误更新界面


}
