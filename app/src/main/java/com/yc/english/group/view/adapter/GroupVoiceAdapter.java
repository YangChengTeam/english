package com.yc.english.group.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.Voice;

import java.io.IOException;
import java.util.List;

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

        holder.setOnClickListener(R.id.m_iv_play_voice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(mContext).load(R.mipmap.group59).into((ImageView) holder.getView(R.id.m_iv_play_voice));
                MediaPlayer mPlayer = new MediaPlayer();
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

                } catch (IOException e) {
                    LogUtils.e("播放失败");
                    holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
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
