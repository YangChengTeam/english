package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.helper.AvatarHelper;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupResolvingContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.presenter.GroupResolvingPresenter;
import com.yc.english.group.rong.models.GroupInfo;

import butterknife.BindView;
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
    private GroupInfo groupInfo;
    private ClassInfo mInfo;

    @Override
    public void init() {

        mPresenter = new GroupResolvingPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group_manager));
        mToolbar.showNavigationIcon();

        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            tvGroupName.setText(groupInfo.getName());
        }

        int condition = SPUtils.getInstance().getInt(groupInfo.getId());
        setVerify_reslut(condition);
        mPresenter.queryGroupById(this, groupInfo.getId());

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_group_manager;
    }

    @OnClick({R.id.rl_group_image, R.id.rl_group_name, R.id.rl_group_delete_member,
            R.id.tv_permission_check, R.id.rl_group_check, R.id.rl_group_transfer, R.id.btn_resolving_group})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {

            case R.id.rl_group_image:
                AvatarHelper.openAlbum(this);
                break;
            case R.id.rl_group_name:
                intent = new Intent(this, GroupChangeNameActivity.class);
                intent.putExtra("group", groupInfo);
                startActivity(intent);
                break;
            case R.id.rl_group_delete_member:

                intent = new Intent(this, GroupDeleteMemberActivity.class);
                intent.putExtra("group", groupInfo);
                startActivity(intent);
                break;
            case R.id.rl_group_check:
                intent = new Intent(this, GroupVerifyConditionActivity.class);
                intent.putExtra("group", groupInfo);
                startActivityForResult(intent, 200);
                break;
            case R.id.rl_group_transfer:
                intent = new Intent(this, GroupTransferActivity.class);
                intent.putExtra("group", this.mInfo);

                startActivity(intent);
                break;
            case R.id.btn_resolving_group:

                mPresenter.resolvingGroup(groupInfo.getId(), mInfo.getMaster_id());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            int condition = data.getIntExtra("condition", 0);
            setVerify_reslut(condition);
            return;
        }
        if (requestCode == 1) {
            AvatarHelper.uploadAvatar(this, new AvatarHelper.IAvatar() {
                @Override
                public void uploadAvatar(String image) {
                    mPresenter.changeGroupInfo(GroupManagerActivity.this, groupInfo.getId(), "", image, "");
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
    public void showClassInfo(ClassInfo info) {
        this.mInfo = info;
        tvGroupName.setText(info.getClassName());
        GlideHelper.circleImageView(this, ivGroupImage, info.getImageUrl(), R.mipmap.default_avatar);
    }


    @Override
    public void showChangeGroupInfo(RemoveGroupInfo data) {
        GlideHelper.circleImageView(this, ivGroupImage, data.getFace(), R.mipmap.default_avatar);
    }

    @Override
    public void showResolvingResult() {
        finish();
        RxBus.get().post(BusAction.GROUPLIST, "remove group");
        RxBus.get().post(BusAction.FINISH, BusAction.REMOVE_GROUP);

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.CHANGE_NAME)
            }
    )
    public void changeName(String group) {
        mPresenter.queryGroupById(this, groupInfo.getId());
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


}
