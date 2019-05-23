package com.yc.english.read.common;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kk.utils.LogUtil;
import com.yc.soundmark.base.constant.SpConstant;

import java.math.BigDecimal;

import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by wanglin  on 2019/5/22 14:33.
 */
public class ExoPlayer implements AudioPlayManager, Player.EventListener {

    private OnUiUpdateManager updateManager;

    private SimpleExoPlayer player;
    private Context mContext;



    public ExoPlayer(Context context, OnUiUpdateManager updateManager) {
        this.updateManager = updateManager;
        this.mContext = context;
        player = ExoPlayerFactory.newSimpleInstance(context);

        player.setPlayWhenReady(false);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes);

        player.addListener(this);
        Log.e("TAG", "ExoPlayer:  " + getClass().getName());
    }

    @Override
    public void start(String url) {
        try {

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, mContext.getPackageName()));
            // This is the MediaSource representing the media to be played.
            Uri mp4VideoUri = Uri.parse(url);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mp4VideoUri);
            int anInt = SPUtils.getInstance().getInt(SpConstant.PLAY_SPEED, 40);

            float speed = (anInt * 2 + 20) / (100 * 1.0f);
            BigDecimal bd = new BigDecimal(speed);
            PlaybackParameters parameters = new PlaybackParameters(bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());

            player.setPlaybackParameters(parameters);
            // Prepare the player with the source.
            player.prepare(videoSource);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.msg("e: " + e.getMessage());

            if (updateManager != null) {
                updateManager.onErrorUI(0, 0, "");
            }
        }

    }

    @Override
    public void stop() {
        if (player != null) {
            player.stop();
//           player.release();
        }
        if (updateManager != null) {
            updateManager.onStopUI();
        }
    }

    @Override
    public void onDestroy() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public boolean isPlaying() {
        return player != null && player.isPlayingAd();
    }

    @Override
    public int getPlayPosition() {
        if (player != null && player.isPlayingAd()) {
            return (int) player.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY) {
            player.setPlayWhenReady(true);
            if (updateManager != null) {
                updateManager.onStartUI((int) player.getDuration());
            }
        } else if (playbackState == Player.STATE_ENDED) {
            if (updateManager != null) {
                updateManager.onCompleteUI();
            }
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (updateManager != null) {
            updateManager.onErrorUI(error.type, error.rendererIndex, error.getRendererException().getMessage());
        }
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.e("TAG", "onPlaybackParametersChanged: " + playbackParameters.speed);
    }
}
