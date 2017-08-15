package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupScoreTaskContract;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.engin.GroupScoreTaskEngine;
import com.yc.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/10 21:21.
 */

public class GroupScoreTaskPresenter extends BasePresenter<GroupScoreTaskEngine, GroupScoreTaskContract.View> implements GroupScoreTaskContract.Presenter {
    public GroupScoreTaskPresenter(Context context, GroupScoreTaskContract.View view) {
        super(context, view);
        mEngin = new GroupScoreTaskEngine(context);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getDoneTaskDetail(Context context, String id, String user_id) {
        Subscription subscription = EngineUtils.getDoneTaskDetail(context, id, user_id).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> taskInfoWrapperResultInfo) {
                handleResultInfo(taskInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showDoneTaskInfo(taskInfoWrapperResultInfo.data.getInfo());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getPublishTaskDetail(Context context, String task_id, String class_id, String user_id) {
        Subscription subscription = EngineUtils.getPublishTaskDetail(context, task_id, class_id, user_id).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> taskInfoWrapperResultInfo) {
                handleResultInfo(taskInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showPublishTaskInfo(taskInfoWrapperResultInfo.data.getInfo());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void taskScore(final Context context, String id, String score) {
        Subscription subscription = mEngin.taskScore(id, score).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showScoreResult();


                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
