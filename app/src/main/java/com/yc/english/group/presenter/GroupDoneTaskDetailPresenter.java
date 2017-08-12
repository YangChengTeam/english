package com.yc.english.group.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoneTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.model.engin.GroupDoneTaskDetailEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.group.view.provider.CustomMessage;

import java.io.File;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/10 15:23.
 */

public class GroupDoneTaskDetailPresenter extends BasePresenter<GroupDoneTaskDetailEngine, GroupDoneTaskDetailContract.View> implements GroupDoneTaskDetailContract.Presenter {
    public GroupDoneTaskDetailPresenter(Context context, GroupDoneTaskDetailContract.View view) {
        super(context, view);
        mEngin = new GroupDoneTaskDetailEngine(context);
    }

    @Override
    public void getDoneTaskDetail(Context context,String id, String user_id) {
        Subscription subscription = EngineUtils.getDoneTaskDetail(context,id, user_id).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showDoneTaskDetail(stringResultInfo.data.getInfo());
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
                        mView.showPublishTaskDetail(taskInfoWrapperResultInfo.data.getInfo());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void updateDoTask(String id, String user_id, String desp, String imgs, String voices, String docs) {
        Subscription subscription = mEngin.updateDoTask(id, user_id, desp, imgs, voices, docs).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        sendDoWorkMessage(stringResultInfo.data.getInfo());
                        mView.finish();
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void uploadFile(Context context, File file, String fileName, String name) {
        Subscription subscription = EngineUtils.uploadFile(context, file, fileName, name).subscribe(new Subscriber<ResultInfo<TaskUploadInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<TaskUploadInfo> taskUploadInfoResultInfo) {

            }
        });
        mSubscriptions.add(subscription);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    private void sendDoWorkMessage(TaskInfo taskInfo) {
        CustomMessage customMessage = CustomMessage.obtain("家庭作业", taskInfo.getDesp(), "");

        customMessage.setExtra(JSONObject.toJSONString(taskInfo));


        Message message = Message.obtain(taskInfo.getClass_id(), Conversation.ConversationType.GROUP, customMessage);

        RongIM.getInstance().sendMessage(message, "app:custom", null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                LogUtils.e(message);
            }

            @Override
            public void onSuccess(Message message) {
                LogUtils.e(message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                LogUtils.e(message + "---" + errorCode);
            }
        });

    }

}
