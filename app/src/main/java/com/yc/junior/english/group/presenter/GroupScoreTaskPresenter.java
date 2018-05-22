package com.yc.junior.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.contract.GroupScoreTaskContract;
import com.yc.junior.english.group.model.bean.TaskInfoWrapper;
import com.yc.junior.english.group.model.engin.GroupScoreTaskEngine;
import com.yc.junior.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
                ResultInfoHelper.handleResultInfo(taskInfoWrapperResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.showDoneTaskInfo(taskInfoWrapperResultInfo.data.getInfo());

                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getPublishTaskDetail(Context context, String task_id, String class_id, String user_id) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getPublishTaskDetail(context, task_id, class_id, user_id).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> taskInfoWrapperResultInfo) {
                ResultInfoHelper.handleResultInfo(taskInfoWrapperResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.hideStateView();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.hideStateView();
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.showPublishTaskInfo(taskInfoWrapperResultInfo.data.getInfo());
                        mView.hideStateView();
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void taskScore(final Context context, String id, String score) {
        mView.showLoadingDialog("正在打分，请稍候...");
        Subscription subscription = mEngin.taskScore(id, score).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        TipsHelper.tips(mContext, "作业打分成功");
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


}
