package com.xinqu.videoplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import com.xinqu.videoplayer.manager.XinQuMediaManager;
import com.xinqu.videoplayer.manager.XinQuVideoPlayerManager;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class JZMediaSystem extends XinQuMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;
    public static boolean CURRENT_PLING_LOOP;
    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (dataSourceObjects.length > 1) {
                mediaPlayer.setLooping((boolean) dataSourceObjects[1]);
            }
            mediaPlayer.setOnPreparedListener(JZMediaSystem.this);
            mediaPlayer.setOnCompletionListener(JZMediaSystem.this);
            mediaPlayer.setOnBufferingUpdateListener(JZMediaSystem.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(JZMediaSystem.this);
            mediaPlayer.setOnErrorListener(JZMediaSystem.this);
            mediaPlayer.setOnInfoListener(JZMediaSystem.this);
            mediaPlayer.setOnVideoSizeChangedListener(JZMediaSystem.this);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
            if (dataSourceObjects.length > 2) {
                method.invoke(mediaPlayer, currentDataSource.toString(), dataSourceObjects[2]);
            } else {
                method.invoke(mediaPlayer, currentDataSource.toString(), null);
            }
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        mediaPlayer.seekTo((int) time);
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    @Override
    public void setLoop(boolean flag) {
        this.CURRENT_PLING_LOOP=flag;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if (currentDataSource.toString().toLowerCase().contains("mp3")) {
            XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                        XinQuVideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        XinQuVideoPlayerManager.getCurrentJzvd().onPrepared();
                    } else {
                        XinQuVideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, final int width, final int height) {
        XinQuMediaManager.instance().currentVideoWidth = width;
        XinQuMediaManager.instance().currentVideoHeight = height;
        XinQuMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (XinQuVideoPlayerManager.getCurrentJzvd() != null) {
                    XinQuVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged(width,height);
                }
            }
        });
    }
}
