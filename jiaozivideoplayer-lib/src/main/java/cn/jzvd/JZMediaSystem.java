package cn.jzvd;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.lang.reflect.Method;

import static cn.jzvd.JZVideoPlayer.TAG;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class JZMediaSystem extends JZMediaInterface implements PLMediaPlayer.OnPreparedListener, PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnBufferingUpdateListener, PLMediaPlayer.OnSeekCompleteListener, PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnVideoSizeChangedListener {

    private Context mContext;
    public PLMediaPlayer mediaPlayer;
    private long time;

    public JZMediaSystem(Context context) {
        this.mContext = context;
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        try {
            AVOptions avOptions = new AVOptions();
//            avOptions.setInteger(AVOptions.KEY_BUFFER_TIME, 1000);
            avOptions.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
            mediaPlayer = new PLMediaPlayer(mContext, avOptions);
//            mediaPlayer.reset();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
            Class<PLMediaPlayer> clazz = PLMediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class);
            Log.i(TAG, "prepare: " + currentDataSource.toString());
            method.invoke(mediaPlayer, currentDataSource.toString());
            time = System.currentTimeMillis();
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "prepare: " + e.getMessage());
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
    public void seekTo(int time) {
        mediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    @Override
    public int getCurrentPosition() {
        return (int) mediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return (int) mediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

//    @Override
//    public void onPrepared(PLMediaPlayer mediaPlayer) {
//
//    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {
        long duration = System.currentTimeMillis() - time;
        Log.e("TAG", "onPrepared: " + duration + "ms");
        mediaPlayer.start();
        if (currentDataSource.toString().toLowerCase().contains("mp3")) {
            JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                        JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onCompletion(PLMediaPlayer mediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(PLMediaPlayer mediaPlayer, final int percent) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(PLMediaPlayer mediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, final int what) {
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onError(what, 0);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(PLMediaPlayer mediaPlayer, final int what, final int extra) {

        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        JZVideoPlayerManager.getCurrentJzvd().onPrepared();
                    } else {
                        JZVideoPlayerManager.getCurrentJzvd().onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }


//    @Override
//    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height, int i2, int i3) {
//
//    }


    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
        JZMediaManager.instance().currentVideoWidth = width;
        JZMediaManager.instance().currentVideoHeight = height;
        JZMediaManager.instance().mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    JZVideoPlayerManager.getCurrentJzvd().onVideoSizeChanged();
                }
            }
        });
    }
}
