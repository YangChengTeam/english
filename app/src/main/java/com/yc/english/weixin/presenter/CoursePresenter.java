package com.yc.english.weixin.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.view.IView;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.model.engin.IndexEngin;
import com.yc.english.weixin.contract.CourseContract;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.model.domain.CourseInfoWrapper;
import com.yc.english.weixin.model.engin.WeixinEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CoursePresenter extends BasePresenter<WeixinEngin, CourseContract.View> implements CourseContract.Presenter {
    public CoursePresenter(Context context, CourseContract.View iView) {
        super(context, iView);
        mEngin = new WeixinEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getWeiXinList(String type_id,final String page,
                              String page_size) {
        if(page.equals("1") ) {
            mView.showLoading();
        }
        Subscription subscription = mEngin.getWeixinList(type_id, page, page_size).subscribe(new Subscriber<ResultInfo<CourseInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(page.equals("1") ) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<CourseInfoWrapper> courseInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(courseInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if(page.equals("1") ) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if(page.equals("1") ) {
                            mView.showNoData();
                        }
                        mView.fail();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (courseInfoResultInfo.data != null && courseInfoResultInfo.data.getList() != null &&
                                courseInfoResultInfo.data.getList().size() > 0) {
                            mView.showWeixinList(courseInfoResultInfo.data.getList());
                            if(page.equals("1") ) {
                                mView.hideStateView();
                            }
                        } else {
                            if(page.equals("1") ) {
                                mView.showNoData();
                            }
                            mView.end();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
