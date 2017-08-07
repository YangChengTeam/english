package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupTaskPublishContract;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.engin.GroupTaskPublishEngine;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/7 10:41.
 */

public class GroupTaskPublishPresenter extends BasePresenter<GroupTaskPublishEngine, GroupTaskPublishContract.View> implements GroupTaskPublishContract.Presenter {
    public GroupTaskPublishPresenter(Context context, GroupTaskPublishContract.View view) {
        super(context, view);
        mEngin = new GroupTaskPublishEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        String uid = UserInfoHelper.getUserInfo().getUid();
        getGroupList(mContext, uid, "1");
    }

    @Override
    public void publishTask(String class_ids, String publisher, String desp, byte[] imges, byte[] voices, byte[] docs) {
        mView.showLoadingDialog("正在发布作业，请稍候！");
        Subscription subscription = mEngin.publishTask(class_ids, publisher, desp, imges, voices, docs).subscribe(new Subscriber<ResultInfo<String>>() {
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
                        mView.showTaskDetail();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getGroupInfo(Context context, String id) {
        Subscription subscription = EngineUtils.queryGroupById(context, id, "").subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoWarpper> classInfoWarpperResultInfo) {

                handleResultInfo(classInfoWarpperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showGroupInfo(classInfoWarpperResultInfo.data.getInfo());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void uploadFile(File file) {
        Subscription subscription = mEngin.uploadFile(file).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
        mSubscriptions.add(subscription);
    }

    private void getGroupList(Context context, String user_id, String is_admin) {
        Subscription subscription = EngineUtils.getMyGroupList(context, user_id, is_admin).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override

            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfoListResultInfo) {
                handleResultInfo(classInfoListResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showMyGroupList(classInfoListResultInfo.data.getList());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

}
