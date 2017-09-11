package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.helper.AvatarHelper;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupResolvingContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.presenter.GroupResolvingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wanglin  on 2017/7/26 17:32.
 */

public class GroupManagerActivity extends FullScreenActivity<GroupResolvingPresenter> implements GroupResolvingContract.View {

    @BindView(R.id.iv_group_image)
    ImageView ivGroupImage;
    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.tv_permission_check)
    TextView tvPermissionCheck;
    @BindView(R.id.m_tv_group_image)
    TextView mTvGroupImage;
    @BindView(R.id.m_tv_group_name)
    TextView mTvGroupName;
    @BindView(R.id.m_tv_group_transfer)
    TextView mTvGroupTransfer;
    @BindView(R.id.btn_resolving_group)
    Button btnResolvingGroup;
    private AlertDialog alertDialog;
    private int condition;

    @Override
    public void init() {
        mPresenter = new GroupResolvingPresenter(this, this);
        initData();
        mToolbar.showNavigationIcon();

        tvGroupName.setText(GroupInfoHelper.getGroupInfo().getName());

        condition = SPUtils.getInstance().getInt(GroupInfoHelper.getGroupInfo().getId(), -1);
        if (condition == -1) {
            condition = Integer.parseInt(GroupInfoHelper.getClassInfo().getVali_type());
        }
        setVerify_reslut(condition);
        GlideHelper.circleImageView(this, ivGroupImage, GroupInfoHelper.getClassInfo().getImageUrl(), R.mipmap.default_avatar);

    }

    private void initData() {
        if (GroupInfoHelper.getClassInfo().getType().equals("1")) {
            mTvGroupImage.setText(getString(R.string.union_image));
            mTvGroupName.setText(getString(R.string.union_name));
            mTvGroupTransfer.setText(getString(R.string.transfer_union));
            btnResolvingGroup.setText(getString(R.string.resolving_union));
            mToolbar.setTitle(getString(R.string.union_manager));

        } else {
            mToolbar.setTitle(getString(R.string.group_manager));
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_group_manager;
    }

    @OnClick({R.id.rl_group_image, R.id.rl_group_name, R.id.rl_group_delete_member,
            R.id.tv_permission_check, R.id.rl_group_check, R.id.rl_group_transfer, R.id.btn_resolving_group, R.id.rl_group_forbid})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_group_image:
                AvatarHelper.openAlbum(this);
                break;
            case R.id.rl_group_name:
                startActivity(new Intent(this, GroupChangeNameActivity.class));
                break;
            case R.id.rl_group_delete_member:
                startActivity(new Intent(this, GroupDeleteMemberActivity.class));
                break;
            case R.id.rl_group_check:
                intent = new Intent(this, GroupVerifyConditionActivity.class);
                intent.putExtra("valiType", condition);
                startActivityForResult(intent, 200);
                break;
            case R.id.rl_group_transfer:
                startActivity(new Intent(this, GroupTransferActivity.class));
                break;
            case R.id.btn_resolving_group:
                if (alertDialog == null) {
                    alertDialog = new AlertDialog(this);
                }
                alertDialog.setDesc("是否解散班群");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mPresenter.resolvingGroup(GroupInfoHelper.getGroupInfo().getId(), GroupInfoHelper.getClassInfo().getMaster_id());
                    }
                });
                alertDialog.show();
                break;
            case R.id.rl_group_forbid:
                startActivity(new Intent(this, GroupForbidTalkActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            condition = data.getIntExtra("condition", 0);
            setVerify_reslut(condition);
            return;
        }
        if (requestCode == 1) {
            AvatarHelper.uploadAvatar(this, new AvatarHelper.IAvatar() {
                @Override
                public void uploadAvatar(String image) {
                    mPresenter.changeGroupInfo(GroupManagerActivity.this, GroupInfoHelper.getGroupInfo().getId(), "", image, "");
                }
            }, requestCode, resultCode, data);
        }

    }

    private void setVerify_reslut(int code) {
        switch (code) {
            case GroupConstant.CONDITION_ALL_ALLOW:
                tvPermissionCheck.setText(getString(R.string.all_allow));
                break;
            case GroupConstant.CONDITION_ALL_FORBID:
                tvPermissionCheck.setText(getString(R.string.all_forbid));
                break;
            case GroupConstant.CONDITION_VERIFY_JOIN:
                tvPermissionCheck.setText(getString(R.string.verify_join));
                break;
        }
    }

    @Override
    public void showChangeGroupInfo(RemoveGroupInfo data) {
        GlideHelper.circleImageView(this, ivGroupImage, data.getFace(), R.mipmap.default_avatar);
    }

    @Override
    public void showResolvingResult() {
        finish();
        RxBus.get().post(BusAction.GROUP_LIST, "remove group");
        RxBus.get().post(BusAction.FINISH, BusAction.REMOVE_GROUP);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.CHANGE_NAME)
            }
    )
    public void changeName(String group) {

        tvGroupName.setText(group);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.FINISH)
            }
    )
    public void getList(String group) {
        if (group.equals(BusAction.REMOVE_GROUP)) {
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
