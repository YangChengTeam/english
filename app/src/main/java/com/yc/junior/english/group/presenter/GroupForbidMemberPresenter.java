package com.yc.junior.english.group.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.contract.GroupForbidMemberContract;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.model.bean.RemoveGroupInfo;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.model.bean.StudentInfoWrapper;
import com.yc.junior.english.group.model.engin.GroupForbidMemberEngine;
import com.yc.junior.english.group.rong.models.CodeSuccessResult;
import com.yc.junior.english.group.rong.models.ListGagGroupUserResult;
import com.yc.junior.english.group.utils.EngineUtils;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/8/30 09:53.
 */

public class GroupForbidMemberPresenter extends BasePresenter<GroupForbidMemberEngine, GroupForbidMemberContract.View> implements GroupForbidMemberContract.Presenter {


    public GroupForbidMemberPresenter(Context context, GroupForbidMemberContract.View view) {
        super(context, view);
        mEngin = new GroupForbidMemberEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

        if (!forceUpdate) return;
        getMemberList(GroupInfoHelper.getGroupInfo().getId(), 1, 1000, "1", "", GroupInfoHelper.getClassInfo().getType());
    }

    @Override
    public void addForbidMember(final StudentInfo studentInfo, final String minute, final boolean allForbid) {
        mView.showLoadingDialog("添加禁言成员中，请稍候...");
        Subscription subscription = EngineUtils.addForbidMember(studentInfo.getUser_id(), studentInfo.getClass_id(), minute).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
                LogUtils.e(e.getMessage());
            }

            @Override
            public void onNext(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult != null && codeSuccessResult.getCode() == 200) {
                    mView.showForbidResult(studentInfo, allForbid);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void rollBackMember(final String[] userId, final String nickName, final String groupId, final int position, final boolean allForbid) {
        mView.showLoadingDialog("正在解禁用户，请稍候...");

        Subscription subscription = mEngin.rollBackMember(userId, groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult != null && codeSuccessResult.getCode() == 200) {
                    mView.showRollBackResult(userId, nickName, groupId, position, allForbid);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getMemberList(final String sn, int page, int page_size, String status, String master_id, String type) {
        Subscription subscription = EngineUtils.getMemberList(mContext, sn, page, page_size, status, master_id, type).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<StudentInfoWrapper> studentInfoWrapperResultInfo) {
                handleResultInfo(studentInfoWrapperResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (studentInfoWrapperResultInfo.data != null && studentInfoWrapperResultInfo.data.getList() != null) {
                            lisGagUser(sn, studentInfoWrapperResultInfo.data.getList());
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
                    mView.showLisGagUserResult(listGagGroupUserResult.getUsers(), list);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * @param class_id
     * @param is_allow_talk 禁言状态 0 开启禁言 1 解除禁言
     *                      自己开启禁言传递的参数和返回的结果一致，即is_allow_talk 为0，返回的也是0
     *                      自己关闭禁言传递的参数和返回的结果一致，即is_allow_talk 为1，返回的也是1
     *                      如果是后台开启禁言，自己手动关闭可能会出现传递参数和返回结果不一致，即即is_allow_talk为1,返回的为0
     * @param strs
     */
    public void changeGroupInfo(String class_id, final String is_allow_talk, final String[] strs) {
        Subscription subscription = EngineUtils.changeGroupInfo(mContext, class_id, "", "", "", is_allow_talk).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResultInfo<RemoveGroupInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<RemoveGroupInfo> removeGroupInfoResultInfo) {
                handleResultInfo(removeGroupInfoResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (removeGroupInfoResultInfo != null && removeGroupInfoResultInfo.code == HttpConfig.STATUS_OK && removeGroupInfoResultInfo.data != null) {

                            GroupInfoHelper.getClassInfo().setIs_allow_talk(removeGroupInfoResultInfo.data.getIs_allow_talk());
                            mView.showChangeInfo(removeGroupInfoResultInfo.data, is_allow_talk, strs);

                        }
//                        RxBus.get().post(BusAction.GROUP_LIST, "forbid");
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

}
