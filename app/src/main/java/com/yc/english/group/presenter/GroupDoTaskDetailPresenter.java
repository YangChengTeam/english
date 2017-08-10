package com.yc.english.group.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupDoTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.model.engin.GroupDoTaskDetailEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.group.view.provider.CustomMessage;

import java.io.File;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/8 16:09.
 */

public class GroupDoTaskDetailPresenter extends BasePresenter<GroupDoTaskDetailEngine, GroupDoTaskDetailContract.View> implements GroupDoTaskDetailContract.Presenter {
    public GroupDoTaskDetailPresenter(Context context, GroupDoTaskDetailContract.View view) {
        super(view);
        mEngin = new GroupDoTaskDetailEngine(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

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
                        mView.showTaskDetail(taskInfoWrapperResultInfo.data.getInfo());
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
            public void onNext(final ResultInfo<TaskUploadInfo> taskUploadInfoResultInfo) {
                handleResultInfo(taskUploadInfoResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showUploadResult(taskUploadInfoResultInfo.data);
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void doTask(String class_id, String user_id, String task_id, String desp, String imgs, String voices, String docs) {
        if (TextUtils.isEmpty(desp) && TextUtils.isEmpty(imgs) && TextUtils.isEmpty(voices) && TextUtils.isEmpty(docs)) {
            TipsHelper.tips(mContext, "请填写要完成的作业");
            return;
        }
        Subscription subscription = mEngin.doTask(class_id, user_id, task_id, desp, imgs, voices, docs).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
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
                        sendDoWorkMessage(taskInfoWrapperResultInfo.data.getInfo());
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
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
