package com.yc.english.group.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.Voice;

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
                Uri uri = Uri.parse(result.getFile().getPath());
                LogUtils.e("listener  " + result.getFile().getPath() + "----" + uri);
//                final MediaPlayer mPlayer = new MediaPlayer();
//
//
//                try {
//                    Uri uri = Uri.parse(result.getFile().getPath());
//
//                    AudioPlayManager.getInstance().startPlay(mContext, uri, new IAudioPlayListener() {
//                        @Override
//                        public void onStart(Uri uri) {
//                            Glide.with(mContext).load(R.mipmap.group59).into((ImageView) holder.getView(R.id.m_iv_play_voice));
//                        }
//
//                        @Override
//                        public void onStop(Uri uri) {
//
//                        }
//
//                        @Override
//                        public void onComplete(Uri uri) {
//                            holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
//                        }
//                    });
//                } catch (Exception e) {
//                    LogUtils.e("播放失败" + e.getMessage());
//                    holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
//                }
//                try {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mPlayer.reset();
//                            try {
//                                mPlayer.setDataSource(result.getFile().getPath());
//                                mPlayer.prepare();// prepare之后自动播放
//                                mPlayer.start();
//                                try {
//                                    Thread.sleep(200);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            } catch (IOException e) {
//
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }).start();
//
//
//                    mPlayer.setDataSource(mContext, uri);
//                    LogUtils.e("listener  " + result.getFile().getPath() + "----" + uri);
////                    File file = new File(result.getFile().getPath());
////                    FileInputStream fis = new FileInputStream(file);
////                    mPlayer.setDataSource(fis.getFD());
//
//                    mPlayer.prepare();
//                    mPlayer.start();
//
////                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////                        @Override
////                        public void onPrepared(MediaPlayer mp) {
////                            mPlayer.start();
////
////                        }
////                    });
////                    mPlayer.prepareAsync();
//                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//
//                            mp.stop();
//                            mp.reset();
//                            mp.release();
//                            mp = null;
//
//                        }
//                    });
//
//                } catch (IOException e) {
//                    LogUtils.e("播放失败" + e.getMessage());
//                    holder.setImageDrawable(R.id.m_iv_play_voice, mContext.getResources().getDrawable(R.mipmap.group67));
//                }
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
