package com.yc.english.read.presenter;

import android.content.Context;

import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.AddBookContract;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;
import com.yc.english.read.model.engin.BookEngin;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/7.
 */

public class AddBookPresenter extends BasePresenter<BookEngin, AddBookContract.View> implements AddBookContract.Presenter {

    public AddBookPresenter(Context context, AddBookContract.View view) {
        super(context, view);
        mEngin = new BookEngin(context);
    }
    
    @Override
    public void gradeList() {
        Subscription subscribe = mEngin.gradeList(mContext).subscribe(new Subscriber<List<GradeInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<GradeInfo> gradeInfos) {

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void getCVListByGradeId(String gradeId) {
        Subscription subscribe = mEngin.getCVListByGradeId(mContext,gradeId).subscribe(new Subscriber<List<CourseVersionInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<CourseVersionInfo> courseVersionInfos) {

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
