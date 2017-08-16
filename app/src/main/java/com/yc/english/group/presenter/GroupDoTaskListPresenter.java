package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoTaskListContract;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.english.group.model.engin.GroupDoTaskListEngine;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

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
                mView.hideStateView();
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
                        List<TaskAllInfoWrapper.TaskAllInfo> list = taskDoneInfoWrapperResultInfo.data.getList();
                        if (list != null && list.size() > 0) {
                            mView.showDoneTaskResult(list);
                            mView.hideStateView();
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
