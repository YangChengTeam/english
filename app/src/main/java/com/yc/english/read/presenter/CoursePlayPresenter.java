package com.yc.english.read.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.CoursePlayContract;
import com.yc.english.read.model.domain.EnglishCourseInfo;
import com.yc.english.read.model.engin.CoursePlayEngin;

import java.util.List;

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
    public void getCourseList(int currentPage, int pageCount) {
        Subscription subscribe = mEngin.getCourseList(currentPage, pageCount).subscribe(new Subscriber<List<EnglishCourseInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<EnglishCourseInfo> courseInfoList) {
                if(courseInfoList != null){
                    mView.showCourseListData(courseInfoList);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
