package com.yc.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.contract.GroupGetForbidMemberContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.rong.models.ListGagGroupUserResult;
import com.yc.english.group.utils.EngineUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/8/31 09:18.
 */

public class GroupGetForbidMemberPresenter extends BasePresenter<BaseEngin, GroupGetForbidMemberContract.View> implements GroupGetForbidMemberContract.Presenter {
    public GroupGetForbidMemberPresenter(Context context, GroupGetForbidMemberContract.View view) {
        super(context, view);

    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getMemberList(GroupInfoHelper.getGroupInfo().getId(),1,1000, "1", "", GroupInfoHelper.getClassInfo().getType());

    }

    @Override
    public void getMemberList(final String sn, int page,int page_size, String status, String master_id, String type) {
        mView.showLoading();
        Subscription subscription = EngineUtils.getMemberList(mContext, sn,page,page_size,  status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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
                            mView.hideStateView();
                            lisGagUser(sn, data.getList());
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
    public void lisGagUser(String groupId, final List<StudentInfo> list) {
        Subscription subscription = EngineUtils.lisGagUser(groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ListGagGroupUserResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ListGagGroupUserResult listGagGroupUserResult) {
                if (listGagGroupUserResult.getCode() == 200) {
                    mView.showGagUserResult(listGagGroupUserResult.getUsers(), list);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
