package com.yc.english.group.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by wanglin  on 2017/9/27 17:43.
 * 一个音频播放的管理类
 */

public class MediaPlayerManager {

    private static MediaPlayer mediaPlayer;
    private static MediaPlayerManager instance;



    private static void init(Context context) {
        if (instance == null) {
            instance = new MediaPlayerManager();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }



}
