package com.yc.soundmark.study.fragment;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.junior.english.R;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.soundmark.base.constant.Config;
import com.yc.soundmark.base.fragment.BasePayFragment;
import com.yc.soundmark.base.utils.UIUtils;
import com.yc.soundmark.base.widget.MainToolBar;
import com.yc.soundmark.study.adapter.StudyMainAdapter;
import com.yc.soundmark.study.contract.StudyContract;
import com.yc.soundmark.study.model.domain.StudyInfoWrapper;
import com.yc.soundmark.study.presenter.StudyPresenter;
import com.yc.soundmark.study.utils.AVMediaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class StudyFragment extends BaseFragment<StudyPresenter> implements StudyContract.View, OnAdvStateListener {

    private ViewPager studyViewPager;
    private ImageView ivShowVowel;
    private MainToolBar mainToolbar;
    private ImageView ivNext;
    private ImageView ivPre;
    private LinearLayout llTopTint;

    private LinearLayout container;

    private FrameLayout bottomContainer;


    private List<Fragment> fragments = new ArrayList<>();
    private int currentPos = 0;
    private int totalPages;//总页码


    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    public void init() {
        mPresenter = new StudyPresenter(getActivity(), this);

        initListener();
//        mainToolbar.init(((BaseActivity) getActivity()), WebActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.diandu), R.mipmap.diandu);

        UIUtils.getInstance(getActivity()).measureViewLoction(llTopTint);

        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND)))
            AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bottomContainer, null, Config.TENCENT_ADV_ID, Config.BANNER_BOTTOM_ADV_ID, this);

    }

    @Override
    protected void initView() {
        studyViewPager = (ViewPager) getChildView(R.id.study_viewPager);
        ivShowVowel = (ImageView) getChildView(R.id.iv_show_vowel);
        mainToolbar = (MainToolBar) getChildView(R.id.main_toolbar);
        ivNext = (ImageView) getChildView(R.id.iv_next);
        ivPre = (ImageView) getChildView(R.id.iv_pre);
        llTopTint = (LinearLayout) getChildView(R.id.ll_top_tint);
        container = (LinearLayout) getChildView(R.id.container);
        ivPre = (ImageView) getChildView(R.id.bottom_container);
    }

    private void initListener() {
        RxView.clicks(ivShowVowel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                StudyVowelFragment studyVowelFragment = new StudyVowelFragment();
                studyVowelFragment.setOnClickListener(new StudyVowelFragment.onClickListener() {
                    @Override
                    public void onClick(int pos) {
                        if (pos < totalPages) {
                            studyViewPager.setCurrentItem(pos);
                            currentPos = pos;
                        }

                        if (pos == 0) {
                            ivPre.setImageResource(R.mipmap.study_pre_normal);
                        } else if (pos == totalPages - 1) {
                            ivNext.setImageResource(R.mipmap.study_next_normal_);
                        }
                    }
                });

                studyVowelFragment.show(getFragmentManager(), "");
            }
        });

        RxView.clicks(ivNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 下一页

                currentPos++;
                if (currentPos < totalPages) {
                    if (isCanNext(currentPos)) {
                        next(currentPos);
                    } else {
                        currentPos--;
                        showPayDialog();
                    }
                } else {
                    currentPos--;
                    ToastUtil.toast2(getActivity(), "已经是最后一页了");
                }

//                LogUtil.msg("currentPos: next--> " + currentPos);


            }
        });
        RxView.clicks(ivPre).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                // TODO: 2018/11/2 上一页
                if (currentPos > 0) {
                    currentPos--;
                    pre(currentPos);
                } else {
                    ivPre.setImageResource(R.mipmap.study_pre_normal);
                    ToastUtil.toast2(getActivity(), "已经是第一页了");
                }
//                LogUtil.msg("currentPos: pre--> " + currentPos);
            }
        });


    }


    private void next(int pos) {//下一页

        studyViewPager.setCurrentItem(pos);

        ivPre.setImageResource(R.mipmap.study_pre_selected);
        if (pos == totalPages - 1) {
            ivNext.setImageResource(R.mipmap.study_next_normal_);
        }

    }

    private void pre(int pos) {//上一页

        studyViewPager.setCurrentItem(pos);
        ivNext.setImageResource(R.mipmap.study_next_selected);
        if (pos == 0) {
            ivPre.setImageResource(R.mipmap.study_pre_normal);
        }
    }


    @Override
    public void showStudyPages(Integer data) {
        totalPages = data;
        initViewPager(data);
    }

    private void initViewPager(Integer data) {
        LogUtil.msg("data:  " + data);
        for (int i = 0; i < data; i++) {
            StudyMainFragment studyMainFragment = new StudyMainFragment();
            studyMainFragment.setPos(i);


            List<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(new StudyWordFragment());
            fragmentList.add(new StudyPhraseFragment());
            fragmentList.add(new StudySentenceFragment());
            studyMainFragment.setFragments(fragmentList);

            fragments.add(studyMainFragment);
        }

        StudyMainAdapter mainAdapter = new StudyMainAdapter(getChildFragmentManager(), fragments);
        studyViewPager.setAdapter(mainAdapter);
//        studyViewPager.setOffscreenPageLimit(2);
        studyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                XinQuVideoPlayer.releaseAllVideos();
                AVMediaManager.getInstance().releaseAudioManager();

//                LogUtil.msg("currentPos: scroll-->" + currentPos + "--position-->" + position);
                if (isCanNext(position)) {
                    if (currentPos > position) {
                        pre(position);
                    } else {
                        next(position);
                    }
                    currentPos = position;
                } else {
                    studyViewPager.setCurrentItem(currentPos);
                    showPayDialog();
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void showStudyInfo(StudyInfoWrapper data) {

    }


    private boolean isCanNext(int pos) {
        boolean isNext = false;
        if (UserInfoHelper.isYbVip() || pos < 4) {
            isNext = true;
        }
        return isNext;

    }

    private void showPayDialog() {
        BasePayFragment basePayFragment = new BasePayFragment();
        basePayFragment.show(getFragmentManager(), "");
    }

    @Override
    public void hide() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNoData() {
    }

    @Override
    public void showNoNet() {

    }


    @Override
    public void fetchData() {
        super.fetchData();
        mPresenter.getStudyPages();
    }


    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }
}
