package com.yc.english.speak.view.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.yc.english.EnglishApp;
import com.yc.english.R;
import com.yc.english.speak.contract.MainPlayContract;
import com.yc.english.speak.utils.ImageUtils;
import com.yc.english.speak.utils.MediaPlayerCreatedEvent;
import com.yc.english.speak.utils.PlayList;
import com.yc.english.speak.utils.PlayServiceCreatedEvent;
import com.yc.english.speak.utils.Song;
import com.yc.english.speak.utils.TextUtils;
import com.yc.english.speak.utils.UpdateUiEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;



/**
 * Created by zhengken.me on 2016/11/27.
 * ClassName    : MainPresenter
 * Description  :
 */

public class MainPresenter implements MainPlayContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final int MSG_SEEK_BAR_REFRESH = 0;
    private static final int MSG_MUSIC_LRC_REFRESH = 1;

    private MainPlayContract.View mMainView;

    private PlayList mPlayList;

    private MediaPlayer mMediaPlayer;

    private Context mContext;

    public MainPresenter(@NonNull MainPlayContract.View mainView, Context context) {
        mMainView = mainView;
        mContext = context;
        mainView.setPresenter(this);

        mPlayList = PlayList.getmInstance();

        Song song = new Song(Environment.getExternalStorageDirectory()+"/000english/222.mp3");
        song.getLrcPath();
        mPlayList.addSong(song);

        EventBus.getDefault().register(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void processIntent(Intent intent) {
        Log.d(TAG, "intent.getAction = " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_MAIN)) {
            return;
        }
        Uri uri = intent.getData();
        Song song = new Song(uri);
        mPlayList.setIsThirdCall(true);
        mPlayList.setThirdSong(song);
        playMusic(mPlayList.getCurrSong());
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mPlayList.pause();
        }
    }

    @Override
    public void play() {
        handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        if (mMediaPlayer != null) {
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);
            mPlayList.play();
            mMainView.updatePlayButton(false);
        }
    }

    @Override
    public void destroy() {
        mMainView = null;
        handler.removeMessages(MSG_SEEK_BAR_REFRESH);
        handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        EventBus.getDefault().unregister(this);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SEEK_BAR_REFRESH:
                    mMainView.updateSeekBar(mMediaPlayer.getCurrentPosition());
                    sendEmptyMessageDelayed(MSG_SEEK_BAR_REFRESH, EnglishApp.get().getResources().getInteger(R.integer.seek_bar_refresh_interval));
                    break;
                case MSG_MUSIC_LRC_REFRESH:
                    if (mMediaPlayer != null) {
                        mMainView.updateLrcView(mMediaPlayer.getCurrentPosition());
                    }
                    sendEmptyMessageDelayed(MSG_MUSIC_LRC_REFRESH, 120);
                    break;
            }
        }
    };

    @Override
    public void playMusic(Song song) {
        //mMediaPlayer = new MediaPlayer();
        if (mMediaPlayer == null) {
            return;
        }
        mPlayList.play();
        mMainView.resetSeekBar(song.getDuration());
        mMainView.updateTitle(song.getmTitle());
        mMainView.updateArtist(song.getArtist());
        mMainView.setEndTime(TextUtils.duration2String(song.getDuration()));
        mMainView.updatePlayButton(false);
        handler.sendEmptyMessage(MSG_SEEK_BAR_REFRESH);
    }

    @Override
    public void onBtnPlayPausePressed() {
        if (mMediaPlayer == null || mPlayList.getCurrSong() == null) {
            ToastUtils.showShort(mContext.getResources().getString(R.string.app_name));
            return;
        }


        boolean isPlaying = mMediaPlayer.isPlaying();
        if (isPlaying) {
            // avoid ui stuck when quick switch play and pause
            handler.removeMessages(MSG_MUSIC_LRC_REFRESH);

            //pause
            mPlayList.pause();
        } else {
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);

            //play
            mPlayList.play();
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

    @Subscribe
    public void onPlayServiceCreated(PlayServiceCreatedEvent event) {
        playMusic(mPlayList.getCurrSong());
    }

    @Subscribe
    public void onMediaPlayerCreated(@NonNull MediaPlayerCreatedEvent event) {
        mMediaPlayer = event.mMediaPlayer;
    }

    @Subscribe
    public void onUpdateUI(UpdateUiEvent event) {
        Song song = mPlayList.getCurrSong();

        if (song == null) {
            return;
        }

        Bitmap bitmap = song.getCover();
        if (bitmap != null) {
            mMainView.updateCoverGauss(ImageUtils.fastblur(bitmap, 0.1f, 10));
            mMainView.updateCover(bitmap);
            mMainView.updateCoverMirror(ImageUtils.createReflectionBitmapForSingle(bitmap,
                    (int) EnglishApp.get().getResources().getDimension(R.dimen.cover_width_height),
                    (int) EnglishApp.get().getResources().getDimension(R.dimen.cover_mirror_height)));
        } else {
            mMainView.updateCoverGauss(null);
            mMainView.updateCover(null);
            mMainView.updateCoverMirror(null);
        }

        if (song.getLrcPath() != null) {
            mMainView.initLrcView(new File(song.getLrcPath()));
            handler.sendEmptyMessage(MSG_MUSIC_LRC_REFRESH);
        } else {
            mMainView.initLrcView(null);
            handler.removeMessages(MSG_MUSIC_LRC_REFRESH);
        }
    }
}
