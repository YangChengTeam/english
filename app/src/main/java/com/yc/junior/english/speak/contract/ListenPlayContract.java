package com.yc.junior.english.speak.contract;

import android.graphics.Bitmap;

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
         * @param isPlaying -true:  pause图片 -false:play图片
         */
        void updatePlayButton(boolean isPlaying);

        void updateSeekBar(int progress);

        void updateDone(boolean isDone);

        void onCompletion();//当前一首播放完成
    }

    interface Presenter extends SpeakBasePresenter {


        /**
         * 暂停
         */
        void pause();

        /**
         * 播放
         */
        void play();


        void onBtnPlayPausePressed();

        void onProgressChanged(int progress);

        void destroy();

        void prev();

        void next();
    }
}
