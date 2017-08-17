package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoTaskListContract;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.english.group.model.engin.GroupDoTaskListEngine;
import com.yc.english.group.utils.EngineUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/8/8 16:01.
 */

public class GroupDoTaskListPresenter extends BasePresenter<GroupDoTaskListEngine, GroupDoTaskListContract.View> implements GroupDoTaskListContract.Presenter {
    public GroupDoTaskListPresenter(Context context, GroupDoTaskListContract.View view) {
        super(context, view);
        mEngin = new GroupDoTaskListEngine(context);
    }

    @Override
    public void getDoTaskList(String class_id, String user_id) {
        mView.showLoading();
        Subscription subscription = mEngin.getDoTaskList(class_id, user_id).subscribe(new Subscriber<ResultInfo<TaskAllInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<TaskAllInfoWrapper> taskDoneInfoWrapperResultInfo) {

                handleResultInfo(taskDoneInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {

                        if (taskDoneInfoWrapperResultInfo.data != null) {
                            List<TaskAllInfoWrapper.TaskAllInfo> list = taskDoneInfoWrapperResultInfo.data.getList();
                            if (list != null && list.size() > 0) {
                                mView.showDoneTaskResult(list);
                                mView.hideStateView();
                            } else {
                                mView.showNoData();
                            }
                        } else {
                            mView.showNoData();
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getPublishTaskList(String publisher, String class_id) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getPublishTaskList(mContext,publisher, class_id).subscribe(new Subscriber<ResultInfo<TaskAllInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<TaskAllInfoWrapper> taskDoneInfoWrapperResultInfo) {
                ResultInfoHelper.handleResultInfo(taskDoneInfoWrapperResultInfo, new ResultInfoHelper.Callback() {
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
                        if (taskDoneInfoWrapperResultInfo.data != null) {
                            List<TaskAllInfoWrapper.TaskAllInfo> list = taskDoneInfoWrapperResultInfo.data.getList();
                            if (list != null && list.size() > 0) {
                                mView.showDoneTaskResult(list);
                                mView.hideStateView();
                            } else {
                                mView.showNoData();
                            }
                        } else {
                            mView.showNoData();
                        }
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }
}
