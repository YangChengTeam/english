package com.yc.soundmark.study.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Builder;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.yc.english.R;
import com.yc.english.base.view.StateView;
import com.yc.soundmark.base.constant.SpConstant;
import com.yc.soundmark.base.utils.UIUtils;
import com.yc.soundmark.study.activity.PreviewActivity;
import com.yc.soundmark.study.adapter.StudyApplyAdapter;
import com.yc.soundmark.study.contract.StudyContract;
import com.yc.soundmark.study.listener.OnAVManagerListener;
import com.yc.soundmark.study.listener.OnUIPracticeControllerListener;
import com.yc.soundmark.study.model.domain.StudyInfo;
import com.yc.soundmark.study.model.domain.StudyInfoWrapper;
import com.yc.soundmark.study.presenter.StudyPresenter;
import com.yc.soundmark.study.utils.LPUtils;
import com.yc.soundmark.study.utils.PpAudioManager;
import com.yc.soundmark.study.widget.MediaPlayerView;
import com.yc.soundmark.study.widget.StudyViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by wanglin  on 2018/10/26 16:23.
 */
public class StudyMainFragment extends BaseFragment<StudyPresenter> implements StudyContract.View, OnUIPracticeControllerListener {

    @BindView(R.id.tv_perception_voice)
    TextView tvPerceptionVoice;
    @BindView(R.id.iv_perception_voice)
    ImageView ivPerceptionVoice;
    @BindView(R.id.ll_perception_voice)
    LinearLayout llPerceptionVoice;
    @BindView(R.id.iv_perception_word)
    ImageView ivPerceptionWord;
    @BindView(R.id.tv_perception_word_example)
    TextView tvPerceptionWordExample;
    @BindView(R.id.tv_pronounce)
    TextView tvPronounce;
    @BindView(R.id.ll_perception_word)
    LinearLayout llPerceptionWord;
    @BindView(R.id.iv_pronounce_icon)
    ImageView ivPronounceIcon;
    @BindView(R.id.rl_pronounce)
    RelativeLayout rlPronounce;
    @BindView(R.id.ll_perception_container)
    LinearLayout llPerceptionContainer;

