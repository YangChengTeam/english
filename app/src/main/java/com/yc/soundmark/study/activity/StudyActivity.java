package com.yc.soundmark.study.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.yc.english.R;
import com.yc.english.base.view.StateView;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.soundmark.base.fragment.BasePayFragment;
import com.yc.soundmark.base.utils.UIUtils;
import com.yc.soundmark.base.widget.MainToolBar;
import com.yc.soundmark.category.activity.CategoryActivity;
import com.yc.soundmark.index.activity.PhoneticActivity;
import com.yc.soundmark.study.adapter.StudyMainAdapter;
import com.yc.soundmark.study.contract.StudyContract;
import com.yc.soundmark.study.fragment.StudyMainFragment;
import com.yc.soundmark.study.fragment.StudyPhraseFragment;
import com.yc.soundmark.study.fragment.StudySentenceFragment;
import com.yc.soundmark.study.fragment.StudyVowelFragment;
import com.yc.soundmark.study.fragment.StudyWordFragment;
import com.yc.soundmark.study.model.domain.StudyInfoWrapper;
import com.yc.soundmark.study.presenter.StudyPresenter;
import com.yc.soundmark.study.utils.AVMediaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class StudyActivity extends BaseActivity<StudyPresenter> implements StudyContract.View {


    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_top_tint)
    LinearLayout llTopTint;
    @BindView(R.id.study_viewPager)
    ViewPager studyViewPager;
    @BindView(R.id.iv_show_vowel)
    ImageView ivShowVowel;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_pre)
    ImageView ivPre;
    @BindView(R.id.iv_category)
    ImageView ivCategory;
    @BindView(R.id.container)
    LinearLayout container;


    private List<Fragment> fragments = new ArrayList<>();
    private int currentPos = 0;
    private int totalPages;//总页码


    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    public void init() {

        mPresenter = new StudyPresenter(this, this);

        initListener();
        mainToolbar.setTitle(getString(R.string.soundmark));
        mainToolbar.init(this, PhoneticActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.phonetic_introduce), R.mipmap.soundmark_introduce_icon);
        mPresenter.getStudyPages();
        UIUtils.getInstance(this).measureViewLoction(llTopTint);


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

                studyVowelFragment.show(getSupportFragmentManager(), "");
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
                    ToastUtil.toast2(StudyActivity.this, "已经是最后一页了");
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
                    ToastUtil.toast2(StudyActivity.this, "已经是第一页了");
                }
//                LogUtil.msg("currentPos: pre--> " + currentPos);
            }
        });
        RxView.clicks(ivCategory).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(StudyActivity.this, CategoryActivity.class);
                startActivity(intent);
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

        StudyMainAdapter mainAdapter = new StudyMainAdapter(getSupportFragmentManager(), fragments);
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
        basePayFragment.show(getSupportFragmentManager(), "");
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
    public boolean isStatusBarMateria() {
        return true;
    }


}
