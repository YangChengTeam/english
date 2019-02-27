package com.yc.soundmark.study.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.soundmark.study.contract.StudyContract;
import com.yc.soundmark.study.model.domain.StudyInfoWrapper;
import com.yc.soundmark.study.model.domain.StudyPages;
import com.yc.soundmark.study.model.engine.StudyEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/10/30 16:40.
 */
public class StudyPresenter extends BasePresenter<StudyEngine, StudyContract.View> implements StudyContract.Presenter {
    public StudyPresenter(Context context, StudyContract.View view) {
        super(context, view);
        mEngine = new StudyEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;


    }

    @Override
    public void getStudyPages() {
//        mView.showLoading();
        Subscription subscription = mEngine.getStudyPages().subscribe(new Subscriber<ResultInfo<StudyPages>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<StudyPages> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK && stringResultInfo.data != null) {
                        mView.hide();
//                        getStudyDetail(0, stringResultInfo.data.count);
                        mView.showStudyPages(stringResultInfo.data.count);
                    } else {
                        mView.showNoData();
                    }
                } else {
                    mView.showNoNet();
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getStudyDetail(final int page) {
        mView.showLoading();
        Subscription subscription = mEngine.getStudyDetail(page).subscribe(new Subscriber<ResultInfo<StudyInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(ResultInfo<StudyInfoWrapper> studyInfoWrapperResultInfo) {
                if (studyInfoWrapperResultInfo != null) {
                    if (studyInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && studyInfoWrapperResultInfo.data != null) {
                        mView.hide();

                        mView.showStudyInfo(studyInfoWrapperResultInfo.data);

                    } else {
                        mView.showNoData();
                    }

                } else {
                    mView.showNoNet();
                }

            }
        });
        mSubscriptions.add(subscription);

    }
}
