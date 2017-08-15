package com.yc.english.group.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
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

    public GroupVoiceAdapter(Context context, boolean isPublish, List<Voice> mList) {
        super(context, mList);
        this.mIsPublish = isPublish;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final int position) {

        holder.setVisible(R.id.m_iv_issue_voice_delete, mIsPublish);

        final Voice result = mList.get(position);
        holder.setText(R.id.m_tv_issue_result_voice, result.getDuration());
        holder.setOnClickListener(R.id.m_iv_issue_voice_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVoice(position);
                RxBus.get().post(BusAction.DELETE_VOICE, result);
            }
        });
        playAudio(holder, mIsPublish, result);

    }

    private void playAudio(final BaseViewHolder holder, final boolean mIsPublish, final Voice result) {
        final MediaPlayer mPlayer = new MediaPlayer();
        RxView.clicks(holder.getView(R.id.m_iv_play_voice)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Glide.with(mContext).load(R.mipmap.group59).into((ImageView) holder.getView(R.id.m_iv_play_voice));
                if (mIsPublish) {
                    try {
                        mPlayer.setDataSource(result.getFile().getPath());
                        mPlayer.prepare();
                        mPlayer.start();
                        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.stop();
                                mp.release();
                                holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
                            }
                        });
                        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {

                                return false;
                            }
                        });

                    } catch (IOException e) {
                        LogUtils.e("播放失败");
                        holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
                    }
                } else {
                    RxUtils.getFile(mContext, result.getUri()).observeOn
                            (AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {

                            if (file == null) return;

                            try {
                                mPlayer.setDataSource(mContext, Uri.parse(file.getAbsolutePath()));
                                mPlayer.prepare();
                                mPlayer.start();
                                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.stop();
                                        mp.release();
                                        holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                                holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
                            }

                        }
                    });
                }
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

}
