package com.yc.junior.english.read.view.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.utils.DrawableUtils;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.community.view.wdigets.CountDown;
import com.yc.junior.english.community.view.wdigets.GridSpacingItemDecoration;
import com.yc.junior.english.read.common.AudioPlayManager;
import com.yc.junior.english.read.common.MediaPlayerPlayer;
import com.yc.junior.english.read.common.OnUiUpdateManager;
import com.yc.junior.english.read.common.SpeechUtils;
import com.yc.junior.english.read.contract.ReadWordContract;
import com.yc.junior.english.read.model.domain.LetterInfo;
import com.yc.junior.english.read.model.domain.WordInfo;
import com.yc.junior.english.read.presenter.ReadWordPresenter;
import com.yc.junior.english.read.view.adapter.ReadLetterItemClickAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.ToastUtils;



/**
 * Created by admin on 2017/7/26.
 */

public class WordPracticeActivity extends FullScreenActivity<ReadWordPresenter> implements ReadWordContract.View, OnUiUpdateManager {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.layout_content)
    RelativeLayout mLayoutContext;

    @BindView(R.id.rv_letter_list)
    RecyclerView mLetterRecyclerView;

    @BindView(R.id.et_word_input)
    TextView mWordInputTextView;

    @BindView(R.id.iv_word_delete)
    ImageView mWordDeleteImageView;

    @BindView(R.id.iv_audio_play)
    ImageView mAudioPlayImageView;

    @BindView(R.id.btn_right_next_word)
    Button mRightNextButton;

    @BindView(R.id.btn_error_next_word)
    Button mErrorNextButton;

    @BindView(R.id.btn_error_again_word)
    Button mErrorAgainButton;

    @BindView(R.id.tv_cn_word)
    TextView mChineseWordTextView;

    @BindView(R.id.tv_right_remind_word)
    TextView mRightRemindTextView;

    @BindView(R.id.tv_word_count_down)
    TextView mWordCountDownTextView;

    @BindView(R.id.btn_check_word)
    Button mCheckWordButton;

    @BindView(R.id.layout_letter_list)
    RelativeLayout mLetterListLayout;

    @BindView(R.id.layout_right)
    RelativeLayout mRightLayout;

    @BindView(R.id.layout_error)
    RelativeLayout mErrorLayout;

    @BindView(R.id.tv_word_error_tip)
    TextView mWordErrorTipTextView;


    private ReadLetterItemClickAdapter mLetterAdapter;

    private List<WordInfo> mWordInfoDatas;

    private List<LetterInfo> mLetterDatas;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 缓冲进度
    private int mPercentForBuffering = 0;

    // 播放进度
    private int mPercentForPlaying = 0;

    private String unitId;

    private CountDown countDown;

    private int currentWordIndex;

    private String currentRightWord;

    private String currentRightCnWord;

    private AudioPlayManager manager;


    @Override
    public int getLayoutId() {
        return R.layout.read_activity_word_practice;
    }

    @Override
    public void init() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unitId = bundle.getString("unit_id");
        }

        mPresenter = new ReadWordPresenter(this, this);

        mToolbar.showNavigationIcon();

        mTts = SpeechUtils.getTts(this);
