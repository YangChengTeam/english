package com.yc.english.read.common;

import android.content.Context;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.read.view.activitys.ReadWordActivity;

/**
 * Created by admin on 2017/8/8.
 * 语音播放
 */

public class SpeechUtils {

    public static Context mContext;

    // 语音合成对象
    public static SpeechSynthesizer mTts;

    // 默认云端发音人
    public static String voicer = "catherine";

    // 引擎类型
    public static String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 缓冲进度
    public static int mPercentForBuffering = 0;

    // 播放进度
    public static int mPercentForPlaying = 0;

    public static boolean isSpeechSuccess = true;

    public static SpeechSynthesizer getTts(Context context) {
        initSpeech(context, 28, 50, 50, 1);
        return mTts;
    }


    public static void initSpeech(Context context, int speed, int pitch, int volume, int streamType) {
        mContext = context;
        if (mTts == null) {
            mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        }
        //语音播放参数设置
        //mTts.setParameter(SpeechConstant.PARAMS, null);//清空参数
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            // 设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, speed + "");
            // 设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, pitch + "");
            // 设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, volume + "");
        } else {
            //TODO
            //暂时只提供在线语音合成
        }
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, streamType + "");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 初始化监听
     */
    public static InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                TipsHelper.tips(mContext, "语音播放初始化失败");
                isSpeechSuccess = false;
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                isSpeechSuccess = true;
            }
        }
    };

}