    @BindView(R.id.ll_study_container)
    LinearLayout llStudyContainer;
    @BindView(R.id.ll_study_total_container)
    LinearLayout llStudyTotalContainer;
    @BindView(R.id.tv_number_progress)
    TextView tvNumberProgress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_practice_soundmark)
    TextView tvPracticeSoundmark;
    @BindView(R.id.iv_practice)
    ImageView ivPractice;

    @BindView(R.id.iv_top_carton)
    ImageView ivTopCarton;
    @BindView(R.id.ll_practice_container)
    LinearLayout llPracticeContainer;
    @BindView(R.id.iv_essentials_example)
    ImageView ivEssentialsExample;
    @BindView(R.id.rl_essentials)
    RelativeLayout rlEssentials;
    @BindView(R.id.tv_essentials_desp)
    TextView tvEssentialsDesp;
    @BindView(R.id.mediaPlayerView)
    MediaPlayerView mediaPlayerView;
    @BindView(R.id.ll_essentials_container)
    LinearLayout llEssentialsContainer;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    StudyViewPager viewPager;
    @BindView(R.id.ll_apply_container)
    LinearLayout llApplyContainer;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_app_title)
    LinearLayout llAppTitle;
    @BindView(R.id.ll_apply_root)
    LinearLayout llApplyRoot;
    @BindView(R.id.exoVideoView)
    ExoVideoView exoVideoView;


    private int playStep = 1;//播放步骤


    private List<Fragment> mFragments;
    private int pos;

    private OnAVManagerListener mListener;
    private int[] firstLayoutIds = new int[]{R.layout.study_perception_guide, R.layout.study_study_guide};


    public void setFragments(List<Fragment> fragments) {
        this.mFragments = fragments;
    }

    @Override
    public int getLayoutId() {
        return R.layout.study_main_item;
    }


    @Override
    public void init() {

        mPresenter = new StudyPresenter(getActivity(), this);

    }


    @Override
    protected void initView() {
    }


    private void initView(StudyInfoWrapper studyInfoWrapper) {

        StudyApplyAdapter studyApplyAdapter = new StudyApplyAdapter(getChildFragmentManager(), getActivity(),
                mFragments, studyInfoWrapper.getWords(), studyInfoWrapper.getPhrase(), studyInfoWrapper.getSentence(), pos);
        viewPager.setAdapter(studyApplyAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void showStudyPages(Integer data) {

    }

    private StudyInfo mStudyInfo;

    @Override
    public void showStudyInfo(StudyInfoWrapper data) {
        //显示引导页
        boolean isShowFirst = SPUtils.getInstance().getBoolean(SpConstant.IS_SHOW_FIRST, true);
        if (isShowFirst && isVisibleToUser) {
            views.clear();
            views.add(llPerceptionContainer);
            views.add(llStudyContainer);
            if (!(TextUtils.equals("meizu", Build.BRAND) || TextUtils.equals("Meizu", Build.BRAND)))
                startGuide(views, firstLayoutIds);

            SPUtils.getInstance().put(SpConstant.IS_SHOW_FIRST, false);
        }

        StudyInfo studyInfo = data.getInfo();
        mStudyInfo = studyInfo;
        tvPerceptionVoice.setText(studyInfo.getName());
        Glide.with(getActivity()).asBitmap().load(studyInfo.getImage()).into(ivPronounceIcon);
        tvPerceptionWordExample.setText(studyInfo.getWord().replaceAll("#", ""));
        tvPronounce.setText(Html.fromHtml(LPUtils.getInstance().addPhraseLetterColor(studyInfo.getVoice())));
//        LogUtil.msg("data: " + studyInfo.getMouth_cover());
        Glide.with(getActivity()).asBitmap().load(studyInfo.getMouth_cover()).into(ivEssentialsExample);
        tvEssentialsDesp.setText(studyInfo.getMouth_desp());
        mediaPlayerView.setPath(studyInfo.getDesp_mp3());
        initView(data);
        playVideo(studyInfo);
        initListener(studyInfo);
    }

    private View currentView;

    private void initListener(final StudyInfo studyInfo) {
        RxView.clicks(llPerceptionVoice).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentView = ivPerceptionVoice;
                mListener.playMusic(studyInfo.getVowel_mp3());
//                startAnimation(tvPerceptionVoice);
            }
        });

        RxView.clicks(llPerceptionWord).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                currentView = ivPerceptionWord;
                mListener.playMusic(studyInfo.getMp3());
            }
        });

        //1.播放引导音 2.播放发音 3.播放di声音4.录音并播放5.播放第二段引导音6
        RxView.clicks(ivPractice).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (mListener.isPlaying()) {
                    playPracticeAfterUpdateUI();
                } else {
                    //todo 播放并录音
                    playStep = 1;
                    mListener.playAssetFile("guide_01.mp3", playStep);
                    if (mStudyInfo != null) {
                        tvPracticeSoundmark.setVisibility(View.VISIBLE);
                        tvPracticeSoundmark.setText(mStudyInfo.getName());
                    }
                }


            }
        });

        RxView.clicks(rlPronounce).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
                if (mStudyInfo != null)
                    intent.putExtra("img", mStudyInfo.getImage());
                startActivity(intent);
            }
        });

        RxView.clicks(rlEssentials).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), PreviewActivity.class);
                if (mStudyInfo != null) {
                    intent.putExtra("img", mStudyInfo.getMouth_cover());
                }
                startActivity(intent);

            }
        });

    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private List<View> views = new ArrayList<>();

    @Override
    public void fetchData() {
        mPresenter.getStudyDetail(pos);
        mListener = new PpAudioManager(getActivity(), this);

    }

    //设置引导视图
    private void showGuide(final List<View> viewList, int[] layoutIds, final int scrollHeight) {

        Builder builder = getBuilder("guide1", 1, scrollHeight);

        if (viewList != null && viewList.size() > 0) {

            for (int i = 0; i < viewList.size(); i++) {

                builder.addGuidePage(GuidePage.newInstance()
                        .addHighLight(viewList.get(i), HighLight.Shape.RECTANGLE, 16)
                        .setEverywhereCancelable(false)

                        .setLayoutRes(layoutIds[i], R.id.iv_next)

                        .setOnLayoutInflatedListener((view, controller) -> {
                        }));

            }

            builder.show();
        }
    }


    private boolean isPracticeShow = true;
    private boolean isApplyShow = true;

    private Builder getBuilder(String label, final int step, final int scrollHeight) {

        return NewbieGuide.with(getActivity())
                .setLabel(label)
                .alwaysShow(true)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                    }

                    @Override
                    public void onRemoved(Controller controller) {

                        if (step == 1 && isPracticeShow) {
                            isPracticeShow = false;
                            nestedScrollView.scrollTo(0, scrollHeight);
                            int[] location = new int[2];
                            llPracticeContainer.getLocationOnScreen(location);
                            RectF rect = new RectF();
                            rect.set(location[0] - ScreenUtil.dip2px(getActivity(), 7), location[1], location[0] + llPerceptionContainer.getRight() - ScreenUtil.dip2px(getActivity(), 10), llPerceptionContainer.getBottom() + location[1] + ScreenUtil.dip2px(getActivity(), 40));

                            int[] location1 = new int[2];
                            llEssentialsContainer.getLocationOnScreen(location1);
                            RectF rectF = new RectF();
                            rectF.set(location1[0] - ScreenUtil.dip2px(getActivity(), 7), location1[1] - ScreenUtil.dip2px(getActivity(), 10),
                                    location1[0] + llEssentialsContainer.getRight() - ScreenUtil.dip2px(getActivity(), 10),
                                    llEssentialsContainer.getBottom() + location1[1] - ScreenUtil.dip2px(getActivity(), 40));

                            practiceGuide(rect, rectF);
                        } else if (step == 2 && isApplyShow) {
                            isApplyShow = false;
                            nestedScrollView.scrollBy(0, ScreenUtil.getHeight(getActivity()) - UIUtils.getInstance(getActivity()).getBottomBarHeight());
                            int[] location = new int[2];
                            llApplyContainer.getLocationOnScreen(location);

                            RectF rectF = new RectF(location[0] - ScreenUtil.dip2px(getActivity(), 7), location[1] - ScreenUtil.dip2px(getActivity(), 10), location[0] + llApplyContainer.getRight() - ScreenUtil.dip2px(getActivity(), 10), llApplyContainer.getBottom() + location[1] - ScreenUtil.dip2px(getActivity(), 20));
                            applyGuide(rectF);

                        } else if (step == 3) {
                            //
                            nestedScrollView.scrollBy(0, -ScreenUtil.getHeight(getActivity()));
                        }
                    }
                });
    }


    private void practiceGuide(final RectF rect, final RectF rectF) {
        llPracticeContainer.post(() -> {
            Builder builder = getBuilder("guide2", 2, 0);
            builder.addGuidePage(GuidePage.newInstance()
                    .addHighLight(rect, HighLight.Shape.RECTANGLE, 16)
                    .setEverywhereCancelable(false)
                    .setLayoutRes(R.layout.study_practice_guide, R.id.iv_next))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(rectF, HighLight.Shape.RECTANGLE, 16)
                            .setEverywhereCancelable(false)
                            .setLayoutRes(R.layout.study_essentials_guide, R.id.iv_next));


            builder.show();
        });
    }

    private void applyGuide(final RectF rect) {
        llPracticeContainer.post(() -> {
            Builder builder = getBuilder("guide3", 3, 0);
            builder.addGuidePage(GuidePage.newInstance()
                    .addHighLight(rect, HighLight.Shape.RECTANGLE, 16)
                    .setEverywhereCancelable(false)
                    .setLayoutRes(R.layout.study_apply_guide, R.id.iv_next));


            builder.show();
        });
    }


    private void startGuide(final List<View> views, final int[] layoutIds) {

        if (getActivity() != null && !getActivity().isDestroyed())
            getActivity().getWindow().getDecorView().postDelayed(() -> {
                int[] location = new int[2];
                llStudyTotalContainer.getLocationOnScreen(location);

                UIUtils instance = UIUtils.getInstance(getActivity());
                final int[] topLocation = instance.getLocation();
                location[1] = location[1] + llStudyTotalContainer.getBottom() - llStudyTotalContainer.getTop() - topLocation[1];

                showGuide(views, layoutIds, location[1]);

            }, 1000);
    }


    /**
     * 播放视频
     *
     * @param studyInfo
     */
    private void playVideo(StudyInfo studyInfo) {
//

        exoVideoView.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        Glide.with(this).load(studyInfo.getVideo_cover()).thumbnail(0.1f).into(exoVideoView.artworkView);
        SimpleMediaSource mediaSource = new SimpleMediaSource(studyInfo.getVoice_video());//uri also supported

        exoVideoView.play(mediaSource, false);//play from a particular position

        exoVideoView.startVideo(true, null);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
            exoVideoView.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            exoVideoView.pause();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            exoVideoView.resume();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if ((Build.VERSION.SDK_INT <= 23)) {
            exoVideoView.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayerView.destroy();
        exoVideoView.releasePlayer();
    }


    @Override
    public void playBeforeUpdateUI() {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            if (currentView == ivPerceptionVoice) {

                Glide.with(getActivity()).asGif().load(R.mipmap.small_trumpet_stop).into(ivPerceptionVoice);
            } else if (currentView == ivPerceptionWord) {
                Glide.with(getActivity()).asGif().load(R.mipmap.big_trumpet_stop).into(ivPerceptionWord);
            }
        }
    }


    @Override
    public void playAfterUpdateUI() {
        if (getActivity() != null && !getActivity().isDestroyed()) {
            Glide.with(getActivity()).clear(ivPerceptionVoice);
            Glide.with(getActivity()).clear(ivPerceptionWord);
        }
        ivPerceptionVoice.setImageResource(R.mipmap.small_trumpet);
        ivPerceptionWord.setImageResource(R.mipmap.big_trumpet);
    }


    @Override
    public void playPracticeBeforeUpdateUI(int progress) {
        ivPractice.setImageResource(R.mipmap.study_practice_pause);
        tvNumberProgress.setText(String.format(getString(R.string.practice_progress), progress));
    }

    @Override
    public void playPracticeFirstUpdateUI() {

        playStep = 2;
        if (mStudyInfo == null) return;
//        String mp3 = "http://thumb.1010pic.com/dmt/diandu/27/mp3/000002_Unit%201_Lesson%2001.mp3";
        mListener.playMusic(mStudyInfo.getVowel_mp3(), false, playStep);

    }

    @Override
    public void playPracticeSecondUpdateUI() {

        playStep = 3;
        mListener.playAssetFile("user_tape_tips.mp3", playStep);
    }

    @Override
    public void recordUpdateUI() {
        ivTopCarton.setVisibility(View.VISIBLE);
    }

    @Override
    public void playPracticeAfterUpdateUI() {
        ivPractice.setImageResource(R.mipmap.study_practice_play);
        if (isAdded())
            tvNumberProgress.setText(String.format(getString(R.string.practice_progress), 0));
        ivTopCarton.setVisibility(View.GONE);
        mListener.stopMusic();
        tvPracticeSoundmark.setVisibility(View.GONE);

    }

    @Override
    public void updateProgressBar(int percent) {
        progressBar.setProgress(percent);
        if (percent != 100) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void playPracticeThirdUpdateUI() {
        playStep = 1;
        mListener.playAssetFile("guide_02.mp3", playStep);
        ivTopCarton.setVisibility(View.GONE);
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(nestedScrollView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(nestedScrollView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(nestedScrollView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getStudyDetail(pos);
            }
        });
    }

}

