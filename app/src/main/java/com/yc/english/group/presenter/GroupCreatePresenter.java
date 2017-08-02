package com.yc.english.group.presenter;

import android.content.Context;
import android.nfc.Tag;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupCreateContract;
import com.yc.english.group.dao.ClassInfoDao;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.engin.GroupCreateEngine;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/24 18:38.
 */

public class GroupCreatePresenter extends BasePresenter<GroupCreateEngine, GroupCreateContract.View> implements GroupCreateContract.Presenter {
    private ClassInfoDao classInfoDao;

    public GroupCreatePresenter(Context context, GroupCreateContract.View view) {
        super(view);
        mEngin = new GroupCreateEngine(context);
        classInfoDao = GroupApp.getmDaoSession().getClassInfoDao();
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

    }


    @Override
    public void createGroup(String user_id, String groupName, String face) {
        if (TextUtils.isEmpty(groupName)) {
            ToastUtils.showShort("请输入班级名称");
            return;
        }

        mView.showLoadingDialog("正在创建班级，请稍候");
        Subscription subscription = mEngin.createGroup(user_id, groupName, face).subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<ClassInfoWarpper> classInfo) {
                handleResultInfo(classInfo, new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.e(classInfo.data.toString());
                        createRongGroup(classInfo.data.getInfo().getGroupId(), classInfo.data.getInfo().getClassName(), classInfo.data.getInfo().getMaster_id());
                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }

    private boolean isExist;

    private void saveGroup(final int groupId, final String groupName, final String[] userIds, final String masterId) {
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            private ClassInfo info;

            @Override
            public void call(String s) {
                List<ClassInfo> classInfos = classInfoDao.queryBuilder().build().list();
                if (classInfos != null && classInfos.size() > 0) {
                    for (ClassInfo classInfo : classInfos) {
                        if (classInfo.getGroupId() == groupId) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        info = new ClassInfo("", groupName, userIds.length + "", groupId);
                        info.setMaster_id(masterId);
                        classInfoDao.insert(info);
                    }
                } else {
                    info = new ClassInfo("", groupName, userIds.length + "", groupId);
                    info.setMaster_id(masterId);
                    classInfoDao.insert(info);
                }
                RxBus.get().post(BusAction.GROUPLIST, "create group");
            }
        });
        mView.finish();
    }


    /**
     * 创建班级
     */
    private void createRongGroup(final int groupId, final String groupName,final String user_id) {
        final String[] userIds = new String[]{user_id};
        ImUtils.createGroup(userIds, groupId + "", groupName).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CodeSuccessResult>() {
                    @Override
                    public void call(CodeSuccessResult codeSuccessResult) {
                        if (codeSuccessResult.getCode() == 200) {
                            saveGroup(groupId, groupName, userIds,user_id);
                            mView.finish();
                        }
                    }
                });

    }
}
