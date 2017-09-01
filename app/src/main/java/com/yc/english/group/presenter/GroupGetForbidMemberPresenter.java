package com.yc.english.group.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupGetForbidMemberContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.engin.GroupGetForbidMemEngine;
import com.yc.english.group.rong.models.ListGagGroupUserResult;
import com.yc.english.group.utils.EngineUtils;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/8/31 09:18.
 */

public class GroupGetForbidMemberPresenter extends BasePresenter<GroupGetForbidMemEngine, GroupGetForbidMemberContract.View> implements GroupGetForbidMemberContract.Presenter {
    public GroupGetForbidMemberPresenter(Context context, GroupGetForbidMemberContract.View view) {
        super(context, view);
        mEngin = new GroupGetForbidMemEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getMemberList(GroupInfoHelper.getGroupInfo().getId(), "1", "", GroupInfoHelper.getClassInfo().getFlag());

    }

    @Override
    public void getMemberList(final String sn, String status, String master_id, String flag) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMemberList(mContext, sn, status, master_id, flag).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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
                        StudentInfoWrapper data = studentInfoWrapperResultInfo.data;
                        if (data != null && data.getList() != null && data.getList().size() > 1) {
                            mView.showMemberList(data.getList());
                            mView.hideStateView();
                            lisGagUser(sn);
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
    public void lisGagUser(String groupId) {
        Subscription subscription = mEngin.lisGagUser(groupId).subscribe(new Subscriber<ListGagGroupUserResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ListGagGroupUserResult listGagGroupUserResult) {
                if (listGagGroupUserResult.getCode() == 200) {
                    mView.showGagUserResult(listGagGroupUserResult.getUsers());
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
