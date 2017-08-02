package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.WordDetailInfo;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;
import com.yc.english.read.view.wdigets.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yc.english.read.view.activitys.CoursePlayActivity.VOICER_NAME;

/**
 * Created by admin on 2017/7/27.
 */

public class ReadWordActivity extends FullScreenActivity implements ReadWordItemClickAdapter.ItemDetailClick {

    @BindView(R.id.rv_word_list)
    RecyclerView mReadWordRecyclerView;

    @BindView(R.id.layout_pass_word)
    LinearLayout mPassWordLayout;

    @BindView(R.id.layout_spell_word)
    RelativeLayout mSpellWordLayout;

    @BindView(R.id.iv_read_all)
    ImageView mReadAllImageView;

    @BindView(R.id.iv_spell_icon)
    ImageView mSpellWordImageView;

    ReadWordItemClickAdapter mReadWordItemClickAdapter;

    List<MultiItemEntity> mDatas;

    boolean isSpell = false;

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

    private int readTotalCount;

    private int readCurrentWordIndex;

    private String readCurrentWord;

    MediaPlayer mediaPlayer;

    //当前读到的单词
    private int currentIndex;

    LinearLayoutManager linearLayoutManager;

    private View lastView;

    private boolean isPlay = false;

    private boolean isCountinue = false;

    private String[] words = new String[]{"Book","Apple","Study","Book","Apple","Study","Book","Apple","Study"};

    private String[] wordChineses = new String[]{"书、书本","苹果","学习","书、书本","苹果","学习","书、书本","苹果","学习"};

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
               case 1:
                    if (currentIndex < mDatas.size()) {
                        currentIndex ++;

                        View view = linearLayoutManager.findViewByPosition(currentIndex);
                        if(lastView != view){
                            ((TextView) lastView.findViewById(R.id.tv_word_number)).setTextColor(ContextCompat.getColor(ReadWordActivity.this, R.color.gray_aaa));
                            lastView = view;
                        }

                        TextView mNumberTv = (TextView) view.findViewById(R.id.tv_word_number);
                        mNumberTv.setTextColor(ContextCompat.getColor(ReadWordActivity.this, R.color.read_word_share_btn_color));
                        startSynthesizer(((WordInfo) mDatas.get(currentIndex)).getWord());
                    } else {
                        mTts.stopSpeaking();
                    }
                    break;
                case 3:

                    if(readCurrentWordIndex < readCurrentWord.length()){
                        playWord();
                    }else{
                        readCurrentWordIndex = 0;
                        currentIndex ++;
                        readCurrentWord = ((WordInfo) mDatas.get(currentIndex)).getWord();
                        startSynthesizer(readCurrentWord);
                    }

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_word_play;
    }

    @Override
    public void init() {
        mToolbar.setTitle("Unit 1");
        mToolbar.showNavigationIcon();
        mDatas = new ArrayList<MultiItemEntity>();
        mediaPlayer = new MediaPlayer();
        mTts = SpeechSynthesizer.createSynthesizer(ReadWordActivity.this, mTtsInitListener);
        mSharedPreferences = getSharedPreferences(VOICER_NAME, MODE_PRIVATE);

        for (int i = 0; i < 9; i++) {
            WordInfo wordInfo = new WordInfo(words[i], wordChineses[i]);
            WordDetailInfo wordDetailInfo = new WordDetailInfo("I just took her book home and let him play", "我只是把她的书带回家，让他去玩了");
            wordInfo.addSubItem(wordDetailInfo);
            mDatas.add(wordInfo);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        mReadWordRecyclerView.setLayoutManager(linearLayoutManager);
        mReadWordItemClickAdapter = new ReadWordItemClickAdapter(ReadWordActivity.this, mDatas);
        mReadWordItemClickAdapter.setItemDetailClick(this);
        mReadWordRecyclerView.setAdapter(mReadWordItemClickAdapter);
        mReadWordRecyclerView.addItemDecoration(new SpaceItemDecoration(1));

        mReadWordItemClickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (mDatas.get(position) != null) {
                    isCountinue = false;
                    readCurrentWord = ((WordInfo) mDatas.get(position)).getWord();

                    if (isSpell) {
                        /*readCurrentIndex = 0;
                        readTotalCount = readCurrentWord.length();
                        startSynthesizer(readCurrentWord.charAt(readCurrentIndex) + "");*/
                        readCurrentWordIndex = 0;
                        startSynthesizer(((WordInfo) mDatas.get(position)).getWord());
                    } else {
                        startSynthesizer(((WordInfo) mDatas.get(position)).getWord());
                    }

                }

                return false;
            }
        });
    }

    public void playWord() {
        try {

            if (readCurrentWordIndex < readCurrentWord.length()) {
                mediaPlayer.reset();

                LogUtils.e("index--->"+("" + readCurrentWord.charAt(readCurrentWordIndex)).toLowerCase());

                AssetFileDescriptor fd = getAssets().openFd(String.valueOf(readCurrentWord.charAt(readCurrentWordIndex)).toLowerCase() + ".mp3");
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        readCurrentWordIndex++;
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            mTts.setParameter(SpeechConstant.SPEED, "60");
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
                //ToastUtils.showLong("播放完成");

                if (isSpell) {
                   /* Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);*/
                    playWord();
                }else{

                    if(isCountinue){
                        if (currentIndex < mDatas.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }

                }

            } else if (error != null) {
                mTts.stopSpeaking();
                ToastUtils.showLong(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    };

    /**
     * 单词闯关
     */
    @OnClick(R.id.layout_pass_word)
    public void wordPractice() {
        Intent intent = new Intent(ReadWordActivity.this, WordPracticeActivity.class);
        startActivity(intent);
    }

    /**
     * 全部朗读
     */
    @OnClick(R.id.layout_read_all)
    public void readAll() {

        isPlay = !isPlay;

        if(isPlay){
            isCountinue = true;
            Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_gif_play).into(mReadAllImageView);
            if(currentIndex < mDatas.size()){
                readCurrentWord =((WordInfo) mDatas.get(currentIndex)).getWord();
                startSynthesizer(readCurrentWord);
                lastView = linearLayoutManager.findViewByPosition(currentIndex);

            }
        }else{
            isCountinue = false;
            Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView);
            mTts.stopSpeaking();
            ((TextView) lastView.findViewById(R.id.tv_word_number)).setTextColor(ContextCompat.getColor(ReadWordActivity.this, R.color.gray_aaa));
        }

    }

    /**
     * 拼写朗读
     */
    @OnClick(R.id.layout_spell_word)
    public void spellWord() {
        isSpell = !isSpell;

        if (isSpell) {
            mSpellWordImageView.setVisibility(View.VISIBLE);
        } else {
            mSpellWordImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void detailClick(int position) {

        MultiItemEntity multiItemEntity = ((MultiItemEntity) mDatas.get(position));

        WordDetailInfo wordDetailInfo = ((WordDetailInfo) multiItemEntity);
        startSynthesizer(wordDetailInfo.getWordExample());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
}
