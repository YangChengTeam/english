package com.yc.english.read.view.activitys;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.utils.WakeLockUtils;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.read.common.SpeechUtils;
import com.yc.english.read.contract.CoursePlayContract;
import com.yc.english.read.model.domain.EnglishCourseInfo;
import com.yc.english.read.model.domain.EnglishCourseInfoList;
import com.yc.english.read.presenter.CoursePlayPresenter;
import com.yc.english.read.view.adapter.ReadCourseItemClickAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindInt;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class CoursePlayActivity extends FullScreenActivity<CoursePlayPresenter> implements CoursePlayContract.View {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.layout_content)
    FrameLayout mLayoutContext;

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

    @BindView(R.id.iv_next_unit)
    ImageView mNextUnitImageView;

    @BindView(R.id.iv_language_change)
    ImageView mLanguageChangeImageView;

    ReadCourseItemClickAdapter mItemAdapter;

    private int playPosition = -1;

    LinearLayoutManager linearLayoutManager;

    // 语音合成对象
    private SpeechSynthesizer mTts;

    private boolean isCountinue = false;

    private int languageType = 1; //1:中英,2:英,3:中

    private PublishSubject mTsSubject;

    private String unitId;

    private String unitTitle;

    private String lastUnitIds;

    private String[] nextUnitIds;

    private String lastUnitTitles;

    private String[] nextUnitTitles;

    private int currentUnitPosition;

    private int currentPage = 1;

    private boolean isNext = false;

    @BindView(R.id.toolbarWarpper)
    FrameLayout mToolbarWarpper;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_course_play;
    }

    @Override
    public void init() {
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar );

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unitId = bundle.getString("unit_id");
            unitTitle = bundle.getString("unit_title");
            lastUnitIds = bundle.getString("last_unit_ids");
            lastUnitTitles = bundle.getString("last_unit_titles");
            if (!StringUtils.isEmpty(lastUnitIds)) {
                nextUnitIds = lastUnitIds.split(",");
            }

            if (!StringUtils.isEmpty(lastUnitTitles)) {
                nextUnitTitles = lastUnitTitles.split("#");
            }
        }

        WakeLockUtils.acquireWakeLock(this);
        mTts = SpeechUtils.getTts(this);
        mPresenter = new CoursePlayPresenter(this, this);

        mToolbar.setTitle(unitTitle != null ? unitTitle : "");
        mToolbar.showNavigationIcon();
        mToolbar.setTitleColor(ContextCompat.getColor(this, R.color.black_333));

        linearLayoutManager = new LinearLayoutManager(this);
        mCourseRecyclerView.setLayoutManager(linearLayoutManager);

        mItemAdapter = new ReadCourseItemClickAdapter(this, null);
        mCourseRecyclerView.setAdapter(mItemAdapter);

        mTsSubject = PublishSubject.create();
        mTsSubject.delay(800, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                if (playPosition < mItemAdapter.getData().size()) {
                    enableState(playPosition);
                    startSynthesizer(position);
                } else {
                    isCountinue = false;
                    disableState();
                }
            }
        });

        //下一单元
        RxView.clicks(mNextUnitImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (nextUnitIds != null && currentUnitPosition < nextUnitIds.length) {
                    isNext = true;
                    resetPlayState();
                    if (nextUnitTitles != null && currentUnitPosition < nextUnitTitles.length) {
                        mToolbar.setTitle(nextUnitTitles[currentUnitPosition]);
                    }
                    unitId = nextUnitIds[currentUnitPosition];
                    currentUnitPosition++;
                    currentPage = 1;
                    mPresenter.getCourseListByUnitId(currentPage, 0, unitId);
                } else {
                    TipsHelper.tips(CoursePlayActivity.this, "已经是最后一个单元");
                }
            }
        });

        //播放
        RxView.clicks(mCoursePlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (playPosition == -1) {
                    playPosition = 0;
                }
                isCountinue = !isCountinue;
                if (isCountinue) {
                    enableState(playPosition);
                    startSynthesizer(playPosition);
                } else {
                    disableState();
                }
            }
        });

        //语言切换
        RxView.clicks(mLanguageChangeImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                languageType++;

                if (languageType > 3) {
                    languageType = 1;
                }
                if (playPosition > -1 && playPosition < mItemAdapter.getData().size()) {
                    mItemAdapter.getData().get(playPosition).setPlay(true);
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
        });

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, final View view, int position) {
                if (position == playPosition) {
                    return;
                }
                isCountinue = false;
                playPosition = position;
                enableState(playPosition);
                startSynthesizer(playPosition);
            }
        });

        mCourseRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    currentPage++;
                    mPresenter.getCourseListByUnitId(currentPage, 0, unitId);
                }
            }
        });

        mPresenter.getCourseListByUnitId(currentPage, 0, unitId);

        if (com.yc.english.base.utils.SpeechUtils.getAppids() == null || com.yc.english.base.utils.SpeechUtils.getAppids().size() <= 0) {
            com.yc.english.base.utils.SpeechUtils.setAppids(this);
        }
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    private void resetPlayState() {
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
        playPosition = -1;
        isCountinue = false;
        disableState();
    }

    @Override
    public void hideStateView() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mCourseRecyclerView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getCourseListByUnitId(currentPage, 0, unitId);
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(mCourseRecyclerView);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(mCourseRecyclerView, 2);
    }

    /**
     * 语音合成播放
     *
     * @param postion
     */
    public void startSynthesizer(int postion) {
        if (postion < 0 || postion >= mItemAdapter.getData().size()) {
            return;
        }
        mTts = SpeechUtils.getTts(this);
        String text = mItemAdapter.getData().get(postion).getSubTitle();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                TipsHelper.tips(CoursePlayActivity.this, "语音合成失败");
            } else {
                TipsHelper.tips(CoursePlayActivity.this, "语音合成失败");
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
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                speekContinue(isCountinue ? ++playPosition : playPosition);
            } else if (error != null) {
                if (error.getErrorDescription().contains("权")) {
                    com.yc.english.base.utils.SpeechUtils.resetAppid(CoursePlayActivity.this);
                    return;
                }
                speekContinue(playPosition);
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    };

    public void speekContinue(int index) {
        if (isCountinue) {
            mTsSubject.onNext(index);
        } else {
            disableState();
        }
    }

    public void resetPlay() {
        for (EnglishCourseInfo englishCourseInfo : mItemAdapter.getData()) {
            englishCourseInfo.setPlay(false);
        }
    }

    public void enableState(int postion) {
        if (postion < 0 || postion >= mItemAdapter.getData().size()) {
            return;
        }
        if (playPosition > 2) {
            linearLayoutManager.scrollToPositionWithOffset(playPosition - 2, 0);
        }
        if (mTts != null) {
            mTts.stopSpeaking();
        }
        resetPlay();
        mCoursePlayImageView.setBackgroundResource(R.drawable.read_playing_course_btn_selector);
        mItemAdapter.getData().get(postion).setPlay(true);
        mItemAdapter.notifyDataSetChanged();
    }

    public void disableState() {
        if (!isCountinue) {
            mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
        }
        mTts.stopSpeaking();
        resetPlay();
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            mTts.destroy();
            mTts = null;
        }
        WakeLockUtils.releaseWakeLock();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
    }

    @Override
    public void showCourseListData(EnglishCourseInfoList englishCourseInfoList) {
        if (englishCourseInfoList != null) {
            if (isNext) {
                mItemAdapter.setNewData(englishCourseInfoList.list);
                isNext = false;
            } else {
                mItemAdapter.addData(englishCourseInfoList.list);
            }
        }
    }

}
