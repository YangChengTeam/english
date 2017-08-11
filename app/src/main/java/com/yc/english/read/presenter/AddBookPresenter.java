package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.read.contract.AddBookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.CourseVersionInfoList;
import com.yc.english.read.model.domain.GradeInfoList;
import com.yc.english.read.model.engin.BookEngin;

import java.util.ArrayList;
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
        Subscription subscribe = mEngin.gradeList(mContext).subscribe(new Subscriber<ResultInfo<GradeInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<GradeInfoList> resultInfo) {
                if (resultInfo != null) {
                    mView.showGradeListData(resultInfo.data);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void getCVListByGradeId(String gradeId, String partType) {
        Subscription subscribe = mEngin.getCVListByGradeId(mContext, gradeId, partType).subscribe(new Subscriber<ResultInfo<CourseVersionInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<CourseVersionInfoList> resultInfo) {
                if (resultInfo != null) {

                    final List<CourseVersionInfo> cList = (ArrayList) resultInfo.data.list;

                    mEngin.bookList(0, 0, 1).subscribe(new Subscriber<ArrayList<BookInfo>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ArrayList<BookInfo> bookInfos) {
                            for (CourseVersionInfo cInfo : cList) {
                                for (BookInfo bInfo : bookInfos) {
                                    if (cInfo.getBookId().equals(bInfo.getBookId())) {
                                        cInfo.setAdd(false);
                                        break;
                                    }
                                }
                            }
                        }
                    });

                    mView.showCVListData(cList);
                }
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
