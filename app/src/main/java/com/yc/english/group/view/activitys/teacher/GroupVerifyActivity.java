package com.yc.english.group.view.activitys.teacher;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupApplyVerifyContract;
import com.yc.english.group.listener.OnItemClickListener;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupApplyVerifyPresenter;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.view.adapter.GroupVerifyAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/29 11:58.
 * 好友验证
 */

public class GroupVerifyActivity extends FullScreenActivity<GroupApplyVerifyPresenter> implements GroupApplyVerifyContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private GroupVerifyAdapter adapter;
    private String uid;

    @Override
    public void init() {
        mPresenter = new GroupApplyVerifyPresenter(this, this);
        mToolbar.setTitle(getString(R.string.friend_verify));
        mToolbar.showNavigationIcon();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupVerifyAdapter(this, null);
        recyclerView.setAdapter(adapter);
        initData();

    }


    private void initData() {

        uid = UserInfoHelper.getUserInfo().getUid();
        mPresenter.getMemberList(this, "", "0", uid);

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_verify_friend;
    }


    @Override
    public void showVerifyList(List<StudentInfo> list) {
        adapter.setData(list);
        initListener();
    }

    @Override
    public void showApplyResult(String data) {
        mHolder.setVisible(R.id.m_tv_accept, false);
        mHolder.setVisible(R.id.m_tv_already_add, true);
        adapter.notifyDataSetChanged();

        String[] userIds = new String[]{mStudentInfo.getUser_id()};
        String groupId = mStudentInfo.getSn();
        String groupName = mStudentInfo.getClass_name();

        mPresenter.joinGroup(mStudentInfo.getUser_id(), groupId, groupName);

//        ImUtils.joinGroup(userIds, groupId, groupName).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CodeSuccessResult>() {
//            @Override
//            public void call(CodeSuccessResult codeSuccessResult) {
//                if (codeSuccessResult.getCode() == 200) {//加入成功
//                    ToastUtils.showShort("加入成功");
////                    mView.startGroupChat(groupId, groupName);
////                    ClassInfo info = new ClassInfo("", groupName, users.size() + "", Integer.parseInt(groupId));
////                    classInfoDao.insert(info);
//                    RxBus.get().post(BusAction.GROUPLIST, "from groupjoin");
//                }
//            }
//        });

    }

    private BaseViewHolder mHolder;
    private StudentInfo mStudentInfo;

    private void initListener() {
        adapter.setOnItemClickListener(new OnItemClickListener<StudentInfo>() {
            @Override
            public void onItemClick(BaseViewHolder holder, int position, StudentInfo studentInfo) {
                mPresenter.acceptApply(studentInfo.getClass_id(), uid, new String[]{studentInfo.getUser_id()});
                mHolder = holder;
                mStudentInfo = studentInfo;
            }
        });

    }
}
