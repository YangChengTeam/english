package com.yc.english.speak.utils;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;


/**
 * Created by zheng on 2016/10/7.
 */

public class MediaPlayerCreatedEvent {

    public MediaPlayer mMediaPlayer;

    public MediaPlayerCreatedEvent(@NonNull MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
    }
}
