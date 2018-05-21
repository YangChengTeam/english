package com.yc.junior.english.group.presenter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.R;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.common.GroupApp;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.constant.GroupConstant;
import com.yc.junior.english.group.contract.GroupApplyJoinContract;
import com.yc.junior.english.group.model.bean.ClassInfo;
import com.yc.junior.english.group.model.bean.ClassInfoWarpper;
import com.yc.junior.english.group.model.bean.GroupApplyInfo;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.group.model.bean.StudentInfoWrapper;
import com.yc.junior.english.group.rong.models.CodeSuccessResult;
import com.yc.junior.english.group.rong.util.RongIMUtil;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.hepler.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by wanglin  on 2017/8/2 16:40.
 */

public class GroupApplyJoinPresenter extends BasePresenter<BaseEngin, GroupApplyJoinContract.View> implements GroupApplyJoinContract.Presenter {
    public GroupApplyJoinPresenter(Context context, GroupApplyJoinContract.View view) {
        super(context, view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    /**
     * 申请加入班群
     *
     * @param classInfo
     */
    @Override
    public void applyJoinGroup(final ClassInfo classInfo) {
        String groupName = "班群";
        String masterName = "群主";
        if ("1".equals(classInfo.getType())) {
            groupName = "公会";
            masterName = "会主";
        }
        if (TextUtils.isEmpty(classInfo.getGroupId() + "")) {
            ToastUtils.showShort("请输入" + groupName + "号");
            return;
        }
        mView.showLoadingDialog("正在申请加入" + groupName + "，请稍候");
        final String finalGroupName = groupName;
        final String finalMasterName = masterName;
        Subscription subscription = EngineUtils.applyJoinGroup(mContext, UserInfoHelper.getUserInfo().getUid(), classInfo.getGroupId() + "").subscribe(new Subscriber<ResultInfo<GroupApplyInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
                LogUtils.e("onError: " + e.getMessage());
            }

            @Override
            public void onNext(final ResultInfo<GroupApplyInfo> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        if (stringResultInfo != null && stringResultInfo.data != null) {
                            GroupApplyInfo applyInfo = stringResultInfo.data;
                            int type = Integer.parseInt(applyInfo.getVali_type());

                            if (type == GroupConstant.CONDITION_ALL_ALLOW) {

                                ToastUtils.showShort(String.format(mContext.getString(R.string.congratulation_join), finalGroupName));
                                RongIMUtil.insertMessage("欢迎" + UserInfoHelper.getUserInfo().getNickname() + "加入本" + finalGroupName, classInfo.getClass_id());
                                RxBus.get().post(BusAction.GROUP_LIST, "from groupjoin");
                                setMode(classInfo);
                                StudentInfo studentInfo = new StudentInfo();
                                studentInfo.setUser_id(applyInfo.getUser_id());
                                studentInfo.setClass_id(applyInfo.getClass_id());
                                if (classInfo.getIs_allow_talk() == 0) {
                                    addForbidMember(studentInfo);
                                }

                            } else if (type == GroupConstant.CONDITION_VERIFY_JOIN) {

                                ToastUtils.showShort(String.format(mContext.getString(R.string.commit_apply_join), finalGroupName, finalMasterName));
                            }
                            mView.finish();
                        }
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 根据群号查找群
     *
     * @param sn
     */

    @Override
    public void queryGroupById(Context context, String id, String sn) {

        Subscription subscription = EngineUtils.queryGroupById(context, id, sn).subscribe(new Subscriber<ResultInfo<ClassInfoWarpper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoWarpper> stringResultInfo) {
                handleResultInfo(stringResultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.showGroup(stringResultInfo.data.getInfo());
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getMemberList(String sn, String status, int page, int page_size, String master_id, String type) {
        Subscription subscription = EngineUtils.getMemberList(mContext, sn, page, page_size, status, master_id, type).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ResultInfo<StudentInfoWrapper>>() {
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
                        StudentInfoWrapper data = studentInfoWrapperResultInfo.data;
                        if (data != null && data.getList() != null && data.getList().size() > 0) {
                            List<UserInfo> list = new ArrayList<>();
                            for (StudentInfo studentInfo : data.getList()) {
                                list.add(new UserInfo(studentInfo.getUser_id(), studentInfo.getNick_name(), Uri.parse(studentInfo.getFace())));
                            }
                            mView.showMemberList(list, data.getList());
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void addForbidMember(StudentInfo studentInfo) {
        Subscription subscription = EngineUtils.addForbidMember(studentInfo.getUser_id(), studentInfo.getClass_id(), "0").observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CodeSuccessResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                LogUtils.e(e.getMessage());
            }

            @Override
            public void onNext(CodeSuccessResult codeSuccessResult) {
                if (codeSuccessResult != null && codeSuccessResult.getCode() == 200) {

                }

            }
        });
        mSubscriptions.add(subscription);
    }

    private void setMode(ClassInfo classInfo) {
        if (classInfo.getType().equals("1")) {//公会
            GroupApp.setMyExtensionModule(false, false);
        } else {
            GroupApp.setMyExtensionModule(false, true);
        }
        RongIM.getInstance().startGroupChat(mContext, classInfo.getClass_id(), classInfo.getClassName());
    }
}
