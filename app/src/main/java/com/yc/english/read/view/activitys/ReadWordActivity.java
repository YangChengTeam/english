package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.common.SpeechUtil;
import com.yc.english.read.contract.ReadWordContract;
import com.yc.english.read.model.domain.WordDetailInfo;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.presenter.ReadWordPresenter;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;
import com.yc.english.read.view.wdigets.SpaceItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import static com.yc.english.read.view.activitys.CoursePlayActivity.VOICER_NAME;

/**
 * Created by admin on 2017/7/27.
 */

public class ReadWordActivity extends FullScreenActivity<ReadWordPresenter> implements ReadWordContract.View, ReadWordItemClickAdapter.ItemDetailClick {

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

    @BindView(R.id.tv_read_current_num)
    TextView mReadCurrentNum;

    @BindView(R.id.tv_read_total_num)
    TextView mReadTotalNum;

    @BindView(R.id.pb_read_num)
    ProgressBar mProgressReadNum;

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

    private boolean isContinue = false;

    private String unitId;

    private PublishSubject mTsSubject;

    private PublishSubject mSpellSubject;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_word_play;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unitId = bundle.getString("unit_id");
        }

        mPresenter = new ReadWordPresenter(this, this);

        mToolbar.setTitle("Unit 1");
        mToolbar.showNavigationIcon();

        mediaPlayer = new MediaPlayer();

        SpeechUtil.initSpeech(ReadWordActivity.this, 28, 50, 50, 1);
        mTts = SpeechUtil.getmTts();

        mSharedPreferences = getSharedPreferences(VOICER_NAME, MODE_PRIVATE);

        linearLayoutManager = new LinearLayoutManager(this);
        mReadWordRecyclerView.setLayoutManager(linearLayoutManager);
        mReadWordItemClickAdapter = new ReadWordItemClickAdapter(ReadWordActivity.this, null);
        mReadWordItemClickAdapter.setItemDetailClick(this);
        mReadWordRecyclerView.setAdapter(mReadWordItemClickAdapter);
        mReadWordRecyclerView.addItemDecoration(new SpaceItemDecoration(1));

        mReadWordItemClickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (mDatas.get(position) != null && !isPlay) {
                    currentIndex = position;

                    isContinue = false;
                    readCurrentWord = ((WordInfo) mDatas.get(position)).getName();

                    if (isSpell) {
                        readCurrentWordIndex = 0;
                        startSynthesizer(position);
                    } else {
                        startSynthesizer(position);
                    }

                    Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_gif_play).into((ImageView) view.findViewById(R.id.iv_read_word));
                }

                return false;
            }
        });

        mTsSubject = PublishSubject.create();
        mTsSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                if (isContinue) {
                    if (position < mDatas.size()) {
                        startSynthesizer(position);
                    } else {
                        setStopPlayState();
                    }
                }
            }
        });

        mSpellSubject = PublishSubject.create();
        mSpellSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                if (readCurrentWordIndex < readCurrentWord.length()) {
                    playWord();
                } else {
                    if (isContinue) {
                        readCurrentWordIndex = 0;
                        currentIndex++;
                        if (currentIndex < mDatas.size()) {
                            if (mDatas.get(currentIndex) instanceof WordInfo) {
                                readCurrentWord = ((WordInfo) mDatas.get(currentIndex)).getName();
                                startSynthesizer(currentIndex);
                            }
                        } else {
                            isPlay = false;
                            mReadWordRecyclerView.scrollToPosition(0);
                            setStopPlayState();
                        }
                    } else {
                        // TODO
                        //当前阅读的行，设置未未读
                    }
                }
            }
        });

        mPresenter.getWordListByUnitId(0, 0, unitId);
    }

    @Override
    public void showWordListData(List<WordInfo> list) {
        if (list != null) {
            //mDatas = list;

            if (mDatas == null) {
                mDatas = new ArrayList<MultiItemEntity>();
            } else {
                mDatas.clear();
            }

            setProgressNum(0, mDatas.size());
            //TODO 数据有问题，待定
            for (int i = 0; i < list.size(); i++) {
                WordInfo wordInfo = (WordInfo) list.get(i);
                wordInfo.addSubItem(new WordDetailInfo(wordInfo.getName(), wordInfo.getMeans()));
                mDatas.add(wordInfo);
            }
            mReadWordItemClickAdapter.setNewData(mDatas);
        }
    }

    public void setProgressNum(int current, int total) {
        mReadCurrentNum.setText(current + "");
        mReadTotalNum.setText(total + "");
        mProgressReadNum.setMax(total);
        mProgressReadNum.setProgress(current);
    }

    /**
     * 拼读单个单词
     */
    public void playWord() {
        try {
            if (readCurrentWordIndex < readCurrentWord.length()) {
                mediaPlayer.reset();
                LogUtils.e("index--->" + ("" + readCurrentWord.charAt(readCurrentWordIndex)).toLowerCase());
                AssetFileDescriptor fd = getAssets().openFd(String.valueOf(readCurrentWord.charAt(readCurrentWordIndex)).toLowerCase() + ".mp3");
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        readCurrentWordIndex++;
                        mSpellSubject.onNext(readCurrentWordIndex);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSynthesizer(int position) {
        String text = ((WordInfo) mReadWordItemClickAdapter.getData().get(position)).getName();
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

                if (isSpell) {
                    if (currentIndex < mDatas.size()) {
                        if (currentIndex > 4) {
                            mReadWordRecyclerView.scrollToPosition(currentIndex + 1);
                        }
                        readCurrentWord = ((WordInfo) mDatas.get(currentIndex)).getName();
                        playWord();
                    } else {
                        Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView);
                    }
                } else {

                    if (isContinue) {
                        setProgressNum(currentIndex + 1, mDatas.size());

                        currentIndex++;
                        if (currentIndex < mDatas.size()) {
                            if (currentIndex > 4 && currentIndex < mDatas.size() - 1) {
                                mReadWordRecyclerView.scrollToPosition(currentIndex + 1);
                            }

                            try {
                                Thread.sleep(300);
                                mTsSubject.onNext(currentIndex);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } else {
                            setStopPlayState();
                        }
                    } else {
                        View view = linearLayoutManager.findViewByPosition(currentIndex);
                        if (view != null) {
                            Glide.with(ReadWordActivity.this).load(R.mipmap.read_word_audio).into((ImageView) view.findViewById(R.id.iv_read_word));
                        }
                    }

                }

            } else if (error != null) {
                ToastUtils.showLong(error.getPlainDescription(true));
                setStopPlayState();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    };

    /**
     * 设置停止播放状态
     */
    public void setStopPlayState() {
        mTts.stopSpeaking();
        Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView);
    }

    /**
     * 单词闯关
     */
    @OnClick(R.id.layout_pass_word)
    public void wordPractice() {
        Intent intent = new Intent(ReadWordActivity.this, WordPracticeActivity.class);
        intent.putExtra("unit_id", unitId);
        startActivity(intent);
    }

    /**
     * 全部朗读
     */
    @OnClick(R.id.layout_read_all)
    public void readAll() {
        isPlay = !isPlay;

        if (isPlay) {
            isContinue = true;
            currentIndex = 0;
            readCurrentWordIndex = 0;
            Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_gif_play).into(mReadAllImageView);
            if (currentIndex < mDatas.size()) {
                readCurrentWord = ((WordInfo) mDatas.get(currentIndex)).getName();
                startSynthesizer(currentIndex);
                lastView = linearLayoutManager.findViewByPosition(currentIndex);
            }
        } else {
            isContinue = false;
            setStopPlayState();
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
        startSynthesizer(currentIndex);
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
