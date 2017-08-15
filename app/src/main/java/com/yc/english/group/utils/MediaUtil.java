package com.yc.english.group.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;

import com.yc.english.group.model.bean.Voice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2017/8/7 19:49.
 */

public class MediaUtil {
    private MediaUtil() {
        // 禁止被外部类实例化
    }

    public static void startMediaPlayPathList(Context context, MediaPlayCallBack soundPlayCallBack, List<Voice> playList, int startIndex) {
        if (null == playList || playList.size() == 0 || startIndex >= playList.size()) {
            return;
        }
        mMediaPlayerCallBack = soundPlayCallBack;
        mAudioPlayIdList = new ArrayList<>();
        mAudioPlayIdList = playList;
        mCurrentListIndex = startIndex;
        if (null != mMediaPlayerCallBack) {
            mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_START, 0);
        }
        startMediaId(context, mAudioPlayIdList.get(mCurrentListIndex).getUri());
    }

    private static void startMediaId(final Context context, String path) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (null != mp) {
                    mp.stop();
                    mp.release();
                }
                mMediaPlayer = null;
                if (null != mAudioPlayIdList) {
                    mCurrentListIndex++;
                    if (mCurrentListIndex < mAudioPlayIdList.size()) {
                        mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_CUT, mCurrentListIndex);
                        startMediaId(context, mAudioPlayIdList.get(mCurrentListIndex).getUri());
                    } else {
                        if (null != mMediaPlayerCallBack) {
                            mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_STOP, mAudioPlayIdList.size());
                        }
                    }
                }
            }
        });
    }

    public static void startMediaPlayUriList(Context context, MediaPlayCallBack soundPlayCallBack, List<Voice> playList, int startIndex) {
        if (null == playList || playList.size() == 0 || startIndex >= playList.size()) {
            return;
        }
        mMediaPlayerCallBack = soundPlayCallBack;
        uriList = new ArrayList<>();
        uriList = playList;
        mCurrentListIndex = startIndex;
        if (null != mMediaPlayerCallBack) {
            mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_START, 0);
        }
        startMediaId(context, Uri.parse(uriList.get(mCurrentListIndex).getFile().getAbsolutePath()));
    }

    private static void startMediaId(final Context context, Uri uri) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (null != mp) {
                    mp.stop();
                    mp.release();
                }
                mMediaPlayer = null;
                if (null != uriList) {
                    mCurrentListIndex++;
                    if (mCurrentListIndex < uriList.size()) {
                        mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_CUT, mCurrentListIndex);
                        startMediaId(context, Uri.parse(uriList.get(mCurrentListIndex).getFile().getAbsolutePath()));
                    } else {
                        if (null != mMediaPlayerCallBack) {
                            mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_STOP, mAudioPlayIdList.size());
                        }
                    }
                }
            }
        });
    }

    public static void stopMedia() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer = null;

            if (null != mAudioPlayIdList) {
                if (null != mMediaPlayerCallBack) {
                    mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_ID, MediaPlayCallBack.STATE_PAUSE, -1);
                    mMediaPlayerCallBack = null;
                }
                mAudioPlayIdList = null;
            }
            mCurrentListIndex = 0;
            if (null != mMediaPlayerCallBack) {
                mMediaPlayerCallBack.mediaPlayCallBack(MediaPlayCallBack.TYPE_PATH, MediaPlayCallBack.STATE_PAUSE, -1);
            }
        }
    }

    private static int mCurrentListIndex = 0;
    private static MediaPlayer mMediaPlayer = null;
    private static MediaPlayCallBack mMediaPlayerCallBack = null;
    private static List<Voice> mAudioPlayIdList = null;
    private static List<Voice> uriList = null;
}




