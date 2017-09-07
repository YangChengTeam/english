package com.yc.english.news.view.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kk.utils.PathUtils;
import com.umeng.socialize.sina.helper.MD5;
import com.yc.english.R;
import com.yc.english.base.helper.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/9/6 15:15.
 */

public class MediaPlayerView extends LinearLayout {
    private Context mContext;
    private boolean isPlay;
    private MediaPlayer mediaPlayer;
    private SeekBar mSeekbar;
    private boolean isChanging;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView mTextViewTime;
    private ImageView mImageView;

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

    private void init(final Context context) {
        View view = View.inflate(context, R.layout.mediaplayer_view, null);
        mImageView = (ImageView) view.findViewById(R.id.mImageView);
        mSeekbar = (SeekBar) view.findViewById(R.id.mSeekBar);
        mTextViewTime = (TextView) view.findViewById(R.id.mTextViewTime);
        mSeekbar.setOnSeekBarChangeListener(new MySeekbarListenter());
        addView(view);
        mediaPlayer = new MediaPlayer();
    }


    /**
     * 开始播放
     */
    public void startPlay() {

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlay = !isPlay;

                if (isPlay) {
                    mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.media_play));
                    mediaPlayer.start();// 开始
                } else {
                    mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.media_stop));
                    stop();
                }

            }
        });


    }


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    //进度条处理
    private class MySeekbarListenter implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isChanging = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
            isChanging = false;
        }

    }

    /**
     * 先调用setPath()方法，在调用startPlay()方法
     *
     * @param path
     */
    public void setPath(String path) {
        if (mediaPlayer != null) {


            RxUtils.getFile(mContext, path).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                @Override
                public void call(File file) {
                    if (file == null) return;


                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(mContext, Uri.parse(file.getAbsolutePath()));

//                        AssetManager assetManager = mContext.getAssets();
//                        AssetFileDescriptor assetFileDescriptor =
//                                assetManager.openFd("test1.mp3");
//                        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
//                                assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                        mediaPlayer.prepare();// 准备

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mSeekbar.setMax(mediaPlayer.getDuration());//设置进度条
                    mTextViewTime.setText(mediaPlayer.getDuration() / 1000 + "");
                    //----------定时器记录播放进度---------//
                    mTimer = new Timer();
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (isChanging) {
                                return;
                            }
                            mSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    };
                    mTimer.schedule(mTimerTask, 0, 10);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mImageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.media_stop));
                            isPlay = false;

                        }
                    });


                }
            });
        }


    }

    public void destory() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }


    }
}
