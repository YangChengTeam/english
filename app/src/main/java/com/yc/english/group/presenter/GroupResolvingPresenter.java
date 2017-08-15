package com.yc.english.group.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupResolvingContract;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.model.engin.GroupResolvingEngine;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/4 15:47.
 */

public class GroupResolvingPresenter extends BasePresenter<GroupResolvingEngine, GroupResolvingContract.View> implements GroupResolvingContract.Presenter {
    public GroupResolvingPresenter(Context context, GroupResolvingContract.View view) {
        super(context, view);
        mEngin = new GroupResolvingEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void resolvingGroup(String class_id, String master_id) {

        mView.showLoadingDialog("解算班群中，请稍候！");

        Subscription subscription = mEngin.resolvingGroup(class_id, master_id).subscribe(new Subscriber<ResultInfo<RemoveGroupInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<RemoveGroupInfo> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {

                        mView.showResolvingResult();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void queryGroupById(Context context, String id) {
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
                        mView.showClassInfo(classInfoWarpperResultInfo.data.getInfo());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void changeGroupInfo(Context context, String class_id, String name, String face, String vali_type) {
        mView.showLoadingDialog("正在修改");
        Subscription subscription = EngineUtils.changeGroupInfo(context, class_id, name, face, vali_type).subscribe(new Subscriber<ResultInfo<RemoveGroupInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<RemoveGroupInfo> removeGroupInfoResultInfo) {
                handleResultInfo(removeGroupInfoResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        RxBus.get().post(BusAction.GROUPLIST, "change face");
                        mView.showChangeGroupInfo(removeGroupInfoResultInfo.data);
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


}
