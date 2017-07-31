package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.rong.models.GroupInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wanglin  on 2017/7/26 17:32.
 */

public class GroupManagerActivity extends BaseActivity {


    @BindView(R.id.txt1)
    TextView txt1;

    @BindView(R.id.tv_manager)
    TextView tvManager;
    @BindView(R.id.img3)
    ImageView img3;

    @BindView(R.id.iv_group_image)
    ImageView ivGroupImage;

    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.tv_permission_check)
    TextView tvPermissionCheck;

    @Override
    public void init() {
        img3.setVisibility(View.GONE);
        txt1.setText(getResources().getString(R.string.group_manager));

        ivGroupImage.setImageBitmap(ImageUtils.toRound(BitmapFactory.decodeResource(getResources(), R.mipmap.portial)));
        if (getIntent() != null) {
            GroupInfo groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            tvGroupName.setText(groupInfo.getName());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_group_manager;
    }

    @OnClick({R.id.img1, R.id.rl_group_image, R.id.rl_group_name, R.id.rl_group_delete_member,
            R.id.tv_permission_check, R.id.rl_group_check, R.id.rl_group_transfer, R.id.btn_resolving_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img1:
                finish();
                break;
            case R.id.rl_group_image:
                break;
            case R.id.rl_group_name:
                startActivity(new Intent(this, GroupChangeNameActivity.class));
                break;
            case R.id.rl_group_delete_member:
                startActivity(new Intent(this, GroupDeleteMemberActivity.class));
                break;
            case R.id.rl_group_check:
                startActivityForResult(new Intent(this, GroupVerifyConditionActivity.class), 200);
                break;
            case R.id.rl_group_transfer:
                startActivity(new Intent(this, GroupTransferActivity.class));
                break;
            case R.id.btn_resolving_group:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            int condition = data.getIntExtra("condition", 0);
            switch (condition) {
                case GroupConstant.CONDITION_ALL_ALLOW:
                    tvPermissionCheck.setText(getString(R.string.all_allow));
                    break;
                case GroupConstant.CONDITION_ALL_FORBID:
                    tvPermissionCheck.setText(getString(R.string.all_forbid));
                    break;
                case GroupConstant.CONDITION_VERIFYJOIN:
                    tvPermissionCheck.setText(getString(R.string.verify_join));
                    break;

            }

        }
    }

}
