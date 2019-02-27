package com.yc.soundmark.study.listener;

/**
 * Created by wanglin  on 2018/11/2 17:11.
 */
public interface OnUIPracticeControllerListener extends OnUIControllerListener {

    void playPracticeBeforeUpdateUI(int progress);

    void playPracticeFirstUpdateUI();//第一次播放后更新界面

    void playPracticeSecondUpdateUI();//第二次播放后更新界面

    void recordUpdateUI();//录音更新界面

    void playPracticeAfterUpdateUI();

    void updateProgressBar(int percent);

    void playPracticeThirdUpdateUI();//第三次播放更新界面
}
