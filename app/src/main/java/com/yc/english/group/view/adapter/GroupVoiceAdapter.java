package com.yc.english.group.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.RxUtils;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.Voice;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/7 20:28.
 */

public class GroupVoiceAdapter extends BaseAdapter<Voice> {
    private boolean mIsPublish;

    private SparseArray<MediaPlayer> mediaPlayerSparseArray = new SparseArray<>();

    public GroupVoiceAdapter(Context context, boolean isPublish, List<Voice> mList) {
        super(context, mList);
        this.mIsPublish = isPublish;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final int position) {

        holder.setVisible(R.id.m_iv_issue_voice_delete, mIsPublish);
        final Voice result = mList.get(position);

        String duration = result.getDuration();
        if (duration.equals("0''")) {
            duration = "1''";
        }
        holder.setText(R.id.m_tv_issue_result_voice, duration);

        RxView.clicks(holder.getView(R.id.m_iv_issue_voice_delete)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                deleteVoice(position);
                RxBus.get().post(BusAction.DELETE_VOICE, result);
            }
        });

        RxView.clicks(holder.getView(R.id.m_iv_play_voice)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                playController(holder, result);
            }
        });
    }


    private void deleteVoice(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_voice_item;
    }


    private int prePos = -1;//上一个播放的位置
    private ImageView preImageView = null;//上一个view

    private void playController(final BaseViewHolder holder, final Voice result) {

        int currentPos = mList.indexOf(result);
        MediaPlayer mediaPlayer;
        if (mediaPlayerSparseArray.get(currentPos) == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayerSparseArray.put(currentPos, mediaPlayer);
        } else {
            mediaPlayer = mediaPlayerSparseArray.get(currentPos);
        }

        ImageView imageView = holder.getView(R.id.m_iv_play_voice);

        /**
         *
         * 三种情况
         * ①.如果是首次点击，则直接设置数据，开始播放
         * ②.如果点击的是当前播放的，则暂停或继续播放
         * ③.如果点击的是其他条目，则停止上一个播放条目，开始当前播放
         */

        if (prePos < 0) {//①.如果是首次点击，则直接设置数据，开始播放
            setDataAndPlay(currentPos, mediaPlayer, imageView);
        } else {
            if (prePos == currentPos) {//②.如果点击的是当前播放的，则暂停或继续播放
                startOrPausePlay(currentPos, mediaPlayer, imageView);
            } else {//③.如果点击的是其他条目，则停止上一个播放条目，开始当前播放
                startNewPlay(prePos, currentPos, mediaPlayer, preImageView, imageView);
            }
        }
        prePos = currentPos;
        preImageView = imageView;


    }

    private void startNewPlay(int prePos, int currentPos, MediaPlayer mediaPlayer, ImageView preImageView, ImageView imageView) {
        stopLastPlay(prePos, preImageView);

        setDataAndPlay(currentPos, mediaPlayer, imageView);
    }

    private void setDataAndPlay(int currentPos, MediaPlayer mediaPlayer, ImageView imageView) {
        if (mIsPublish) {
            playUrl(mList.get(currentPos), mediaPlayer, imageView);
        } else {
            playFile(mList.get(currentPos), mediaPlayer, imageView);
        }
    }

    private void stopLastPlay(int prePos, ImageView preImageView) {
        MediaPlayer mediaPlayer = mediaPlayerSparseArray.get(prePos);
        releasePlayer(mediaPlayer);
        mediaPlayerSparseArray.remove(prePos);
        preImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.group67));

    }

    private void releasePlayer(MediaPlayer mediaPlayer) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            mediaPlayer = null;
        }

    }

    private void playFile(Voice result, final MediaPlayer mPlayer, final ImageView imageView) {
        RxUtils.getFile(mContext, result.getPath()).observeOn
                (AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
                if (file == null) return;
                try {
                    mPlayer.reset();
                    isPlayComplete = false;
                    mPlayer.setDataSource(mContext, Uri.parse(file.getAbsolutePath()));
                    mPlayer.prepare();
                    mPlayer.start();
                    Glide.with(mContext).load(R.mipmap.group59).into(imageView);
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.stop();
                            isPlayComplete = true;
                            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.group67));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.group67));
                }

            }
        });
    }

    /**
     * 暂停播放
     *
     * @param mediaPlayer
     */
    private void pausePlay(MediaPlayer mediaPlayer, ImageView imageView) {
        imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.group67));

        mediaPlayer.pause();

    }

    /**
     * 开始播放
     *
     * @param mediaPlayer
     * @param imageView
     */
    private void startPlay(MediaPlayer mediaPlayer, ImageView imageView) {
        Glide.with(mContext).load(R.mipmap.group59).into(imageView);
        mediaPlayer.start();

    }

    private void playUrl(Voice result, MediaPlayer mPlayer, final ImageView imageView) {
        try {
            mPlayer.reset();
            isPlayComplete = false;
            mPlayer.setDataSource(result.getFile().getPath());
            mPlayer.prepare();
            mPlayer.start();
            Glide.with(mContext).load(R.mipmap.group59).into(imageView);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    isPlayComplete = true;
                    imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.group67));
                }
            });

        } catch (IOException | RuntimeException e) {
            LogUtils.e("播放失败");
            if (mPlayer != null) {
                mPlayer.reset();
            }
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.group67));
        }
    }

    private boolean isPlayComplete = false;

    private void startOrPausePlay(int currentPos, MediaPlayer mediaPlayer, ImageView imageView) {

        /**
         * 判断
         * 如果当前点击的是播放的，则暂停
         * 如果当前点击是暂停的，则播放
         */

        if (!isPlayComplete) {
            if (mediaPlayer.isPlaying()) {
                pausePlay(mediaPlayer, imageView);
            } else {
                startPlay(mediaPlayer, imageView);
            }
        } else {
            setDataAndPlay(currentPos, mediaPlayer, imageView);
        }

    }


    public void destroyPlayer() {
        for (int i = 0; i < mediaPlayerSparseArray.size(); i++) {
            MediaPlayer mediaPlayer = mediaPlayerSparseArray.get(mediaPlayerSparseArray.keyAt(i));
            if (mediaPlayer != null) {
                releasePlayer(mediaPlayer);
            }
        }
    }

}
