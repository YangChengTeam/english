package com.yc.english.read.view.activitys;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.common.SpeechUtil;
import com.yc.english.read.contract.CoursePlayContract;
import com.yc.english.read.model.domain.EnglishCourseInfo;
import com.yc.english.read.model.domain.EnglishCourseInfoList;
import com.yc.english.read.presenter.CoursePlayPresenter;
import com.yc.english.read.view.adapter.ReadCourseItemClickAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class CoursePlayActivity extends FullScreenActivity<CoursePlayPresenter> implements CoursePlayContract.View {

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

    @BindView(R.id.iv_next_unit)
    ImageView mNextUnitImageView;

    @BindView(R.id.iv_language_change)
    ImageView mLanguageChangeImageView;

    ReadCourseItemClickAdapter mItemAdapter;

    private int playPosition;

    private int lastPosition = -1;

    LinearLayoutManager linearLayoutManager;

    private boolean isPlay;

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

    private int currentPosition;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_course_play;
    }

    @Override
    public void init() {

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

        SpeechUtil.initSpeech(CoursePlayActivity.this, 28, 50, 50, 1);
        mTts = SpeechUtil.getmTts();

        mPresenter = new CoursePlayPresenter(this, this);

        mToolbar.setTitle(unitTitle != null ? unitTitle : "");
        mToolbar.showNavigationIcon();
        mToolbar.setTitleColor(ContextCompat.getColor(this, R.color.black_333));

        linearLayoutManager = new LinearLayoutManager(this);
        mCourseRecyclerView.setLayoutManager(linearLayoutManager);

        mItemAdapter = new ReadCourseItemClickAdapter(this, null);
        mCourseRecyclerView.setAdapter(mItemAdapter);

        mTsSubject = PublishSubject.create();
        mTsSubject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer position) {
                startSynthesizer(position);
            }
        });

        //下一单元
        RxView.clicks(mNextUnitImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (nextUnitIds != null && currentPosition < nextUnitIds.length) {
                    if(nextUnitTitles != null && currentPosition < nextUnitTitles.length){
                        mToolbar.setTitle(nextUnitTitles[currentPosition]);
                    }
                    unitId = nextUnitIds[currentPosition];
                    currentPosition++;
                    mPresenter.getCourseListByUnitId(0, 0, unitId);
                } else {
                    TipsHelper.tips(CoursePlayActivity.this, "已经是最后一个单元");
                }
            }
        });

        //播放
        RxView.clicks(mCoursePlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isCountinue) {
                    mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
                    mTts.stopSpeaking();
                    hideItemView(playPosition);
                } else {
                    startSynthesizer(playPosition);
                }
                isCountinue = !isCountinue;
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
                hideItemView(playPosition);
                showItemView(position);
                playPosition = position;
                startSynthesizer(playPosition);
            }
        });

        mPresenter.getCourseListByUnitId(0, 0, unitId);

    }

    /**
     * 语音合成播放
     *
     * @param postion
     */
    public void startSynthesizer(int postion) {
        mCoursePlayImageView.setBackgroundResource(R.drawable.read_playing_course_btn_selector);
        String text = mItemAdapter.getData().get(postion).getTitle();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                // 未安装则跳转到提示安装页面
                //mInstaller.install();
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

            //开始播放
            if (isCountinue) {
                if (playPosition >= mItemAdapter.getData().size()) {
                    mTts.stopSpeaking();
                    playPosition = 0;
                    lastPosition = -1;
                    mCourseRecyclerView.scrollToPosition(0);
                    return;
                }
                showItemView(playPosition);
                hideItemView(lastPosition);
            }
            if (playPosition > 2) {
                mCourseRecyclerView.scrollToPosition(playPosition + 2);
            }
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
                lastPosition = playPosition;
                resetPlay();
                playPosition++;
                if (isCountinue) {
                    try {
                        Thread.sleep(300);
                        mTsSubject.onNext(playPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    hideItemView(lastPosition);
                    mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
                }
            } else if (error != null) {
                mTts.stopSpeaking();
                resetPlay();
                mCoursePlayImageView.setBackgroundResource(R.drawable.read_play_course_btn_selector);
                TipsHelper.tips(getBaseContext(), error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
        }
    };


    public void resetPlay() {
        for (EnglishCourseInfo englishCourseInfo : mItemAdapter.getData()) {
            englishCourseInfo.setPlay(false);
        }
    }

    public void showItemView(int postion) {
        if (postion == -1) {
            return;
        }
        resetPlay();
        mItemAdapter.getData().get(postion).setPlay(true);
        View view = linearLayoutManager.findViewByPosition(postion);
        if (view != null) {
            ImageView mReadPlayIv = (ImageView) view.findViewById(R.id.iv_audio_gif_play);
            TextView mChineseTv = (TextView) view.findViewById(R.id.tv_chinese_title);
            TextView mEnglishTv = (TextView) view.findViewById(R.id.tv_english_title);

            mReadPlayIv.setVisibility(View.VISIBLE);
            Glide.with(CoursePlayActivity.this).load(R.mipmap.read_audio_gif_play).into(mReadPlayIv);
            mChineseTv.setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.black_333));
            mEnglishTv.setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.black_333));
        }
    }

    public void hideItemView(int postion) {
        if (postion == -1) {
            return;
        }
        View view = linearLayoutManager.findViewByPosition(postion);
        if (view != null) {
            view.findViewById(R.id.iv_audio_gif_play).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tv_chinese_title)).setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.gray_999));
            ((TextView) view.findViewById(R.id.tv_english_title)).setTextColor(ContextCompat.getColor(CoursePlayActivity.this, R.color.gray_999));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTts) {
            mTts.stopSpeaking();
            mTts.destroy();
        }
    }

    @Override
    public void showCourseListData(EnglishCourseInfoList englishCourseInfoList) {
        if (englishCourseInfoList != null) {
            mItemAdapter.setNewData(englishCourseInfoList.list);
        }
    }

}
