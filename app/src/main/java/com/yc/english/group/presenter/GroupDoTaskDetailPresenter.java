package com.yc.english.group.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.helper.ResultInfoHelper;
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
        super(context, view);
        mEngin = new GroupDoTaskDetailEngine(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

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
                        TaskInfo info = taskInfoWrapperResultInfo.data.getInfo();
                        mView.showTaskDetail(info);
                        mView.hideStateView();
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void uploadFile(Context context, File file, String fileName, String name) {
        mView.showLoadingDialog("正在上传...");
        Subscription subscription = EngineUtils.uploadFile(context, file, fileName, name).subscribe(new Subscriber<ResultInfo<TaskUploadInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
                TipsHelper.tips(mContext, HttpConfig.NET_ERROR);
            }

            @Override
            public void onNext(final ResultInfo<TaskUploadInfo> taskUploadInfoResultInfo) {

                handleResultInfo(taskUploadInfoResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showUploadResult(taskUploadInfoResultInfo.data);
                        mView.showFile();
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
        mView.showLoadingDialog("正在提交...");
        Subscription subscription = mEngin.doTask(class_id, user_id, task_id, desp, imgs, voices, docs).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
            @Override
            public void onCompleted() {
                mView.hideStateView();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideStateView();
            }

            @Override
            public void onNext(final ResultInfo<TaskInfoWrapper> taskInfoWrapperResultInfo) {

                handleResultInfo(taskInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        TaskInfoWrapper data = taskInfoWrapperResultInfo.data;
                        if (data != null) {
                            sendDoWorkMessage(data.getInfo());
                            mView.finish();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getDoneTaskDetail(Context context, String id, String user_id) {

        Subscription subscription = EngineUtils.getDoneTaskDetail2(context, id, user_id).subscribe(new Subscriber<ResultInfo<TaskInfoWrapper>>() {
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
                        mView.showDoneWorkResult(taskInfoWrapperResultInfo.data.getInfo());
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }


    private void sendDoWorkMessage(TaskInfo taskInfo) {
        String desp = taskInfo.getDesp();
        if (TextUtils.isEmpty(desp)) {
            desp = "点击查看详情";
        }
        CustomMessage customMessage = CustomMessage.obtain("我的作业", desp, "");

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
