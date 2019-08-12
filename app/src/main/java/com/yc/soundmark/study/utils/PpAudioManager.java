package com.yc.soundmark.study.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.yc.soundmark.study.listener.OnAVManagerListener;
import com.yc.soundmark.study.listener.OnUIPracticeControllerListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import yc.com.blankj.utilcode.util.LogUtils;

/**
 * Created by wanglin  on 2018/11/3 09:26.
 * 发音练习管理类
 */
public class PpAudioManager implements OnAVManagerListener {

    private Context mContext;
    private OnUIPracticeControllerListener controllerListener;

    private MediaPlayer mMediaPlayer;//播放MP3

    private boolean mIsOnce;//是否播放一次
    private AudioRecordFunc audioRecordFunc;//初始化录音管理
    private CompositeSubscription mCompositeSubscription;
    private int progress = 1;
    private int mStep = 1;


    public PpAudioManager(Context context, OnUIPracticeControllerListener controllerListener) {
        this.mContext = context;
        this.controllerListener = controllerListener;
        audioRecordFunc = AudioRecordFunc.getInstance();
        mCompositeSubscription = new CompositeSubscription();
        AVMediaManager.getInstance().addAudioManager(this);
    }


    @Override
    public void playMusic(String musicUrl, boolean isOnce, int playStep) {
        this.mIsOnce = isOnce;
        this.mStep = playStep;
        stopMusic();

        if (TextUtils.isEmpty(musicUrl)) return;
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        }
        try {
            mMediaPlayer.setDataSource(musicUrl);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(newPreparedListener);
        mMediaPlayer.setOnCompletionListener(newCompletionListener);

        mMediaPlayer.setOnErrorListener(newErrorListener);
    }

    @Override
    public void playMusic(String musicUrl) {
        playMusic(musicUrl, true, 0);
    }


    @Override
    public void stopMusic() {
        //停止ItemView缩放动画播放
//        controllerListener.playPracticeAfterUpdateUI();
        //停止音乐播放
        if (null != mMediaPlayer) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
//            mMediaPlayer.release();
            mMediaPlayer.reset();
            mMediaPlayer = null;
        }
    }

    private MediaPlayer.OnPreparedListener newPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (mIsOnce) {
                controllerListener.playBeforeUpdateUI();
            } else {
                controllerListener.playPracticeBeforeUpdateUI(progress);
            }
            mp.start();
        }
    };

    private MediaPlayer.OnCompletionListener newCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mIsOnce) {
                controllerListener.playAfterUpdateUI();
                stopMusic();
            } else {

                LogUtil.msg("step:  " + mStep + "  progress :" + progress);
                if (mStep == 1) {
                    controllerListener.playPracticeFirstUpdateUI();
                } else if (mStep == 2) {
                    controllerListener.playPracticeSecondUpdateUI();
                } else if (mStep == 3) {

                    controllerListener.recordUpdateUI();
                    if (audioRecordFunc == null) audioRecordFunc = AudioRecordFunc.getInstance();
                    audioRecordFunc.startRecordAndFile();
                    countDownRead();
                } else if (mStep == 4) {
                    if (progress < 3) {
                        controllerListener.playPracticeThirdUpdateUI();
                    } else if (progress == 3) {
                        controllerListener.playPracticeAfterUpdateUI();
                        progress = 1;
                        return;
                    }
                    progress++;

                }
            }
        }
    };

    private MediaPlayer.OnErrorListener newErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            ToastUtil.toast2(mContext, "播放失败！");

            LogUtil.msg("error->" + what + "  extra->" + extra);

            return false;
        }
    };


    @Override
    public void startRecordAndSynthesis(String word, boolean isWord) {

    }

    @Override
    public void stopRecord() {

    }

    @Override
    public boolean isRecording() {
        return false;
    }


    @Override
    public void playRecordFile() {
        try {
            LogUtil.msg("播放文件路径：" + AudioFileFunc.getWavFilePath());
            playMusic(AudioFileFunc.getWavFilePath(), false, mStep);
        } catch (Exception e) {
            LogUtils.e("prepare() failed");
        }
    }


    @Override
    public void playAssetFile(String assetFilePath,boolean isOnce, final int step) {
        this.mIsOnce= isOnce;
        stopMusic();
        mStep = step;
        if (TextUtils.isEmpty(assetFilePath)) return;

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        }
        AssetManager assetManager = mContext.getAssets();
        AssetFileDescriptor afd = null;
        try {
            afd = assetManager.openFd(assetFilePath);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(newPreparedListener);
            mMediaPlayer.setOnCompletionListener(newCompletionListener);

        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast2(mContext, "播放文件有误！");
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }


    private void countDownRead() {
        final int count = 3;
        //设置0延迟，每隔一秒发送一条数据
        Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.msg("--->" + aLong);
                        int temp = (int) ((double) aLong / (double) count * 100);
                        LogUtil.msg("progress--->" + temp);
//                        mProgressBar.setProgress(temp);

                        controllerListener.updateProgressBar(temp);
                        if (aLong == 0) {

                            controllerListener.updateProgressBar(100);


//                            停止录音
                            if (audioRecordFunc != null) {
                                audioRecordFunc.stopRecordAndFile();
                            }

                            mStep += 1;
                            LogUtil.msg("播放用户录音文件--->" + AudioFileFunc.getWavFilePath());
                            playRecordFile();

                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }


    @Override
    public void destroy() {
        stopMusic();
        if (controllerListener != null) {
            controllerListener.playPracticeAfterUpdateUI();
            controllerListener.playAfterUpdateUI();
        }
        progress = 1;
        mStep = 1;

        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }

    }
}
