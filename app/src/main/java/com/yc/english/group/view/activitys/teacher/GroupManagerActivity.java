package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupResolvingContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupResolvingPresenter;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.utils.EngineUtils;

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

    @Override
    public void init() {

        mPresenter = new GroupResolvingPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group_manager));
        mToolbar.showNavigationIcon();

        ivGroupImage.setImageBitmap(ImageUtils.toRound(BitmapFactory.decodeResource(getResources(), R.mipmap.portial)));
        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            tvGroupName.setText(groupInfo.getName());
        }

        int condition = SPUtils.getInstance().getInt(GroupConstant.VERIFY_RESULT);
        setVerify_reslut(condition);

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
                startActivity(new Intent(this, GroupTransferActivity.class));
                break;
            case R.id.btn_resolving_group:

                mPresenter.queryGroupById(this, groupInfo.getId());


                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            int condition = data.getIntExtra("condition", 0);
            setVerify_reslut(condition);
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
        mPresenter.resolvingGroup(groupInfo.getId(), info.getMaster_id());
    }

    @Override
    public void showResolvingResult() {

        finish();
        RxBus.get().post(BusAction.GROUPLIST, "remove group");
        RxBus.get().post(BusAction.FINISH, BusAction.REMOVE_GROUP);

    }
}
