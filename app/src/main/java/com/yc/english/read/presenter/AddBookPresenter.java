package com.yc.english.read.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.read.contract.AddBookContract;
import com.yc.english.read.model.domain.BookInfo;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.CourseVersionInfoList;
import com.yc.english.read.model.domain.GradeInfo;
import com.yc.english.read.model.domain.GradeInfoList;
import com.yc.english.read.model.engin.BookEngin;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by admin on 2017/8/7.
 */

public class AddBookPresenter extends BasePresenter<BookEngin, AddBookContract.View> implements AddBookContract.Presenter {

    public AddBookPresenter(Context context, AddBookContract.View view) {
        super(context, view);
        mEngine = new BookEngin(context);
    }

    @Override
    public void getGradeListFromLocal() {
        List<GradeInfo> gradeInfos = mEngine.getGradeListFromDB();
        mView.showGradeListData(gradeInfos);
    }

    @Override
    public void gradeList() {
        mView.showLoading();
        Subscription subscribe = mEngine.gradeList(mContext).subscribe(new Subscriber<ResultInfo<GradeInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<GradeInfoList> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null && resultInfo.data.getList().size() > 0) {
                            mView.showGradeListData(resultInfo.data.list);
                            mView.hide();
                        } else {
                            mView.showNoData();
                        }
                    }
                });

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void getCVListByGradeId(String gradeId, String partType) {
        Subscription subscribe = mEngine.getCVListByGradeId(mContext, gradeId, partType).subscribe(new Subscriber<ResultInfo<CourseVersionInfoList>>() {
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

                    mEngine.bookList(0, 0, 1).subscribe(new Subscriber<ArrayList<BookInfo>>() {
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
