package com.yc.english.union.view.activitys.teacher;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupChangeInfoContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.presenter.GroupChangeInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/31 09:08.
 * 老师设置加群验证条件
 */

public class GroupVerifyConditionActivity extends FullScreenActivity<GroupChangeInfoPresenter> implements GroupChangeInfoContract.View {


    @BindView(R.id.m_iv_all_allow)
    ImageView mIvAllAllow;
    @BindView(R.id.m_iv_all_forbid)
    ImageView mIvAllForbid;
    @BindView(R.id.m_iv_verify_join)
    ImageView mIvVerifyJoin;

    private int currentConditon;


    @Override
    public void init() {
        mPresenter = new GroupChangeInfoPresenter(this, this);
        mToolbar.setTitle(getString(R.string.join_verify));
        mToolbar.showNavigationIcon();
        mToolbar.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        if (getIntent() != null) {
            int valiType = getIntent().getIntExtra("valiType", GroupConstant.CONDITION_ALL_ALLOW);
            setVerifyResult(valiType);
            currentConditon = valiType;
        }
    }

    @OnClick({R.id.m_rl_all_allow, R.id.m_rl_all_forbid, R.id.m_rl_verify_join})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_rl_all_allow:
                currentConditon = GroupConstant.CONDITION_ALL_ALLOW;
                break;
            case R.id.m_rl_all_forbid:
                currentConditon = GroupConstant.CONDITION_ALL_FORBID;
                break;
            case R.id.m_rl_verify_join:
                currentConditon = GroupConstant.CONDITION_VERIFY_JOIN;
                break;
        }

        setImageResoure(currentConditon);

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_verify_condition;
    }


    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        SPUtils.getInstance().put(GroupInfoHelper.getGroupInfo().getId(), currentConditon);
        Intent intent = getIntent();
        intent.putExtra("condition", currentConditon);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setVerifyResult(int code) {
        switch (code) {
            case GroupConstant.CONDITION_ALL_ALLOW:
                mIvAllAllow.setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                break;
            case GroupConstant.CONDITION_ALL_FORBID:
                mIvAllForbid.setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                break;
            case GroupConstant.CONDITION_VERIFY_JOIN:
                mIvVerifyJoin.setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                break;
        }
    }

    private void setImageResoure(int currentConditon) {
        switch (currentConditon) {
            case GroupConstant.CONDITION_ALL_ALLOW:
                mIvAllAllow.setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                mIvAllForbid.setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                mIvVerifyJoin.setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                break;
            case GroupConstant.CONDITION_ALL_FORBID:
                mIvAllForbid.setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                mIvAllAllow.setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                mIvVerifyJoin.setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                break;
            case GroupConstant.CONDITION_VERIFY_JOIN:
                mIvVerifyJoin.setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                mIvAllForbid.setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                mIvAllAllow.setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                break;
        }
        mPresenter.changeGroupInfo(this, GroupInfoHelper.getGroupInfo().getId(), "", "", currentConditon + "");
    }

    @Override
    public void showChangeResult(RemoveGroupInfo data) {

    }
}
