package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.view.adapter.GroupPictureAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
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
    @BindView(R.id.recyclerView_picture)
    RecyclerView recyclerViewPicture;
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
    @BindView(R.id.m_tv_sync_group)
    TextView mTvSyncGroup;

    @BindView(R.id.m_tv_sync_count)
    TextView mTvSyncCount;

    @BindView(R.id.m_btn_submit)
    Button mBtnSubmit;
    private GroupPictureAdapter adapter;

    @Override
    public void init() {
        mToolbar.setTitle(getResources().getString(R.string.issue_task));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GroupPictureAdapter(this, null);
        recyclerViewPicture.setAdapter(adapter);
        initListener();
    }

    private void initListener() {

        RxView.clicks(mBtnSubmit).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                ToastUtils.showShort("请输入要发布的作业");
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
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupIssueTaskActivity.this, GroupPublishTaskListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_issue_task;
    }

    @OnClick({R.id.m_rl_async_to_other, R.id.m_iv_issue_picture})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_rl_async_to_other:
                startActivityForResult(new Intent(this, GroupSyncGroupListActivity.class), 200);
                break;
            case R.id.m_iv_issue_picture:
                startActivityForResult(new Intent(this, PictureSelectorActivity.class), 300);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            ArrayList<ClassInfo> classInfoList = data.getParcelableArrayListExtra("selectedList");

            if (classInfoList != null && classInfoList.size() > 0) {
                mTvSyncCount.setText(String.valueOf(classInfoList.size()));
                mTvSyncCount.setVisibility(View.VISIBLE);

            } else {
                mTvSyncCount.setVisibility(View.GONE);
            }

        }

        if (requestCode == 300 && resultCode == -1 && data != null) {

            ArrayList<Uri> list = data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");

            if (list != null && list.size() > 0) {
                adapter.setData(list);
                recyclerViewPicture.setVisibility(View.VISIBLE);
            }else {
                recyclerViewPicture.setVisibility(View.GONE);
            }
        }

    }

}