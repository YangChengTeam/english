package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupPublishTaskDetailContract;
import com.yc.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.english.group.model.bean.StudentLookTaskInfo;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.engin.GroupPublishTaskDetailEngine;
import com.yc.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 16:31.
 * 老师查看发布作业详情
 */

public class GroupPublishTaskDetailPresenter extends BasePresenter<GroupPublishTaskDetailEngine, GroupPublishTaskDetailContract.View> implements GroupPublishTaskDetailContract.Presenter {
    public GroupPublishTaskDetailPresenter(Context context, GroupPublishTaskDetailContract.View view) {
        super(context, view);
        mEngin = new GroupPublishTaskDetailEngine(context);
    }

    @Override
    public void getPublishTaskDetail(Context context, String id, String class_id, String user_id) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getPublishTaskDetail(context, id, class_id, user_id).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> taskPublishDetailInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(taskPublishDetailInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        hideStateView();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        hideStateView();
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.showPublishTaskDetail(taskPublishDetailInfoResultInfo.data.getInfo());
                        hideStateView();
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getIsReadTaskList(String class_id, String task_id) {
        Subscription subscription = mEngin.getIsReadTaskList(class_id, task_id).subscribe(new Subscriber<ResultInfo<StudentLookTaskInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<StudentLookTaskInfo> studentInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(studentInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        hideStateView();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        hideStateView();
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.showIsReadMemberList(studentInfoResultInfo.data.getList());
                        hideStateView();
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getIsFinishTaskList(String class_id, String task_id) {
        Subscription subscription = mEngin.getIsFinishTaskList(class_id, task_id).subscribe(new Subscriber<ResultInfo<StudentFinishTaskInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<StudentFinishTaskInfo> studentTaskInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(studentTaskInfoResultInfo, new
                        ResultInfoHelper.Callback() {
                            @Override
                            public void resultInfoEmpty(String message) {
                                hideStateView();
                            }

                            @Override
                            public void resultInfoNotOk(String message) {
                                hideStateView();
                            }

                            @Override
                            public void reulstInfoOk() {
                                mView.showIsFinishMemberList(studentTaskInfoResultInfo.data.getList());
                                hideStateView();
                            }
                        });


            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    private int count;

    private void hideStateView() {
        count++;
        if (count >= 2) {
            mView.hideStateView();
            count = 0;
        }

    }
}
