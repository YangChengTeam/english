package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.CoursePlayContract;
import com.yc.english.read.model.domain.EnglishCourseInfoList;
import com.yc.english.read.model.engin.CoursePlayEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/7.
 */

public class CoursePlayPresenter extends BasePresenter<CoursePlayEngin, CoursePlayContract.View> implements CoursePlayContract.Presenter {

    public CoursePlayPresenter(Context context, CoursePlayContract.View view) {
        super(context, view);
        mEngin = new CoursePlayEngin(context);
    }

    @Override
    public void getCourseListByUnitId(int currentPage, int pageCount,String unitId) {
        Subscription subscribe = mEngin.getCourseListByUnitId(currentPage, pageCount,unitId).subscribe(new Subscriber<ResultInfo<EnglishCourseInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<EnglishCourseInfoList> resultInfo) {
                if(resultInfo != null){
                    mView.showCourseListData(resultInfo.data);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
