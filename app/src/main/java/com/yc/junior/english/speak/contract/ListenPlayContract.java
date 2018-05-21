package com.yc.junior.english.speak.contract;

import android.content.Intent;
import android.graphics.Bitmap;

import com.yc.junior.english.speak.utils.EnglishLyricBean;

import java.io.File;

public interface ListenPlayContract {

    interface View extends SpeakBaseView<Presenter> {

        void updateTitle(String title);

        void updateArtist(String artist);

        void setEndTime(String time);

        void resetSeekBar(int max);

        void setListener();

        void initLrcView(File lrcFile);

        void updateLrcView(int progress);

        void updateCoverGauss(Bitmap bitmap);

        void updateCover(Bitmap bitmap);

        void updateCoverMirror(Bitmap bitmap);

        /**
         * 设置 播放-暂停 按钮图片
         *
         * @param isPlaying -true: play图片 -false: pause图片
         */
        void updatePlayButton(boolean isPlaying);

        void updateSeekBar(int progress);

    }

    interface Presenter extends SpeakBasePresenter {

        void processIntent(Intent intent);

        void pause();

        void play();

        void playMusic(EnglishLyricBean song);

        void onBtnPlayPausePressed();

        void onProgressChanged(int progress);

        void destroy();

        void prev();

        void next();
    }
}
