package com.yc.english.union.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.union.contract.UnionListContract;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/9/7 12:15.
 */

public class UnionListPresenter extends BasePresenter<BaseEngin, UnionListContract.View> implements UnionListContract.Presenter {
    public UnionListPresenter(Context context, UnionListContract.View view) {
        super(context, view);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getUnionList(String type, String flag, int page, int page_size) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getUnionList(mContext, type, flag, page, page_size).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfo) {
                ResultInfoHelper.handleResultInfo(classInfo, new ResultInfoHelper.Callback() {
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
                        mView.hideStateView();
                        mView.showUnionList(classInfo.data.getList());
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }


    public void getMemberList(Context context, String class_id, String status, String master_id) {
        Subscription subscription = EngineUtils.getMemberList(context, class_id, status, master_id, "").subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> studentInfoWrapperResultInfo) {
                handleResultInfo(studentInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showMemberList(studentInfoWrapperResultInfo.data.getList());

                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

}
