package com.yc.junior.english.read.common;

/**
 * Created by wanglin  on 2019/4/24 14:08.
 */
public interface AudioPlayManager {

    void start(String url);

    void stop();

    void onDestroy();

    boolean isPlaying();

    int getPlayPosition();

}
