package com.yc.soundmark.study.listener;

/**
 * Created by wanglin  on 2018/11/1 15:48.
 */
public interface OnAVManagerListener {

    void playMusic(String musicUrl, boolean isOnce, int playStep);

    void playMusic(String musicUrl);

    void stopMusic();


    void startRecordAndSynthesis(String word, boolean isWord);

    void stopRecord();

    boolean isRecording();

    void playRecordFile();

    void playAssetFile(String assetFilePath, int step);

    boolean isPlaying();//是否在播放

    void destroy();

}
