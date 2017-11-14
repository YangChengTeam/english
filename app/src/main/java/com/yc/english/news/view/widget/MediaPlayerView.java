package com.yc.english.news.view.widget;

import android.content.Context;
import android.media.MediaPlayer;
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

import com.blankj.utilcode.util.TimeUtils;
import com.yc.english.R;

import org.w3c.dom.ProcessingInstruction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wanglin  on 2017/9/6 15:15.
 */

public class MediaPlayerView extends LinearLayout {
    private static final String TAG = "MediaPlayerView";
    private Context mContext;
    private boolean isPlay;
    private MediaPlayer mediaPlayer;
    private SeekBar mSeekBar;
    private boolean isChanging = true;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView mTextViewTime;
    private ImageView mImageView;
    private Handler handler = new Handler(Looper.getMainLooper());
    private MyRunnable myRunnable;


    private int count;

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
        mImageView = (ImageView) view.findViewById(R.id.mImageView);
        mSeekBar = (SeekBar) view.findViewById(R.id.mSeekBar);
        mTextViewTime = (TextView) view.findViewById(R.id.mTextViewTime);
        mSeekBar.setOnSeekBarChangeListener(new MySeekBarListener());


    }


    /**
     * 开始播放
     */
    public void startPlay() {

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = !isPlay;
                count++;
                if (count == 1 && clickListener != null) {
                    clickListener.onMediaClick();
                }
                if (isPlay) {
                    setPlay();
                } else {
                    stop();
                }

            }
        });


    }


    public void stop() {
        mImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.media_stop));
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    //进度条处理
    private class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isChanging = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            mTextViewTime.setText(TimeUtils.millis2String(mediaPlayer.getCurrentPosition(), new SimpleDateFormat("mm:ss")));
            setPlay();
            isPlay = true;
        }

    }

    private void setPlay() {
        mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.media_play));
        isChanging = false;

        mediaPlayer.start();// 开始
    }

    /**
     * 先调用setPath()方法，在调用startPlay()方法
     *
     * @param path
     */
    public void setPath(String path) {

        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);

            mediaPlayer.prepareAsync();// 准备

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {

                    mSeekBar.setMax(mp.getDuration());//设置进度条


                    mTextViewTime.setText(TimeUtils.millis2String(mp.getDuration(), new SimpleDateFormat("mm:ss")));
                    //----------定时器记录播放进度---------//
                    mTimer = new Timer();

                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (isChanging) {
                                return;
                            }
                            mSeekBar.setProgress(mp.getCurrentPosition());
                            myRunnable = new MyRunnable(mp);
                            handler.post(myRunnable);


                        }
                    };
                    mTimer.schedule(mTimerTask, 0, 10);


                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.media_stop));
                    isPlay = false;

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private class MyRunnable implements Runnable {
        private MediaPlayer mMediaPlayer;

        private MyRunnable(MediaPlayer mp) {
            this.mMediaPlayer = mp;

        }

        @Override
        public void run() {

            if (mediaPlayer != null && mMediaPlayer.isPlaying()) {
                mTextViewTime.setText(TimeUtils.millis2String(mMediaPlayer.getCurrentPosition(), new SimpleDateFormat("mm:ss")));
            }
        }
    }


    public void destroy() {
        isChanging = true;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(myRunnable);
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        count = 0;
    }

    private onMediaClickListener clickListener;

    public void setOnMediaClickListener(onMediaClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface onMediaClickListener {
        void onMediaClick();
    }

}
