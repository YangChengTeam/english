package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.service.notification.Condition;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/31 09:08.
 */

public class GroupVerifyConditionActivity extends FullScreenActivity implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.m_rb_all_allow)
    RadioButton mRbAllAllow;
    @BindView(R.id.m_rb_all_forbid)
    RadioButton mRbAllForbid;
    @BindView(R.id.m_rb_verify_join)
    RadioButton mRbVerifyJoin;


    private int currentConditon;

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.join_verify));
        mToolbar.showNavigationIcon();

        initListener();

    }

    private void initListener() {
        mRbAllAllow.setOnCheckedChangeListener(this);
        mRbAllForbid.setOnCheckedChangeListener(this);
        mRbVerifyJoin.setOnCheckedChangeListener(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_verify_condition;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView == mRbAllAllow) {
                mRbAllForbid.setChecked(false);
                mRbVerifyJoin.setChecked(false);
                currentConditon = GroupConstant.CONDITION_ALL_ALLOW;

            } else if (buttonView == mRbAllForbid) {
                mRbAllAllow.setChecked(false);
                mRbVerifyJoin.setChecked(false);
                currentConditon = GroupConstant.CONDITION_ALL_FORBID;

            } else if (buttonView == mRbVerifyJoin) {
                mRbAllForbid.setChecked(false);
                mRbAllAllow.setChecked(false);
                currentConditon = GroupConstant.CONDITION_VERIFYJOIN;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("condition", currentConditon);
        setResult(RESULT_OK, intent);
        finish();

    }
}
