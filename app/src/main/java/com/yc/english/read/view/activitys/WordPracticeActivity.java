package com.yc.english.read.view.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.yc.english.R;
import com.yc.english.base.utils.DrawableUtils;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.common.SpeechUtil;
import com.yc.english.read.contract.ReadWordContract;
import com.yc.english.read.model.domain.LetterInfo;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.presenter.ReadWordPresenter;
import com.yc.english.read.view.adapter.ReadLetterItemClickAdapter;
import com.yc.english.read.view.wdigets.CountDown;
import com.yc.english.read.view.wdigets.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/26.
 */

public class WordPracticeActivity extends FullScreenActivity<ReadWordPresenter> implements ReadWordContract.View {

    @BindView(R.id.rv_letter_list)
    RecyclerView mLetterRecyclerView;

    @BindView(R.id.et_word_input)
    TextView mWordInputTextView;

    @BindView(R.id.iv_word_delete)
    ImageView mWordDeleteImageView;

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

    @BindView(R.id.btn_error_again_word)
    Button mErrorAgainButton;

    @BindView(R.id.btn_error_next_word)
    Button mErrorNextButton;

    private String[] mLetterListValues;

    private ReadLetterItemClickAdapter mLetterAdapter;

    private List<LetterInfo> mLetterDatas;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认云端发音人
    private String voicer = "catherine";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 缓冲进度
    private int mPercentForBuffering = 0;

    // 播放进度
    private int mPercentForPlaying = 0;

    private String unitId;

    CountDown countDown;

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

        mToolbar.setTitle("5/10");
        mToolbar.showNavigationIcon();

        SpeechUtil.initSpeech(WordPracticeActivity.this, 28, 50, 50, 1);
        mTts = SpeechUtil.getmTts();

        mLetterListValues = new String[]{"m", "p", "a", "o", "g", "s", "e", "p", "w", "y", "k", "b", "s", "o", "c"};

        mLetterDatas = new ArrayList<LetterInfo>();

        for (int i = 0; i < mLetterListValues.length; i++) {
            LetterInfo letterInfo = new LetterInfo(LetterInfo.CLICK_ITEM_VIEW);
            letterInfo.setLetterName(mLetterListValues[i]);
            mLetterDatas.add(letterInfo);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        mLetterRecyclerView.setLayoutManager(layoutManager);
        mLetterRecyclerView.addItemDecoration(new GridSpacingItemDecoration(5, getResources().getDimensionPixelSize(R.dimen.dp_6), true));
        mLetterRecyclerView.setHasFixedSize(true);

        mLetterRecyclerView.setLayoutManager(layoutManager);
        mLetterAdapter = new ReadLetterItemClickAdapter(this, mLetterDatas);
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

        countDown = new CountDown(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mWordCountDownTextView.setText(toClock(millisUntilFinished));
            }

            @Override
            public String toClock(long millis) {
                return super.toClock(millis);
            }
        };
        countDown.start();

        startSynthesizer("book");
    }

    public void startSynthesizer(String text) {
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

    @SuppressLint("NewApi")
    @OnClick(R.id.btn_check_word)
    public void checkWord() {
        if (StringUtils.isEmpty(mWordInputTextView.getText())) {
            ToastUtils.showLong("请输入单词");
            return;
        }

        if ("book".equals(mWordInputTextView.getText())) {
            mRightLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.INVISIBLE);
            mLetterListLayout.setVisibility(View.GONE);
            mWordInputTextView.setTextColor(ContextCompat.getColor(this, R.color.right_word_color));
        } else {
            mRightLayout.setVisibility(View.INVISIBLE);
            mErrorLayout.setVisibility(View.VISIBLE);
            mLetterListLayout.setVisibility(View.GONE);
            mWordInputTextView.setTextColor(ContextCompat.getColor(this, R.color.read_word_share_btn_color));
            mErrorAgainButton.setBackground(DrawableUtils.getBgColor(this, 3, R.color.right_word_btn_again_color));
        }
    }

    @OnClick(R.id.btn_error_again_word)
    public void inputAgain() {
        nextWord(0);
    }

    @OnClick(R.id.btn_error_next_word)
    public void nextWordInError() {
        nextWord(0);
    }

    @OnClick(R.id.btn_right_next_word)
    public void nextWordInERight() {
        nextWord(0);
    }

    public void nextWord(int wordPosition) {
        mLetterListLayout.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.INVISIBLE);
        mWordInputTextView.setText("");
        mWordInputTextView.setTextColor(ContextCompat.getColor(this, R.color.black_333));
        countDown.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    @Override
    public void showWordListData(List<WordInfo> list) {

    }
}
