package com.yc.junior.english.read.common;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.kk.utils.LogUtil;

import java.io.IOException;

/**
 * Created by wanglin  on 2019/4/24 15:06.
 */
public class MediaPlayerPlayer implements AudioPlayManager, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private OnUiUpdateManager updateManager;

    public MediaPlayerPlayer(OnUiUpdateManager updateManager) {
        this.updateManager = updateManager;
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void start(String url) {
        stop();

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.msg("e: " + e.getMessage());

            if (updateManager != null) {
                updateManager.onErrorUI(0, 0, "");
            }
        }
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }
        if (updateManager != null) {
            updateManager.onStopUI();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean isPlaying() {
        return null != mediaPlayer && mediaPlayer.isPlaying();
    }


    @Override
    public int getPlayPosition() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) return mediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (updateManager != null) {
            updateManager.onStartUI(mp.getDuration());
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (updateManager != null) {
            updateManager.onCompleteUI();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtil.msg("what: " + what + "  extra: " + extra);
        if (updateManager != null) {
            updateManager.onErrorUI(what, extra, "error");
        }
        return false;
    }
}
