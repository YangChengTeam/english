package com.yc.english.read.view.activitys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.EnglishCourseInfo;
import com.yc.english.read.test.JsonTools;
import com.yc.english.read.view.adapter.ReadCourseItemClickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CoursePlayActivity extends FullScreenActivity {

    public static String VOICER_NAME = "com.yc.english.setting";

    @BindView(R.id.layout_course_play)
    LinearLayout mCoursePlayLayout;

    @BindView(R.id.rv_course_list)
    RecyclerView mCourseRecyclerView;

    @BindView(R.id.iv_course_play)
    ImageView mCoursePlayImageView;

    @BindView(R.id.layout_language_change)
    LinearLayout mLanguageChangeLayout;

    @BindView(R.id.tv_language)
    TextView mLanguageTextView;

    ReadCourseItemClickAdapter mItemAdapter;

    List<EnglishCourseInfo> datas;

    private int playPosition;

    private boolean isPlay = false;

    private View lastView;

    LinearLayoutManager linearLayoutManager;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认云端发音人
    private String voicer = "catherine";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private SharedPreferences mSharedPreferences;

    // 缓冲进度
    private int mPercentForBuffering = 0;

    // 播放进度
    private int mPercentForPlaying = 0;

    private boolean isCountinue = false;

    private int languageType = 1; //1:中英,2:英,3:中

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    startPlaySynthesizer();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_course_play;
    }

    @Override
    public void init() {
        mToolbar.setTitle("Unit 1 Hello");
        mToolbar.showNavigationIcon();
        mToolbar.setTitleColor(ContextCompat.getColor(this, R.color.black_333));
        initData();
    }

    public void initData() {

        mTts = SpeechSynthesizer.createSynthesizer(CoursePlayActivity.this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(VOICER_NAME, MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(this);
        mCourseRecyclerView.setLayoutManager(linearLayoutManager);
        datas = JsonTools.jsonData(CoursePlayActivity.this, "english_course.json");
        mItemAdapter = new ReadCourseItemClickAdapter(this, datas);
        mCourseRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, final View view, int position) {

                //将正在播放的停止
                if(isPlay){
                    isPlay = false;
                    mTts.stopSpeaking();
                }

                showCurrentItemView(view);

                if (position != playPosition && lastView != null) {
                    hideCurrentItemView(lastView);
                }

                playPosition = position;
                lastView = view;

                mCoursePlayImageView.setBackgroundResource(R.drawable.read_playing_course_btn_selector);

                if (!isPlay) {
                    if (datas != null) {
                        //标记为单次播放
                        isCountinue = false;
                        startSynthesizer(datas.get(position).getSubtitle());
                    }
                }
            }
        });
    }

    /**
     * 语音播放参数设置
     *
     * @param
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            // 设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "28");
            // 设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            // 设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            //TODO
            //暂时只提供在线语音合成
        }
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "1"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    public void startSynthesizer(String text) {
        setParam(); //设置参数

        int code = mTts.startSpeaking(text, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
                //mInstaller.install();
            } else {
                ToastUtils.showLong("语音合成失败,错误码: " + code);
                mTts.stopSpeaking();
            }
        }
    }

    @OnClick(R.id.layout_course_play)
    public void coursePlayClick() {
        LogUtils.e("coursePlay--->");
        if (isPlay) {
            mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
            isPlay = false;

            hideCurrentItemView(lastView);//停止item播放
            mTts.stopSpeaking();
        } else {
            mCoursePlayImageView.setBackgroundResource(R.drawable.read_playing_course_btn_selector);
            isPlay = true;
        }
        //标记未连续播放
        isCountinue = true;
        startPlaySynthesizer();
    }


    @OnClick(R.id.layout_language_change)
    public void languageChange() {

        languageType++;

        if (languageType > 3) {
            languageType = 1;
        }

        switch (languageType) {
            case 1:
                mLanguageTextView.setText(getString(R.string.read_course_language_blend_text));
                break;
            case 2:
                mLanguageTextView.setText(getString(R.string.read_course_language_en_text));
                break;
            case 3:
                mLanguageTextView.setText(getString(R.string.read_course_language_cn_text));
                break;
            default:
                break;
        }

        mItemAdapter.setLanguageType(languageType);
        mItemAdapter.notifyDataSetChanged();

    }

    /**
     * 开始播放语音
     */
    public void startPlaySynthesizer() {
        try {
            View view = linearLayoutManager.findViewByPosition(playPosition);
            if (isPlay) {
                if (lastView != null && lastView != view) {
                    hideCurrentItemView(lastView);
                }

                lastView = view;

                showCurrentItemView(view);

                if (datas != null && playPosition < datas.size()) {
                    if (datas.get(playPosition) != null) {
                        startSynthesizer(datas.get(playPosition).getSubtitle());
                    }
                }

            } else {
                hideCurrentItemView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 标记当前播放的item
     *
     * @param view
     */
    public void showCurrentItemView(View view) {
        ImageView mReadPlayIv = (ImageView) view.findViewById(R.id.iv_audio_gif_play);
        TextView mChineseTv = (TextView) view.findViewById(R.id.tv_chinese_title);
        TextView mEnglishTv = (TextView) view.findViewById(R.id.tv_english_title);

        mReadPlayIv.setVisibility(View.VISIBLE);
        Glide.with(CoursePlayActivity.this).load(R.mipmap.read_audio_gif_play).into(mReadPlayIv);
        mChineseTv.setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.black_333));
        mEnglishTv.setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.black_333));
    }

    /**
     * 取消标记当前播放的item
     *
     * @param view
     */
    public void hideCurrentItemView(View view) {
        ((ImageView) view.findViewById(R.id.iv_audio_gif_play)).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.tv_chinese_title)).setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.gray_999));
        ((TextView) view.findViewById(R.id.tv_english_title)).setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.gray_999));
    }

    /**
     * 初始化监听
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            //Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.showLong("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //开始播放
        }

        @Override
        public void onSpeakPaused() {
            //暂停播放
        }

        @Override
        public void onSpeakResumed() {
            //继续播放
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            mPercentForBuffering = percent;
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {

                if (!isCountinue) {
                    //播放完成
                    mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
                    isPlay = false;
                    hideCurrentItemView(linearLayoutManager.findViewByPosition(playPosition));
                } else {
                    isPlay = true;

                    playPosition++;

                    if (playPosition > 2) {
                        mCourseRecyclerView.scrollToPosition(playPosition + 2);
                    }

                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message message = new Message();
                    message.what = 1;
                    if (isCountinue) {
                        message.what = 1;
                    } else {
                        message.what = 2;
                    }
                    message.obj = playPosition;
                    handler.sendMessage(message);
                }

            } else if (error != null) {
                mTts.stopSpeaking();
                ToastUtils.showLong(error.getPlainDescription(true));
                isPlay = false;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
}
