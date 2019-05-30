package com.yc.junior.english.read.view.activitys;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.yc.junior.english.R;
import com.yc.junior.english.base.utils.WakeLockUtils;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.read.common.AudioPlayManager;
import com.yc.junior.english.read.common.MediaPlayerPlayer;
import com.yc.junior.english.read.common.OnUiUpdateManager;
import com.yc.junior.english.read.contract.ReadWordContract;
import com.yc.junior.english.read.model.domain.WordDetailInfo;
import com.yc.junior.english.read.model.domain.WordInfo;
import com.yc.junior.english.read.presenter.ReadWordPresenter;
import com.yc.junior.english.read.view.adapter.ReadWordExpandAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import yc.com.blankj.utilcode.util.ActivityUtils;



/**
 * Created by admin on 2017/7/27.
 */

public class ReadWordActivity extends FullScreenActivity<ReadWordPresenter> implements ReadWordContract.View, ReadWordExpandAdapter.ItemViewClickListener, OnUiUpdateManager {

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

    ReadWordExpandAdapter mReadWordExpandAdapter;

    private List<WordInfo> mDatas;

    private boolean isSpell = false;//是否拼写朗读


    private int readCurrentWordIndex;

    private MediaPlayer mediaPlayer;

    //当前读到的单词
    private int currentIndex;

    private boolean isContinue = false;//是否全部朗读

    private String unitId;

    private String unitTitle;

    private PublishSubject<Integer> mTsSubject;

    private boolean isWordDetailPlay = false;

    private int lastExpandPosition = -1;

    private List<WordInfo> wordInfos;

    private List<WordDetailInfo> wordDetailInfos;

    private int groupCurrentIndex;
    private View groupCurrentView;

    private AudioPlayManager manager;

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
        WakeLockUtils.acquireWakeLock(this);
        mPresenter = new ReadWordPresenter(this, this);
        mToolbar.setTitle(unitTitle);
        mToolbar.showNavigationIcon();

        initMediaPlayer();
        mediaPlayer = new MediaPlayer();


        mReadWordExpandAdapter = new ReadWordExpandAdapter(ReadWordActivity.this, wordInfos, wordDetailInfos);
        mReadWordExpandAdapter.setExpandableListView(mWordListView);
        mWordListView.setAdapter(mReadWordExpandAdapter);
        mWordListView.setGroupIndicator(null);
        mReadWordExpandAdapter.setItemDetailClick(this);
        mWordListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            disableWordDetailState();
            isContinue = false;
            readOver(currentIndex);
            if (groupPosition != lastExpandPosition && lastExpandPosition > -1) {
                mWordListView.collapseGroup(lastExpandPosition);
            }

