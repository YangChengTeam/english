package cn.jzvd;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.lang.reflect.Method;

import static cn.jzvd.JZVideoPlayer.TAG;

/**
 * Created by Nathen on 2017/11/8.
 * 实现系统的播放引擎
 */
public class JZMediaPlMediaPlayer extends JZMediaInterface implements PLMediaPlayer.OnPreparedListener, PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnBufferingUpdateListener, PLMediaPlayer.OnSeekCompleteListener, PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnVideoSizeChangedListener {

    private Context mContext;
    private PLMediaPlayer mediaPlayer;
    private long time;

    public JZMediaPlMediaPlayer(Context context) {
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
//            avOptions.setString(AVOptions.KEY_CACHE_DIR, Config.getDefaultCacheDir(mContext));
            avOptions.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
            avOptions.setInteger(AVOptions.KEY_PREFER_FORMAT, AVOptions.PREFER_FORMAT_MP4);
            mediaPlayer = new PLMediaPlayer(mContext, avOptions);

            if (dataSourceObjects.length > 1) {
                mediaPlayer.setLooping((boolean) dataSourceObjects[1]);
            }
            mediaPlayer.setOnPreparedListener(JZMediaPlMediaPlayer.this);
            mediaPlayer.setOnCompletionListener(JZMediaPlMediaPlayer.this);
            mediaPlayer.setOnBufferingUpdateListener(JZMediaPlMediaPlayer.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(JZMediaPlMediaPlayer.this);
            mediaPlayer.setOnErrorListener(JZMediaPlMediaPlayer.this);
            mediaPlayer.setOnInfoListener(JZMediaPlMediaPlayer.this);
            mediaPlayer.setOnVideoSizeChangedListener(JZMediaPlMediaPlayer.this);
            Class<PLMediaPlayer> clazz = PLMediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class);
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
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void seekTo(long time) {
        if (mediaPlayer != null)
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
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public void setSurface(Surface surface) {
        if (mediaPlayer != null) {
            mediaPlayer.setSurface(surface);
        }
    }


    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {
        long duration = System.currentTimeMillis() - time;
        Log.e("TAG", "onPrepared: " + duration + "ms");
        if (mediaPlayer != null) {
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
        Log.e(TAG, "onError:  " + what + "");

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
