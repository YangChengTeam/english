package com.yc.english.news.view.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.umeng.socialize.sina.helper.MD5;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.PathUtils;
import yc.com.blankj.utilcode.util.TimeUtils;
import yc.com.blankj.utilcode.util.ToastUtils;

/**
 * Created by wanglin  on 2017/9/6 15:15.
 */

public class ExoPlayerView extends LinearLayout implements View.OnClickListener, Player.EventListener {
    private Context mContext;
    private boolean isPlay;
    private SimpleExoPlayer player;
    private SeekBar mSeekBar;

    private TextView mTextViewTime;
    private ImageView mImageView;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    public static final int STATE_INITIALIZE = 0x10;
    public static final int STATE_PREPARED = 0x01;
    public static final int STATE_START = 0x02;
    public static final int STATE_PAUSE = 0x03;
    public static final int STATE_COMPLETE = 0x04;
    public static final int STATE_DESTROY = 0x05;
    public static final int STATE_STOP = 0x06;

    private int currentState;

    private ExecutorService executorService;


    public ExoPlayerView(Context context) {
        this(context, null);
    }

    public ExoPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExoPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);

    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.mediaplayer_view, this, true);
        mImageView = view.findViewById(R.id.mImageView);
        mSeekBar = view.findViewById(R.id.mSeekBar);
        mTextViewTime = view.findViewById(R.id.mTextViewTime);

        executorService = Executors.newSingleThreadExecutor();
//        mediaPlayer = new MediaPlayer();
        player = ExoPlayerFactory.newSimpleInstance(context);

//        player.setAudioStreamType(C.STREAM_TYPE_MUSIC);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes);
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setPlayWhenReady(false);
        initListener();

    }

    private void initListener() {
        mImageView.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new MySeekBarListener());
//        mediaPlayer.setOnPreparedListener(this);
//        mediaPlayer.setOnCompletionListener(this);
//        mediaPlayer.setOnBufferingUpdateListener(this);
//        mediaPlayer.setOnErrorListener(this);

        player.addListener(this);

    }


    //初始化播放器
    private void initMediaPlayer(String path) {

        reset();
        try {
            currentState = STATE_INITIALIZE;
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, mContext.getPackageName()));
            // This is the MediaSource representing the media to be played.
            Uri mp4VideoUri = Uri.parse(path);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mp4VideoUri);
            player.prepare(videoSource);

//            mediaPlayer.setDataSource(path);
//            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //重置播放器
    private void reset() {
        if (player == null) player = ExoPlayerFactory.newSimpleInstance(mContext);
        if (currentState == STATE_INITIALIZE || currentState == STATE_PAUSE ||
                currentState == STATE_COMPLETE || currentState == STATE_START
                || currentState == STATE_PREPARED || currentState == STATE_STOP) {
//            mediaPlayer.stop();
//            mediaPlayer.reset();

            player.stop();
        }
    }

    //播放文件
    private void play() {

        if (currentState == STATE_DESTROY) {
            return;
        }
        if (currentState == STATE_INITIALIZE) {
            TipsHelper.tips(mContext, "正在缓冲中，请稍候...");
            return;
        }
        try {


            if (null != player) player.setPlayWhenReady(true);// 开始
            currentState = STATE_START;
            mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.media_play));
            mHandler.postDelayed(myRunnable, 0);
        } catch (Exception e) {

        }
    }

    //暂停播放
    public void pause() {
        try {
            if (null != player && currentState == STATE_START) {
                currentState = STATE_PAUSE;
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.media_stop));
                player.setPlayWhenReady(false);

            }
        } catch (Exception e) {
        }
    }

    //播放完成
    private void complete() {
        if (null != player) {
            mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.media_stop));
            mSeekBar.setProgress(0);
            currentState = STATE_COMPLETE;
        }
    }



    @Override
    public void onClick(View v) {

        if (null != player)
            isPlay = player.isPlayingAd();


        LogUtils.e("TAG", isPlay);
        if (currentState == STATE_PREPARED && clickListener != null) {
            clickListener.onMediaClick();
        }
        if (isPlay) {
            pause();
        } else {
            play();
        }
    }




    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState==Player.STATE_READY){
                currentState = STATE_PREPARED;
                try {
                    mSeekBar.setMax((int) player.getDuration());//设置进度条
                    mTextViewTime.setText(TimeUtils.millis2String(player.getDuration(), new SimpleDateFormat("mm:ss", Locale.getDefault())));
                } catch (Exception e) {
                    LogUtils.e(e.getMessage());
                    ToastUtils.showShort("播放失败");
                }
            }else if (playbackState ==Player.STATE_ENDED){
                complete();
            }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    //进度条处理
    private class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo(seekBar.getProgress());
            mTextViewTime.setText(TimeUtils.millis2String(player.getCurrentPosition(), new SimpleDateFormat("mm:ss", Locale.getDefault())));
            play();
        }

    }


    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (player != null && player.isPlayingAd()) {
                    mSeekBar.setProgress((int) player.getCurrentPosition());
                    mTextViewTime.setText(TimeUtils.millis2String(player.getCurrentPosition(), new SimpleDateFormat("mm:ss", Locale.getDefault())));
                    mHandler.postDelayed(this, 10);
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    };

    /**
     * 调用setPath()方法
     *
     * @param path
     */

    public void setPath(String path) {

        LogUtils.e("TAG", path);
        try {
            String name = getFileName(path);
            File file = new File(PathUtils.makeDir(mContext, "news"), name);

            if (file.exists()) {//设置播放file文件
                LogUtils.e("from file");
//                mediaPlayer.reset();

                try {
                    initMediaPlayer(file.getAbsolutePath());
                } catch (Exception e) {
//                    mediaPlayer.setDataSource(path);
                }
            } else {
                LogUtils.e("from path");
                executorService.submit(new DownloadTask(path));
                initMediaPlayer(path);

            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());

        }

    }


    private class DownloadTask implements Runnable {
        private String mPath;

        private DownloadTask(String path) {
            this.mPath = path;
        }

        @Override
        public void run() {
            download(mPath);
        }
    }




    public void destroy() {
        currentState = STATE_DESTROY;
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        mHandler.removeCallbacks(myRunnable);
        executorService.shutdown();
        executorService = null;

    }

    private onMediaClickListener clickListener;

    public void setOnMediaClickListener(onMediaClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface onMediaClickListener {
        void onMediaClick();
    }


    private void download(String path) {
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String name = getFileName(path);
            File file = new File(PathUtils.makeDir(mContext, "news"), name);
            fileOutputStream = new FileOutputStream(file);
            if (file.exists() && file.length() == urlConnection.getContentLength()) {
                return;
            }
            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                if (currentState == STATE_DESTROY && file.length() < urlConnection.getContentLength()) {
                    file.delete();
                    break;
                }
                fileOutputStream.write(buffer, 0, bufferLength);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileName(String path) {
        String name = MD5.hexdigest(path);
        if (path.lastIndexOf("/") != -1) {
            name = path.substring(path.lastIndexOf("/") + 1);
        }
        return name;
    }
}
