package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
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
                startActivity(new Intent(this,GroupChangeNameActivity.class));
                break;
            case R.id.rl_group_delete_member:
                break;
            case R.id.tv_permission_check:
                break;
            case R.id.rl_group_check:
                break;
            case R.id.rl_group_transfer:
                break;
            case R.id.btn_resolving_group:
                break;
        }
    }

}
