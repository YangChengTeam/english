package com.yc.english.speak.presenter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.speak.contract.ListenPlayContract;
import com.yc.english.speak.utils.AudioConstant;
import com.yc.english.speak.utils.EnglishLyricBean;
import com.yc.english.speak.utils.PlayEnglishAudio;

import java.io.File;

public class LyricViewPresenter implements ListenPlayContract.Presenter {

    private static final int MSG_SEEK_BAR_REFRESH = 0;

    private static final int MSG_MUSIC_LRC_REFRESH = 1;

    private ListenPlayContract.View mMainView;

    private PlayEnglishAudio playEnglishAudio;

    private MediaPlayer mMediaPlayer;

    private Context mContext;

    private String mAudioPath;

    private boolean isPlay;

    public LyricViewPresenter(ListenPlayContract.View mainView, Context context, String audioPath) {
        RxBus.get().register(this);
        mMainView = mainView;
        mContext = context;
        mAudioPath = audioPath;
        mainView.setPresenter(this);

        playEnglishAudio = PlayEnglishAudio.getInstance();

        EnglishLyricBean currentSong = new EnglishLyricBean(audioPath);
        currentSong.setLrcPath();
        LogUtils.e("getLrcPath--->" + currentSong.getLrcPath());
        playEnglishAudio.setSong(currentSong);

        isPlay = true;
    }

    public void setSongPath(String audioPath) {
        if (playEnglishAudio == null) {
            playEnglishAudio = PlayEnglishAudio.getInstance();
        }
        EnglishLyricBean currentSong = new EnglishLyricBean(audioPath);
        currentSong.setLrcPath();
        playEnglishAudio.setSong(currentSong);
    }

    @Override
    public void start() {
    }

    @Override
    public void processIntent(Intent intent) {
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            playEnglishAudio.pause();
        }
    }

    @Override
    public void play() {
        handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        if (mMediaPlayer != null) {
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);
            playEnglishAudio.play();
            mMainView.updatePlayButton(false);
        }
    }

    @Override
    public void destroy() {
        mMainView = null;
        handler.removeMessages(MSG_SEEK_BAR_REFRESH);
        handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        RxBus.get().unregister(this);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isPlay()) {
                switch (msg.what) {
                    case MSG_SEEK_BAR_REFRESH:
                        try {
                            if (mMediaPlayer != null) {
                                mMainView.updateSeekBar(mMediaPlayer.getCurrentPosition());
                                sendEmptyMessageDelayed(MSG_SEEK_BAR_REFRESH, 120);
                            }
                        } catch (Exception e) {
                            LogUtils.e(e.getMessage());
                        }
                        break;
                    case MSG_MUSIC_LRC_REFRESH:
                        try {
                            if (mMediaPlayer != null) {
                                mMainView.updateLrcView(mMediaPlayer.getCurrentPosition());
                            }
                        } catch (Exception e) {
                            LogUtils.e(e.getMessage());
                        }
                        sendEmptyMessageDelayed(MSG_MUSIC_LRC_REFRESH, 120);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    public void playMusic(EnglishLyricBean song) {
        if (mMediaPlayer == null) {
            return;
        }
        if (song == null) {
            //song = new EnglishLyricBean(mAudioPath);
            return;
        }
        playEnglishAudio.play();
        mMainView.resetSeekBar(song.getDuration());
        mMainView.updateTitle(song.getmTitle());
        mMainView.updateArtist(song.getArtist());
        mMainView.setEndTime(TimeUtils.duration2String(song.getDuration()));
        mMainView.updatePlayButton(false);
        handler.sendEmptyMessage(MSG_SEEK_BAR_REFRESH);
    }

    @Override
    public void onBtnPlayPausePressed() {
        if (mMediaPlayer == null || playEnglishAudio.getCurrSong() == null) {
            ToastUtils.showShort("暂无信息");
            return;
        }

        boolean isPlaying = mMediaPlayer.isPlaying();
        if (isPlaying) {
            handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
            playEnglishAudio.pause();
        } else {
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);

            playEnglishAudio.play();
        }
        mMainView.updatePlayButton(isPlaying);
    }

    @Override
    public void onProgressChanged(int progress) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
            mMainView.updateSeekBar(progress);
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AudioConstant.INIT_MEDIA_AND_PLAY)
            }
    )
    public void onMediaPlayerCreated(MediaPlayer mediaPlayer) {
        try {
            mMediaPlayer = mediaPlayer;
            playMusic(playEnglishAudio.getCurrSong());
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AudioConstant.UPDATE_UI)
            }
    )
    public void onUpdateUI(String update) {
        EnglishLyricBean song = playEnglishAudio.getCurrSong();
        if (song == null) {
            song = new EnglishLyricBean(mAudioPath);
        }
        if (song.getLrcPath() != null) {
            mMainView.initLrcView(new File(song.getLrcPath()));
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);
        } else {
            mMainView.initLrcView(null);
            handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        }
    }

    @Override
    public void prev() {

    }

    @Override
    public void next() {

    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
