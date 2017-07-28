package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/27 15:37.
 * 发布作业
 */

public class GroupIssueTaskActivity extends FullScreenActivity {
    @BindView(R.id.m_et_issue_task)
    EditText mEtIssueTask;
    @BindView(R.id.m_iv_issue_picture)
    ImageView mIvIssuePicture;
    @BindView(R.id.m_iv_issue_result_picture)
    ImageView mIvIssueResultPicture;
    @BindView(R.id.m_iv_issue_picture_delete)
    ImageView mIvIssuePictureDelete;
    @BindView(R.id.m_iv_issue_voice)
    ImageView mIvIssueVoice;
    @BindView(R.id.m_tv_issue_result_voice)
    TextView mTvIssueResultVoice;
    @BindView(R.id.m_iv_issue_voice_delete)
    ImageView mIvIssueVoiceDelete;
    @BindView(R.id.m_iv_issue_file)
    ImageView mIvIssueFile;
    @BindView(R.id.m_tv_issue_result_file_title)
    TextView mTvIssueResultFileTitle;
    @BindView(R.id.m_iv_issue_file_delete)
    ImageView mIvIssueFileDelete;

    @BindView(R.id.m_btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.m_tv_sync_group)
    TextView mTvSyncGroup;

    @Override
    public void init() {
        mToolbar.setTitle(getResources().getString(R.string.issue_task));
        mToolbar.showNavigationIcon();
        initListener();
    }

    private void initListener() {


        RxView.clicks(mBtnSubmit).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                ToastUtils.showShort("call");
                return !TextUtils.isEmpty(mEtIssueTask.getText().toString().trim());
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtils.showShort("发布成功");
                Intent intent = new Intent();
                intent.putExtra("task", mEtIssueTask.getText().toString().trim());
                setResult(700, intent);
                finish();

            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_issue_task;
    }

    @OnClick({R.id.m_rl_async_to_other})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_rl_async_to_other:
                startActivityForResult(new Intent(this, GroupSyncListActivity.class), 200);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            ArrayList<ClassInfo> classInfoList = data.getParcelableArrayListExtra("selectedList");
            if (classInfoList != null && classInfoList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (ClassInfo classInfo : classInfoList) {
                    sb.append(classInfo.getClassName()).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                mTvSyncGroup.setText(sb.toString());

            }

        }
    }


}
