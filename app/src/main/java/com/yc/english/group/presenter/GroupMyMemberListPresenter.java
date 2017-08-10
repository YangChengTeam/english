package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupMyMemberListContract;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.utils.EngineUtils;

import java.util.List;

import io.rong.imlib.IRongCallback;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/3 16:46.
 */

public class GroupMyMemberListPresenter extends BasePresenter<BaseEngin, GroupMyMemberListContract.View> implements GroupMyMemberListContract.Presenter {
    public GroupMyMemberListPresenter(GroupMyMemberListContract.View view) {
        super(view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void getMemberList(Context context, String class_id, String status, String master_id) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMemberList(context, class_id, status, master_id).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> studentInfoWrapperResultInfo) {
                ResultInfoHelper.handleResultInfo(studentInfoWrapperResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoData();
                    }

                    @Override
                    public void reulstInfoOk() {

                        mView.showMemberList(studentInfoWrapperResultInfo.data.getList());
                        mView.hideStateView();
                    }
                });

            }
        });

        mSubscriptions.add(subscription);
    }

    public void queryGroupById(Context context, String id, String sn) {
        Subscription subscription = EngineUtils.queryGroupById(context, id, sn).subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
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
}