            if (mWordListView.isGroupExpanded(groupPosition)) {
                mWordListView.collapseGroup(groupPosition);
            } else {
                mWordListView.expandGroup(groupPosition);
                lastExpandPosition = groupPosition;
                mReadWordExpandAdapter.setLastExpandPosition(lastExpandPosition);
            }
            return true;
        });

        mWordListView.setOnChildClickListener((parent, view, groupPosition, childPosition, id) -> {
            isContinue = false;
            readOver(currentIndex);
            if (isWordDetailPlay) {
                return true;
            }
            groupCurrentIndex = groupPosition;
            isWordDetailPlay = true;

            startPlay(wordDetailInfos.get(groupPosition).getVoice());
            wordDetailInfos.get(groupPosition).setPlay(true);
            groupCurrentView = view;
            mReadWordExpandAdapter.setChildViewPlayState(view, true);
            return true;
        });

        mTsSubject = PublishSubject.create();
        mTsSubject.delay(800, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe((Action1<Integer>) position -> {
            if (position < mDatas.size()) {


//                    startSynthesizer(position);


                enableState(currentIndex);

                startPlay(position);
            } else {
                isContinue = false;
                readOver(currentIndex);
                currentIndex = 0;
                if (ActivityUtils.isValidContext(ReadWordActivity.this)) {
                    Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView);
                }
            }
        });

        mPresenter.getWordListByUnitId(0, 0, unitId);

        if (com.yc.junior.english.base.utils.SpeechUtils.getAppids() == null || com.yc.junior.english.base.utils.SpeechUtils.getAppids().size() <= 0) {
            com.yc.junior.english.base.utils.SpeechUtils.setAppids(this);
        }
    }


    private void initMediaPlayer() {
        manager = new MediaPlayerPlayer(this);
    }

    @Override
    public void hide() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力", v -> mPresenter.getWordListByUnitId(0, 0, unitId));
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

            mDatas = list;
            wordInfos = new ArrayList<>();
            wordDetailInfos = new ArrayList<>();
            setProgressNum(0, list.size());

            for (int i = 0; i < list.size(); i++) {
                WordInfo wordInfo = list.get(i);
                WordDetailInfo wordDetailInfo = new WordDetailInfo();
                wordDetailInfo.setWordExample(wordInfo.getEpSentence());
                wordDetailInfo.setWordCnExample(wordInfo.getEpSentenceMeans());
                wordDetailInfo.setVoice(wordInfo.getMp3url());
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
    public void playWord(final int index, final Runnable runnable) {
        if (isSpell) {
            try {
                String readCurrentWord = mDatas.get(index).getName().replaceAll("[\\s\\W]", "");

                if (readCurrentWordIndex < readCurrentWord.length()) {
                    mediaPlayer.reset();
                    String readChat = String.valueOf(readCurrentWord.charAt(readCurrentWordIndex)).toLowerCase();

                    AssetFileDescriptor fd = getAssets().openFd(readChat + ".mp3");
                    mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        readCurrentWordIndex++;
                        playWord(index, runnable);
                    });
                } else {
                    readCurrentWordIndex = 0;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (runnable != null) {
                runnable.run();
            }
        }
    }







    public void startPlay(int position) {
        if (position < 0 || position >= wordInfos.size()) {
            return;
        }
        String url;


        if (isSpell) {
            url = wordInfos.get(position).getWord_voice();
        } else {
            url = wordInfos.get(position).getVoice();
        }

        if (TextUtils.isEmpty(url)) {
            disableState(position);
            return;

        }

        Log.e("TAG", "startPlay: " + url);
        if (manager == null) manager = new MediaPlayerPlayer(this);
        manager.start(url);

    }

    //示例句子朗读
    public void startPlay(String url) {
        if (TextUtils.isEmpty(url)) {
            disableWordDetailState();
            return;
        }
        if (manager == null) manager = new MediaPlayerPlayer(this);
        manager.start(url);
    }






    private boolean disableWordDetailState() {
        if (isWordDetailPlay) {

            if (manager != null) {
                manager.stop();
            }
            isWordDetailPlay = false;
            if (groupCurrentView != null && groupCurrentIndex != -1) {
                wordDetailInfos.get(groupCurrentIndex).setPlay(false);
                mReadWordExpandAdapter.setChildViewPlayState(groupCurrentView, false);
            }
        }
        return isWordDetailPlay;
    }

    private void speakContinue() {
        readOver(currentIndex);
        if (isContinue && currentIndex < mReadWordExpandAdapter.getWordInfos().size()) {
            mTsSubject.onNext(++currentIndex);
        } else {
            disableWordDetailState();
        }
    }

    private void enableState(int index) {
        if (index < 0 || index >= mReadWordExpandAdapter.getWordInfos().size()) {
            return;
        }

        resetPlay();
        mReadWordExpandAdapter.setViewPlayState(index, mWordListView, true);
        mReadWordExpandAdapter.getWordInfos().get(index).setPlay(true);
        setProgressNum(currentIndex + 1, mDatas.size());
        mWordListView.smoothScrollToPosition(index);
    }

    private void disableState(int index) {
        if (index <= 0 && index < mReadWordExpandAdapter.getWordInfos().size()) {
            return;
        }

        mReadWordExpandAdapter.setViewPlayState(index, mWordListView, false);
        mReadWordExpandAdapter.getWordInfos().get(index).setPlay(false);

        if (manager != null) {
            manager.stop();
        }
    }

    private void readOver(int index) {
        resetPlay();
        mReadWordExpandAdapter.setViewPlayState(index, mWordListView, false);

        if (manager != null) manager.stop();
        if (!isContinue && ActivityUtils.isValidContext(this) && ActivityUtils.isValidContext(ReadWordActivity.this)) {
            Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_stop).into(mReadAllImageView);
        }
    }

    public void resetPlay() {
        for (WordInfo wordInfo : mReadWordExpandAdapter.getWordInfos()) {
            wordInfo.setPlay(false);
        }
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
        disableWordDetailState();
        isContinue = !isContinue;
        if (isSpell) {
            isSpell = false;
            isContinue = true;
        }

        readAllOrSpell(isContinue, isSpell);

    }

    private void readAllOrSpell(boolean isContinue, boolean isSpell) {
        if (isContinue || isSpell) {
            if (currentIndex < mDatas.size()) {
                if (ActivityUtils.isValidContext(ReadWordActivity.this)) {
                    Glide.with(ReadWordActivity.this).load(R.mipmap.read_audio_white_gif_play).into(mReadAllImageView);
                }
                enableState(currentIndex);
                startPlay(currentIndex);
            }
        } else {
            readOver(currentIndex);
        }
    }

    /**
     * 拼写朗读
     */
    @OnClick(R.id.layout_spell_word)
    public void spellWord() {
        isSpell = !isSpell;
        if (!isContinue) {
            isContinue = true;
        }

        if (isSpell) {
            mSpellWordImageView.setVisibility(View.VISIBLE);
        } else {
            mSpellWordImageView.setVisibility(View.GONE);
        }
        readAllOrSpell(isContinue, isSpell);
    }


    @Override
    public void groupWordClick(int gPosition) {

        isContinue = false;
        disableWordDetailState();
        if (currentIndex != gPosition) {
            disableState(currentIndex);
        }
        currentIndex = gPosition;
        enableState(currentIndex);

        startPlay(currentIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (manager != null)
            manager.onDestroy();
        WakeLockUtils.releaseWakeLock();
    }


    @Override
    public void onCompleteUI() {



//        runOnUiThread(() ->
//                if(){
//
//        }
//            playWord(currentIndex, this::speakContinue));


        runOnUiThread(this::speakContinue);



    }

    @Override
    public void onErrorUI(int what, int extra, String msg) {
//        playWord(currentIndex, this::speakContinue);
        speakContinue();
    }

    @Override
    public void onStopUI() {

    }

    @Override
    public void onStartUI(int duration) {

    }
}
