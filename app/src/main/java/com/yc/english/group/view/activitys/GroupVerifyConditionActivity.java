package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
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
        mToolbar.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        int verify_result = SPUtils.getInstance().getInt(GroupConstant.VERIFY_RESULT);
        setVerifyResult(verify_result);

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
                currentConditon = GroupConstant.CONDITION_VERIFY_JOIN;
            }
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        back();

    }

    private void back() {
        SPUtils.getInstance().put(GroupConstant.VERIFY_RESULT, currentConditon);
        Intent intent = getIntent();
        intent.putExtra("condition", currentConditon);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setVerifyResult(int code) {

        if (code == 0) {
            mRbAllAllow.setChecked(true);
        }
        switch (code) {
            case GroupConstant.CONDITION_ALL_ALLOW:
                mRbAllAllow.setChecked(true);
                break;
            case GroupConstant.CONDITION_ALL_FORBID:
                mRbAllForbid.setChecked(true);
                break;
            case GroupConstant.CONDITION_VERIFY_JOIN:
                mRbVerifyJoin.setChecked(true);
                break;
        }
    }
}
