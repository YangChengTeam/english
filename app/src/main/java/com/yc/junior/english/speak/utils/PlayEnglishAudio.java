package com.yc.junior.english.speak.utils;

import com.hwangjr.rxbus.RxBus;


public class PlayEnglishAudio {

    private static PlayEnglishAudio mInstance;

    private EnglishLyricBean mCurrentSong;

    public static PlayEnglishAudio getInstance() {
        if (mInstance == null) {
            mInstance = new PlayEnglishAudio();
        }
        return mInstance;
    }

    public void setSong(EnglishLyricBean song) {
        mCurrentSong = song;
    }

    public EnglishLyricBean getCurrSong() {
        return mCurrentSong;
    }

    public void play() {
        if (mCurrentSong != null) {
            RxBus.get().post(AudioConstant.PLAY_STATE, mCurrentSong.getmSongPath());
        }
    }

    public void pause() {
        RxBus.get().post(AudioConstant.PAUSE_STATE, "pause");
    }

    public void next() {
    }

    public void prev() {
    }
}
