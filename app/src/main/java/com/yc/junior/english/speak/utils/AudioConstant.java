package com.yc.junior.english.speak.utils;

import android.support.annotation.Nullable;


public class AudioConstant {

    public static final String INIT_MEDIA_AND_PLAY = "init_media_and_play";

    public static final String UPDATE_UI = "update_ui";

    public static final String PLAY_STATE = "play_state";

    public static final String PAUSE_STATE = "pause_state";

    public int mCtrlId;

    public String mSongPath;

    public AudioConstant(int ctrlId, @Nullable String songPath) {
        mCtrlId = ctrlId;
        mSongPath = songPath;
    }

}
