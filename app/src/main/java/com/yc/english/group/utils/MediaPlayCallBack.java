package com.yc.english.group.utils;

/**
 * Created by wanglin  on 2017/8/15 15:54.
 */

public interface MediaPlayCallBack {

    int STATE_START = 0; // STATE 音频播放状态
    int STATE_PLAY = 1;
    int STATE_PAUSE = 2;
    int STATE_STOP = 3;
    int STATE_CUT = 4;

    int TYPE_PATH = 0; // TYPE 列表或者单个文件
    int TYPE_ID = 1;

    /**
     * @param type     播放模式
     * @param state    播放状态
     * @param position 当前播放的第几个音频
     */
    void mediaPlayCallBack(int type, int state, int position);

}
