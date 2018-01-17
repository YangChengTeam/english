package com.yc.junior.english.news.view.widget;

import android.content.Context;
import android.media.AudioManager;
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

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kk.utils.PathUtils;
import com.umeng.socialize.sina.helper.MD5;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.TipsHelper;

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

/**
 * Created by wanglin  on 2017/9/6 15:15.
 */

public class MediaPlayerView extends LinearLayout implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, View.OnClickListener {
    private Context mContext;
    private boolean isPlay;
    private MediaPlayer mediaPlayer;
    private SeekBar mSeekBar;

    private TextView mTextViewTime;
    private ImageView mImageView;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    public static final int STATE_INITIALIZE = 0x10;
    public static final int STATE_PREPARED = 0x01;
    public static final int STATE_START = 0x02;
    public static final int STATE_PAUSE = 0x03;
    public static final int STATE_STOP = 0x04;
    public static final int STATE_DESTROY = 0x05;

    private int currentState;
    private MyRunnable myRunnable;

    private ExecutorService executorService;


    public MediaPlayerView(Context context) {
        this(context, null);
    }

    public MediaPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(context);

    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.mediaplayer_view, this, true);
        mImageView = view.findViewById(R.id.mImageView);
        mSeekBar = view.findViewById(R.id.mSeekBar);
        mTextViewTime = view.findViewById(R.id.mTextViewTime);
        mSeekBar.setOnSeekBarChangeListener(new MySeekBarListener());
        executorService = Executors.newSingleThreadExecutor();
        mImageView.setOnClickListener(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    /**
     * 开始播放
     */
    private void start() {
        if (currentState == STATE_DESTROY) {
            return;
        }
        if (currentState == STATE_INITIALIZE) {
            TipsHelper.tips(mContext, "正在缓冲中，请稍候...");
            return;
        }
        try {
            if (currentState == STATE_PREPARED || currentState == STATE_PAUSE || currentState == STATE_STOP) {
                mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.media_play));
                mediaPlayer.start();// 开始
                myRunnable = new MyRunnable();
                mHandler.postDelayed(myRunnable, 0);
                currentState = STATE_START;
            }
        } catch (Exception e) {
            ToastUtils.showShort("播放失败");
        }

    }

    public void stop() {
        if (mediaPlayer != null && currentState == STATE_START) {
            mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.media_stop));
            mediaPlayer.pause();
            currentState = STATE_PAUSE;
        }
    }

    @Override
    public void onClick(View v) {

        isPlay = !isPlay;
        if (currentState == STATE_PREPARED && clickListener != null) {
            clickListener.onMediaClick();
        }
        if (isPlay) {
            start();
        } else {
            stop();
        }
    }

    //进度条处理
    private class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            mTextViewTime.setText(TimeUtils.millis2String(mediaPlayer.getCurrentPosition(), new SimpleDateFormat("mm:ss", Locale.getDefault())));
            start();
            isPlay = true;
        }

    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (mediaPlayer != null) {
                    mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    mTextViewTime.setText(TimeUtils.millis2String(mediaPlayer.getCurrentPosition(), new SimpleDateFormat("mm:ss", Locale.getDefault())));
                    mHandler.postDelayed(this, 10);
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    }


    /**
     * 调用setPath()方法
     *
     * @param path
     */

    public void setPath(String path) {

        try {
            String name = getFileName(path);
            File file = new File(PathUtils.makeDir(mContext, "news"), name);

            if (file.exists()) {//设置播放file文件
                LogUtils.e("from file");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(mContext, Uri.parse(file.getAbsolutePath()));
                mediaPlayer.prepareAsync();// 准备
                currentState =STATE_PREPARED;
            } else {
                LogUtils.e("from path");
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                currentState = STATE_INITIALIZE;
                mediaPlayer.prepareAsync();// 准备
                executorService.submit(new DownloadTask(path));
            }


        } catch (IOException e) {
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

    @Override
    public void onPrepared(final MediaPlayer mp) {
        try {

            mSeekBar.setMax(mp.getDuration());//设置进度条

            mTextViewTime.setText(TimeUtils.millis2String(mp.getDuration(), new SimpleDateFormat("mm:ss", Locale.getDefault())));

            currentState = STATE_PREPARED;

        } catch (Exception e) {
            LogUtils.e(e.getMessage());
            ToastUtils.showShort("播放失败");
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.media_stop));
        isPlay = false;
        currentState = STATE_STOP;
    }

    public void destroy() {
        currentState = STATE_DESTROY;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
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
