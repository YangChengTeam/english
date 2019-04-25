package com.yc.junior.english.read.common;

import com.kk.utils.LogUtil;
import com.yc.soundmark.base.constant.SpConstant;
import com.ywl5320.libenum.MuteEnum;
import com.ywl5320.libenum.SampleRateEnum;
import com.ywl5320.libmusic.WlMusic;
import com.ywl5320.listener.OnCompleteListener;
import com.ywl5320.listener.OnErrorListener;
import com.ywl5320.listener.OnPreparedListener;

import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by wanglin  on 2019/4/24 14:27.
 */
public class WlMusicPlayer implements AudioPlayManager, OnPreparedListener, OnCompleteListener, OnErrorListener {

    private WlMusic wlMusic;
    private OnUiUpdateManager updateManager;

    public WlMusicPlayer(OnUiUpdateManager manager) {
        this.updateManager = manager;
        wlMusic = WlMusic.getInstance();

//        wlMusic.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3"); //设置音频源
        wlMusic.setCallBackPcmData(true);//是否返回音频PCM数据
        wlMusic.setShowPCMDB(true);//是否返回音频分贝大小
        wlMusic.setPlayCircle(false); //设置不间断循环播放音频
        wlMusic.setVolume(100); //设置音量 65%
        wlMusic.setPlaySpeed(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
        wlMusic.setPlayPitch(1.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
        wlMusic.setMute(MuteEnum.MUTE_CENTER); //设置立体声（左声道、右声道和立体声）
        wlMusic.setConvertSampleRate(SampleRateEnum.RATE_44100);//设定恒定采样率（null为取消）
        wlMusic.setOnPreparedListener(this);
        wlMusic.setOnCompleteListener(this);
        wlMusic.setOnErrorListener(this);
    }


    @Override
    public void start(String url) {
        stop();

        try {
            wlMusic.setSource(url); //设置音频源
            int anInt = SPUtils.getInstance().getInt(SpConstant.PLAY_SPEED, 40);
            float speed = anInt / (40 * 1f);
            if (speed == 0) {
                speed = 0.25f;
            }

            LogUtil.msg("speed: " + speed);
            wlMusic.setPlaySpeed(speed); //设置播放速度 (1.0正常) 范围：0.25---4.0f

            wlMusic.prePared();
        } catch (Exception e) {
            LogUtil.msg("e:  " + e.getMessage());
            if (updateManager != null) {
                updateManager.onErrorUI(0, 0, e.getMessage());
            }
        }

    }

    @Override
    public void stop() {
        if (wlMusic != null && wlMusic.isPlaying()) {
            wlMusic.stop();
        }
    }

    @Override
    public void onPrepared() {
        if (wlMusic != null) {
            wlMusic.start();
        }
    }


    @Override
    public void onDestroy() {
        if (null != wlMusic) {
            if (wlMusic.isPlaying()) {
                wlMusic.stop();
            }
            wlMusic = null;
        }
    }

    @Override
    public boolean isPlaying() {
        return null != wlMusic && wlMusic.isPlaying();
    }

    @Override
    public int getPlayPosition() {
        return 0;
    }

    @Override
    public void onComplete() {
        if (updateManager != null) {
            updateManager.onCompleteUI();
        }
    }

    @Override
    public void onError(int code, String msg) {
        if (updateManager != null) {
            updateManager.onErrorUI(code, code, msg);
        }
    }
}