//
//        initMediaPlayer();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        mLetterRecyclerView.setLayoutManager(layoutManager);
        mLetterRecyclerView.addItemDecoration(new GridSpacingItemDecoration(5, getResources().getDimensionPixelSize(R.dimen.dp_6), true));
        mLetterRecyclerView.setHasFixedSize(true);

        mLetterRecyclerView.setLayoutManager(layoutManager);
        mLetterAdapter = new ReadLetterItemClickAdapter(this, null);
        mLetterRecyclerView.setAdapter(mLetterAdapter);
        mLetterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mWordInputTextView.setText(mWordInputTextView.getText() + mLetterDatas.get(position).getLetterName());
                if (!StringUtils.isEmpty(mWordInputTextView.getText())) {
                    mWordDeleteImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        countDown = new CountDown(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mWordCountDownTextView.setText(toClock(millisUntilFinished));
            }

            @Override
            public String toClock(long millis) {
                return super.toClock(millis);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (currentRightWord.equals(mWordInputTextView.getText())) {
                    mRightLayout.setVisibility(View.VISIBLE);
                    mErrorLayout.setVisibility(View.INVISIBLE);
                    mLetterListLayout.setVisibility(View.GONE);
                    mWordInputTextView.setTextColor(ContextCompat.getColor(WordPracticeActivity.this, R.color.right_word_color));
                } else {
                    TipsHelper.tips(WordPracticeActivity.this, "你超时啦");
                    mWordErrorTipTextView.setText(getString(R.string.word_time_out_error_text));
                    mRightLayout.setVisibility(View.INVISIBLE);
                    mErrorLayout.setVisibility(View.VISIBLE);
                    mLetterListLayout.setVisibility(View.GONE);
                    mRightRemindTextView.setText(currentRightWord);
                    mWordInputTextView.setTextColor(ContextCompat.getColor(WordPracticeActivity.this, R.color.read_word_share_btn_color));
                    mErrorAgainButton.setBackgroundDrawable(DrawableUtils.getBgColor(WordPracticeActivity.this, 3, R.color.right_word_btn_again_color));
                }
            }
        };

        //播放
        RxView.clicks(mAudioPlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startSynthesizer(currentRightWord);
            }
        });

        //检查
        RxView.clicks(mCheckWordButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (StringUtils.isEmpty(mWordInputTextView.getText())) {
                    ToastUtils.showLong("请输入单词");
                    return;
                }

                if (currentRightWord.replaceAll(" ", "").equals(mWordInputTextView.getText().toString().replaceAll(" ", ""))) {
                    mRightLayout.setVisibility(View.VISIBLE);
                    mErrorLayout.setVisibility(View.INVISIBLE);
                    mLetterListLayout.setVisibility(View.GONE);
                    mWordInputTextView.setTextColor(ContextCompat.getColor(WordPracticeActivity.this, R.color.right_word_color));
                } else {
                    mRightLayout.setVisibility(View.INVISIBLE);
                    mErrorLayout.setVisibility(View.VISIBLE);
                    mLetterListLayout.setVisibility(View.GONE);
                    mRightRemindTextView.setText(currentRightWord);
                    mWordErrorTipTextView.setText(getString(R.string.read_word_again_text));
                    mWordInputTextView.setTextColor(ContextCompat.getColor(WordPracticeActivity.this, R.color.read_word_share_btn_color));
                    mErrorAgainButton.setBackgroundDrawable(DrawableUtils.getBgColor(WordPracticeActivity.this, 3, R.color.right_word_btn_again_color));
                }
                //最后一个
                if (currentWordIndex == mWordInfoDatas.size() - 1) {
                    mErrorNextButton.setText(getString(R.string.read_word_finish_text));
                    mRightNextButton.setText(getString(R.string.read_word_finish_text));
                }
            }
        });

        //再做一遍
        RxView.clicks(mErrorAgainButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (currentWordIndex < mWordInfoDatas.size()) {
                    nextWord(currentWordIndex);
                } else {
                    finish();
                }
            }
        });

        //正确页面下一题
        RxView.clicks(mRightNextButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentWordIndex++;
                if (currentWordIndex < mWordInfoDatas.size()) {
                    nextWord(currentWordIndex);
                } else {
                    finish();
                }
            }
        });

        //错误页面下一题
        RxView.clicks(mErrorNextButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentWordIndex++;
                if (currentWordIndex < mWordInfoDatas.size()) {
                    nextWord(currentWordIndex);
                } else {
                    finish();
                }
            }
        });

        mPresenter.getWordListByUnitId(0, 0, unitId);
    }

    private void initMediaPlayer() {
//        manager = new WlMusicPlayer(this);
        manager = new MediaPlayerPlayer(this);
    }

    @Override
    public void hide() {
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

    public void nextWord(int wordPosition) {
        mLetterListLayout.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.INVISIBLE);
        mWordInputTextView.setText("");
        mWordInputTextView.setTextColor(ContextCompat.getColor(this, R.color.black_333));

        currentRightWord = mWordInfoDatas.get(currentWordIndex).getName();
        currentRightCnWord = mWordInfoDatas.get(currentWordIndex).getMeans();

        mToolbar.setTitle(currentWordIndex + 1 + "/" + mWordInfoDatas.size());
        mChineseWordTextView.setText(currentRightCnWord);
        randomLetterView();
        countDown.start();
    }

    public void startSynthesizer(String text) {

        Glide.with(SpeechUtils.mContext).load(R.mipmap.read_audio_gif_play).into(mAudioPlayImageView);
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
                //播放完成
                Glide.with(SpeechUtils.mContext).load(R.mipmap.read_word_default).into(mAudioPlayImageView);
            } else if (error != null) {
                mTts.stopSpeaking();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    };

    @OnClick(R.id.iv_word_delete)
    public void deleteWordInput() {
        mWordInputTextView.setText("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
            mTts = null;
        }
    }

    @Override
    public void showWordListData(List<WordInfo> list) {

        if (list != null && list.size() > 0) {
            mWordInfoDatas = list;

            mToolbar.setTitle(currentWordIndex + 1 + "/" + list.size());

            currentRightWord = list.get(currentWordIndex).getName();
            currentRightCnWord = list.get(currentWordIndex).getMeans();
            mChineseWordTextView.setText(currentRightCnWord);

            randomLetterView();
        }
    }

    public void randomLetterView() {
        if (!StringUtils.isEmpty(currentRightWord)) {
            String randomStr = randomLetters(currentRightWord);
            mLetterDatas = new ArrayList<>();

            for (int i = 0; i < randomStr.length(); i++) {
                LetterInfo letterInfo = new LetterInfo(LetterInfo.CLICK_ITEM_VIEW);
                letterInfo.setLetterName(randomStr.charAt(i) + "");
                mLetterDatas.add(letterInfo);
            }

            mLetterAdapter.setNewData(mLetterDatas);

            countDown.start();

            startSynthesizer(currentRightWord);
        } else {
            TipsHelper.tips(WordPracticeActivity.this, "数据异常，请稍后重试");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
    }


    /**
     * 根据指定单词随机产生字母字符串
     *
     * @param oldStr
     * @return
     */
    public String randomLetters(String oldStr) {

        String letterStr = "abcdefghijklmnopqrstuvwxyz";

        TreeSet<String> ts = new TreeSet<>();
        int len = oldStr.length();
        for (int i = 0; i < len; i++) {
            ts.add(oldStr.charAt(i) + "");
        }

        Iterator<String> i = ts.iterator();
        StringBuilder sb = new StringBuilder();
        while (i.hasNext()) {
            sb.append(i.next());
        }

        for (; ; ) {
            if (sb.length() >= 15) {
                break;
            }
            char l = letterStr.charAt(new Random().nextInt(26));
            if (sb.toString().indexOf(l) == -1) {
                sb.append(l + "");
            }
        }

        StringBuilder result = new StringBuilder();
        if (sb != null && sb.length() > 0) {
            for (; ; ) {
                if (result.length() >= 15) {
                    break;
                }
                char m = sb.charAt(new Random().nextInt(sb.length()));
                if (result.toString().indexOf(m) == -1) {
                    result.append(m + "");
                }
            }
        }

        return result.toString();
    }


    @Override
    public void onCompleteUI() {

    }

    @Override
    public void onErrorUI(int what, int extra, String msg) {

    }

    @Override
    public void onStopUI() {

    }

    @Override
    public void onStartUI(int duration) {

    }

}
