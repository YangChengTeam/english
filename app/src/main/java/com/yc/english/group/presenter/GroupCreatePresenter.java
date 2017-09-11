package com.yc.english.group.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.dao.ClassInfoDao;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupCreateContract;

import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.engin.GroupCreateEngine;

import java.util.List;

import io.rong.imkit.RongIM;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/24 18:38.
 */

public class GroupCreatePresenter extends BasePresenter<GroupCreateEngine, GroupCreateContract.View> implements GroupCreateContract.Presenter {
    private ClassInfoDao classInfoDao;

    public GroupCreatePresenter(Context context, GroupCreateContract.View view) {
        super(context, view);
        mEngin = new GroupCreateEngine(context);
        classInfoDao = GroupApp.getmDaoSession().getClassInfoDao();
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void createGroup(String user_id, String groupName, String face, String type) {
        if (TextUtils.isEmpty(groupName)) {
            ToastUtils.showShort("请输入班级名称");
            return;
        }
        mView.showLoadingDialog("正在创建班级，请稍候");
        Subscription subscription = mEngin.createGroup(user_id, groupName, face, type).subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
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
                        RxBus.get().post(BusAction.GROUP_LIST, "create group");
                        RongIM.getInstance().startGroupChat(mContext, classInfo.data.getInfo().getClass_id(), classInfo.data.getInfo().getClassName());
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }


    private boolean isExist;

    private void saveGroup(final ClassInfo info) {
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                List<ClassInfo> classInfos = classInfoDao.queryBuilder().build().list();
                if (classInfos != null && classInfos.size() > 0) {
                    for (ClassInfo classInfo : classInfos) {
                        if (classInfo.getGroupId() == info.getGroupId()) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        classInfoDao.save(info);
                    }
                } else {
                    classInfoDao.save(info);
                }
                RxBus.get().post(BusAction.GROUP_LIST, "create group");
            }
        });
        RongIM.getInstance().startGroupChat(mContext, info.getClass_id(), info.getClassName());
        mView.finish();
    }

}
