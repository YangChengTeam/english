package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.read.common.SpeechUtil;
import com.yc.english.read.contract.ReadWordContract;
import com.yc.english.read.model.domain.WordDetailInfo;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.presenter.ReadWordPresenter;
import com.yc.english.read.view.adapter.ReadWordExpandAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by admin on 2017/7/27.
 */

public class ReadWordActivity extends FullScreenActivity<ReadWordPresenter> implements ReadWordContract.View, ReadWordExpandAdapter.ItemViewClickListener {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.layout_content)
    RelativeLayout mLayoutContext;

    @BindView(R.id.rv_word_list)
    ExpandableListView mWordListView;

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

    //ReadWordItemClickAdapter mReadWordItemClickAdapter;

    ReadWordExpandAdapter mReadWordExpandAdapter;

    List<WordInfo> mDatas;

    boolean isSpell = false;

    // 语音合成对象
    private SpeechSynthesizer mTts;

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

    private boolean isPlay = false;

    private boolean isContinue = false;

    private String unitId;

    private String unitTitle;

    private PublishSubject mTsSubject;

    private PublishSubject mSpellSubject;

    private boolean isWordDetailPlay = false;

    private int lastExpandPosition = -1;

    private List<WordInfo> wordInfos;

    private List<WordDetailInfo> wordDetailInfos;

    private View lastChildItemView;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_word_play;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unitId = bundle.getString("unit_id");
            unitTitle = bundle.getString("unit_title");
        }

        mPresenter = new ReadWordPresenter(this, this);
        mToolbar.setTitle(unitTitle);
        mToolbar.showNavigationIcon();

        mediaPlayer = new MediaPlayer();

        SpeechUtil.initSpeech(ReadWordActivity.this, 28, 50, 50, 1);
        mTts = SpeechUtil.getmTts();

        mReadWordExpandAdapter = new ReadWordExpandAdapter(ReadWordActivity.this, wordInfos, wordDetailInfos);

        mWordListView.setAdapter(mReadWordExpandAdapter);
        mWordListView.setGroupIndicator(null);
        mReadWordExpandAdapter.setItemDetailClick(this);
        mWordListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (groupPosition != lastExpandPosition && lastExpandPosition > -1) {
                    if(mTts.isSpeaking()){
                        mTts.stopSpeaking();
                    }
                    mWordListView.collapseGroup(lastExpandPosition);
                    if (lastChildItemView != null) {
                        mReadWordExpandAdapter.setChildViewPlayState(lastChildItemView, false);
                    }
                }

                if (mWordListView.isGroupExpanded(groupPosition)) {
                    mWordListView.collapseGroup(groupPosition);
                } else {
                    mWordListView.expandGroup(groupPosition);
                    lastExpandPosition = groupPosition;
                }
                isPlay = false;
                return true;
            }
        });

        mWordListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                if (mTts.isSpeaking()) {
                    mTts.stopSpeaking();
                }
                lastChildItemView = view;

                isWordDetailPlay = true;
                startSynthesizer(wordDetailInfos.get(groupPosition).getWordExample());
                mReadWordExpandAdapter.setChildViewPlayState(view, true);

                return true;
            }
        });

        mTsSubject = PublishSubject.create();
        mTsSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                if (isContinue) {
                    if (position < mDatas.size()) {
                        mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, true);
                        startSynthesizer(position);
                    } else {
                        isPlay = false;
                        mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
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
                    mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);

                    if (isContinue) {
                        readCurrentWordIndex = 0;
                        currentIndex++;
                        if (currentIndex < mDatas.size()) {
                            try {
                                Thread.sleep(300);
                                mTsSubject.onNext(currentIndex);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            isPlay = false;
                            mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
                            setStopPlayState();
                        }
                    }
                }
            }
        });

        mPresenter.getWordListByUnitId(0, 0, unitId);
    }


    @Override
    public void hideStateView() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWordListByUnitId(0, 0, unitId);
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(mLayoutContext);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(mLayoutContext);
    }

    @Override
    public void showWordListData(List<WordInfo> list) {
        if (list != null && list.size() > 0) {

            if (list != null) {
                mDatas = list;
                wordInfos = new ArrayList<WordInfo>();
                wordDetailInfos = new ArrayList<WordDetailInfo>();
                setProgressNum(0, list.size());
            }

            for (int i = 0; i < list.size(); i++) {
                WordInfo wordInfo = (WordInfo) list.get(i);
                WordDetailInfo wordDetailInfo = new WordDetailInfo();
                wordDetailInfo.setWordExample(wordInfo.getEpSentence());
                wordDetailInfo.setWordCnExample(wordInfo.getEpSentenceMeans());
                wordInfos.add(wordInfo);
                wordDetailInfos.add(wordDetailInfo);
            }

            mReadWordExpandAdapter.setNewDatas(wordInfos, wordDetailInfos);
            mReadWordExpandAdapter.notifyDataSetChanged();
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

    //单词朗读
    public void startSynthesizer(int position) {
        String text = ((WordInfo) wordInfos.get(position)).getName();
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

    //示例句子朗读
    public void startSynthesizer(String sentence) {
        String text = sentence;
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

                //单词例句阅读
                if (isWordDetailPlay) {
                    if (lastChildItemView != null) {
                        mReadWordExpandAdapter.setChildViewPlayState(lastChildItemView, false);
                    }
                    mTts.stopSpeaking();
                    isWordDetailPlay = false;
                    return;
                }

                if (isSpell) {
                    if (currentIndex < mDatas.size()) {
                        if (currentIndex > 4) {
                            //mWordListView.scrollListBy(2);
                        }
                        readCurrentWord = ((WordInfo) mDatas.get(currentIndex)).getName();
                        setProgressNum(currentIndex + 1, mDatas.size());
                        playWord();
                    } else {
                        isPlay = false;
                        mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
                        setStopPlayState();
                    }
                } else {
                    mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
                    if (isContinue) {
                        setProgressNum(currentIndex + 1, mDatas.size());
                        currentIndex++;

                        if (currentIndex < mDatas.size()) {
                            if (currentIndex > 4 && currentIndex < mDatas.size() - 1) {
                                //mWordListView.scrollToPosition(currentIndex + 1);
                            }
                            try {
                                Thread.sleep(300);
                                mTsSubject.onNext(currentIndex);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            isPlay = false;
                            setStopPlayState();
                            mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
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
            //currentIndex = 0;
            readCurrentWordIndex = 0;
            Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_gif_play).into(mReadAllImageView);
            if (currentIndex < mDatas.size()) {
                readCurrentWord = ((WordInfo) mDatas.get(currentIndex)).getName();
                mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, true);
                startSynthesizer(currentIndex);
            }
        } else {
            isContinue = false;
            mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
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
    public void groupWordClick(int gPosition) {
        mTts.stopSpeaking();
        isContinue = false;

        if (currentIndex != gPosition) {
            mReadWordExpandAdapter.setViewPlayState(currentIndex, mWordListView, false);
        }

        if (wordInfos.get(gPosition) != null && !isPlay) {
            currentIndex = gPosition;

            setProgressNum(currentIndex + 1, mDatas.size());

            isContinue = false;
            readCurrentWord = ((WordInfo) wordInfos.get(gPosition)).getName();

            if (isSpell) {
                readCurrentWordIndex = 0;
                startSynthesizer(gPosition);
            } else {
                startSynthesizer(gPosition);
            }

            mReadWordExpandAdapter.setViewPlayState(gPosition, mWordListView, true);
        }
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
