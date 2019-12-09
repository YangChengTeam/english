package com.yc.junior.english.speak.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yc.junior.english.speak.contract.ListenPlayContract;
import com.yc.junior.english.speak.model.bean.ListenEnglishBean;
import com.yc.junior.english.speak.utils.EnglishLyricBean;

import java.io.File;
import java.io.IOException;

import androidx.annotation.Nullable;
import yc.com.blankj.utilcode.util.FileUtils;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.SDCardUtils;
import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.TimeUtils;
import yc.com.blankj.utilcode.util.ToastUtils;

/**
 * 播放服务
 * model
 */
public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = MusicPlayService.class.getSimpleName();


    private MediaPlayer mMediaPlayer;

    private State mCurrentState;


    private boolean isURLValidate;


    private File lrcFile;
    private File audioFile;
    private ListenPlayContract.View mMainView;//将回调结果返回给主界面

    private EnglishLyricBean song;//歌曲相关信息解析

    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.setup(this);
        mHandler = new Handler();
        createMediaPlayer();
    }


    private void createMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setOnCompletionListener(MusicPlayService.this);
        mMediaPlayer.setOnPreparedListener(MusicPlayService.this);
        mMediaPlayer.setOnErrorListener(MusicPlayService.this);
        mMediaPlayer.setOnBufferingUpdateListener(MusicPlayService.this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new LocalBinder();
    }


    public class LocalBinder extends Binder {

        MusicPlayService getService() {
            return MusicPlayService.this;
        }


        public void init(ListenPlayContract.View view) {
            MusicPlayService.this.init(view);
        }

        public void play() {
            MusicPlayService.this.startPlay();
        }

        public void pause() {
            MusicPlayService.this.pause();
        }

        public void reset() {
            MusicPlayService.this.reset();
        }

        public void stop() {
            MusicPlayService.this.stopPlay();
        }

        public void pre() {
            MusicPlayService.this.pre();
        }

        public void next() {
            MusicPlayService.this.next();
        }

        public void downAudioFile(ListenEnglishBean listenEnglishBean) {
            MusicPlayService.this.downAudioFile(listenEnglishBean);
        }

        public void onBtnPlayPausePressed() {
            MusicPlayService.this.onBtnPlayPausePressed();
        }

        public void onProgressChanged(int progress) {
            MusicPlayService.this.onProgressChanged(progress);
        }

        public void destroy() {
            MusicPlayService.this.onDestroy();
        }
    }

    private void onProgressChanged(int progress) {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(progress);
            mMainView.updateSeekBar(progress);
        }
    }

    private void onBtnPlayPausePressed() {
        if (null == mMediaPlayer || null == song) {
            ToastUtils.showShort("暂无信息");
            return;
        }
        boolean isPlaying = mMediaPlayer.isPlaying();
        if (isPlaying) {
            pause();

        } else {
            startPlay();
        }
        mMainView.updatePlayButton(isPlaying);
    }


    /**
     * 下载MP3文件
     *
     * @param listenEnglishBean
     */
    public void downAudioFile(final ListenEnglishBean listenEnglishBean) {

        File fileDir = new File(SDCardUtils.getSDCardPath() + "/yc_english");
        if (FileUtils.createOrExistsDir(fileDir)) {

            String audioPath = fileDir + File.separator + listenEnglishBean.getId() + ".mp3";
            audioFile = new File(audioPath);
            if (!audioFile.exists()) {
                isURLValidate = URLIsValidate(listenEnglishBean.getMp3(), "mp3");
                if (isURLValidate) {
                    FileDownloader.getImpl().create(listenEnglishBean.getMp3()).setPath(audioPath, false).setListener(new FileDownloadSampleListener() {

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            downAudioLrcFile(listenEnglishBean);//继续下载资源词句文件
                        }

                    }).setAutoRetryTimes(3).start();
                } else {
                    ToastUtils.showLong("资源文件下载有误，请重试");
                }
            } else {
                downAudioLrcFile(listenEnglishBean);//继续下载资源词句文件
            }
        }
    }

    /**
     * 下载歌词文件
     */
    public void downAudioLrcFile(final ListenEnglishBean listenEnglishBean) {

        File fileDir = new File(SDCardUtils.getSDCardPath() + "/yc_english");

        if (FileUtils.createOrExistsDir(fileDir)) {

            String lrcPath = fileDir + File.separator + listenEnglishBean.getId() + ".lrc";
            lrcFile = new File(lrcPath);
            if (!lrcFile.exists()) {
                isURLValidate = URLIsValidate(listenEnglishBean.getWordFile(), "lrc");
                if (isURLValidate) {
                    FileDownloader.getImpl().create(listenEnglishBean.getWordFile()).setPath(lrcPath, false).setListener(new FileDownloadSampleListener() {

                        @Override
                        protected void completed(BaseDownloadTask task) {

                            //MP3和歌词全部下载完才算成功
                            mMainView.updateDone(true);

                            song = new EnglishLyricBean(audioFile.getAbsolutePath());
                            initMediaPlayer(audioFile.getAbsolutePath());

                        }


                    }).setAutoRetryTimes(3).start();
                } else {
                    ToastUtils.showLong("资源文件下载有误，请重试");
                }
            } else {
                mMainView.updateDone(true);
                song = new EnglishLyricBean(audioFile.getAbsolutePath());
                initMediaPlayer(audioFile.getAbsolutePath());
            }
        }
    }


    public boolean URLIsValidate(String url, String type) {

        if (StringUtils.isEmpty(url)) {
            return false;
        }

        if (url.substring(url.lastIndexOf("."), url.length()).toLowerCase().endsWith(type)) {
            return true;
        }

        return false;
    }


    private void init(ListenPlayContract.View mainView) {
        this.mMainView = mainView;
    }

    private void next() {
    }

    private void pre() {
    }

    private void pause() {
        if (mMediaPlayer != null && (mCurrentState == State.STATE_PLAYING)) {
            setCurrentState(State.STATE_PAUSE);
            mMediaPlayer.pause();
        }
    }

    private void stopPlay() {
        if (null != mMediaPlayer && (mCurrentState == State.STATE_PLAYING)) {
            setCurrentState(State.STATE_STOPPED);
            mMediaPlayer.stop();
        }
    }

    /**
     * 重置播放器状态
     * 和界面按钮的状态
     */
    private void reset() {
        if (null == song) return;
        try {
            if (null != mMediaPlayer && (mCurrentState == State.STATE_PLAYING ||
                    mCurrentState == State.STATE_COMPLETE || mCurrentState == State.STATE_STOPPED
                    || mCurrentState == State.STATE_PAUSE || mCurrentState == State.STATE_PREPARED
                    || mCurrentState == State.START_INITIALIZED || mCurrentState == State.STATE_IDLE)) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }

            mMainView.resetSeekBar(song.getDuration());
            mMainView.updateTitle(song.getmTitle());
            mMainView.updateArtist(song.getArtist());
            mMainView.setEndTime(TimeUtils.duration2String(song.getDuration()));
            mMainView.updatePlayButton(true);
        } catch (Exception e) {

            Log.e(TAG, "reset: " + e.getMessage());
        }


    }

    private void initMediaPlayer(String path) {
        try {
            LogUtils.e("audio initMediaPlayer--->" + path);
            if (mMediaPlayer != null && !TextUtils.isEmpty(path)) {

                reset();
                setCurrentState(State.STATE_IDLE);
                mMediaPlayer.setDataSource(path);
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
        try {
            startPlay();
        } catch (Exception e) {
            Log.d(TAG, "onPrepared() called with: mediaPlayer = [" + e.getMessage() + "]");
        }

    }

    public void startPlay() {
        try {
            if (mCurrentState == State.STATE_PREPARED || mCurrentState == State.STATE_PAUSE ||
                    mCurrentState == State.STATE_STOPPED || mCurrentState == State.STATE_COMPLETE) {
                setCurrentState(State.STATE_PLAYING);
                mMediaPlayer.start();
                mMainView.initLrcView(lrcFile);
                mMainView.updatePlayButton(false);
                mHandler.postDelayed(runnable, 0);
            }

        } catch (Exception e) {
            Log.d(TAG, "startPlay() called " + e.getMessage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMainView.updateSeekBar(mMediaPlayer.getCurrentPosition());
                    mMainView.updateLrcView(mMediaPlayer.getCurrentPosition());

                    mHandler.postDelayed(this, 100);
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    };


    @Override
    public void onCompletion(MediaPlayer mp) {
        //播放完成，播放下一首
        setCurrentState(State.STATE_COMPLETE);
        mMainView.onCompletion();
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
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mHandler.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    private void setCurrentState(State currentState) {
        mCurrentState = currentState;
    }

    private enum State {
        STATE_STOPPED, STATE_PREPARED, STATE_PLAYING, STATE_PAUSE, STATE_IDLE, STATE_END, STATE_ERROR, STATE_COMPLETE, START_INITIALIZED
    }
}
