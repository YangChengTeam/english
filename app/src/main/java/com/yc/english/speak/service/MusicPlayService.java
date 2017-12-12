package com.yc.english.speak.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.speak.utils.AudioConstant;
import com.yc.english.speak.view.activity.ListenEnglishActivity;

import java.io.IOException;

/**
 * 播放服务
 */
public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = MusicPlayService.class.getSimpleName();

    public static final int MUSIC_CONTROL_PLAY = 0;

    public static final int MUSIC_CONTROL_PAUSE = 1;

    private MediaPlayer mMediaPlayer;

    private State mCurrentState;

    private String mSongPath;

    @Override
    public void onCreate() {
        super.onCreate();

        RxBus.get().register(this);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnBufferingUpdateListener(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ListenEnglishActivity.isClick = false;
        RxBus.get().post(AudioConstant.INIT_MEDIA_AND_PLAY, mMediaPlayer);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initMediaPlayer() {
        try {
            LogUtils.e("audio initMediaPlayer--->");
            if(mMediaPlayer != null && !StringUtils.isEmpty(mSongPath)) {

                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }
                setCurrentState(State.STATE_IDLE);
                mMediaPlayer.setDataSource(mSongPath);
                setCurrentState(State.START_INITIALIZED);
                mMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        setCurrentState(State.STATE_PREPARED);
        LogUtils.e("audio init finish --->");
        ListenEnglishActivity.isClick = true;
        play();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AudioConstant.PLAY_STATE)
            }
    )
    public void playState(String path) {
        try {
            /*if(!ListenEnglishActivity.isClick){
                return;
            }*/

            //继续播放
            if (!StringUtils.isEmpty(mSongPath) && !StringUtils.isEmpty(path) && mSongPath.equals(path)) {
                play();
                return;
            }

            mSongPath = path;
            initMediaPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(AudioConstant.PAUSE_STATE)
            }
    )
    public void onMusicControlEvent(String path) {
        try {
            pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.d(TAG, mp.getCurrentPosition() + "");
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onDestroy() {
        try {
            if (mMediaPlayer != null) {
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RxBus.get().unregister(this);
        super.onDestroy();
    }

    private void play() {
        LogUtils.e("play --->");
        if (mMediaPlayer != null && (mCurrentState == State.STATE_PAUSE || mCurrentState == State.STATE_PREPARED || mCurrentState == State.STATE_PLAYING || mCurrentState == State.STATE_COMPLETE)) {
            mMediaPlayer.start();
            setCurrentState(State.STATE_PLAYING);
            RxBus.get().post(AudioConstant.UPDATE_UI, "update_ui");
        }
    }

    private void pause() {
        if (mMediaPlayer != null && (mCurrentState == State.STATE_PLAYING || mCurrentState == State.STATE_PAUSE || mCurrentState == State.STATE_COMPLETE)) {
            mMediaPlayer.pause();
            setCurrentState(State.STATE_PAUSE);
        }
    }

    private void setCurrentState(State currentState) {
        mCurrentState = currentState;
    }

    private enum State {
        STATE_STOPPED, STATE_PREPARED, STATE_PLAYING, STATE_PAUSE, STATE_IDLE, STATE_END, STATE_ERROR, STATE_COMPLETE, START_INITIALIZED
    }
}
