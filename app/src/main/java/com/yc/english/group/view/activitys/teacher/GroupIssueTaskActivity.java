package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupTaskPublishContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupTaskPublishPresenter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.Source;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.activity.FileListActivity;
import io.rong.imkit.manager.AudioPlayManager;
import io.rong.imkit.manager.AudioRecordManager;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/27 15:37.
 * 发布作业
 */

public class GroupIssueTaskActivity extends FullScreenActivity<GroupTaskPublishPresenter> implements GroupTaskPublishContract.View, View.OnTouchListener {


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
    private String targetId;
    private List<ClassInfo> classInfoList;
    private ClassInfo mClassInfo;
    private List<Uri> uriList;

    @Override
    public void init() {
        mPresenter = new GroupTaskPublishPresenter(this, this);
        mToolbar.setTitle(getResources().getString(R.string.issue_task));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GroupPictureAdapter(this, null);
        recyclerViewPicture.setAdapter(adapter);

        if (getIntent() != null) {
            targetId = getIntent().getStringExtra("targetId");
            mPresenter.getGroupInfo(this, targetId);
        }

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
//                ToastUtils.showShort("发布成功");
                String desc = mEtIssueTask.getText().toString().trim();
                publishTask(desc);

            }
        });
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupIssueTaskActivity.this, GroupPublishTaskListActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mIvIssueFile).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                Intent intent = new Intent(GroupIssueTaskActivity.this, FileListActivity.class);
                intent.putExtra("rootDirType", 100);
                intent.putExtra("fileFilterType", 5);
                intent.putExtra("fileTraverseType", 201);
                startActivity(intent);

            }
        });

        mIvIssueVoice.setOnTouchListener(this);
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

            classInfoList = data.getParcelableArrayListExtra("selectedList");

            setSyncGroup(classInfoList.size());

        }

        if (requestCode == 300 && resultCode == -1 && data != null) {

            uriList = data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");

            if (uriList != null && uriList.size() > 0) {
                adapter.setData(uriList);
                recyclerViewPicture.setVisibility(View.VISIBLE);
            } else {
                recyclerViewPicture.setVisibility(View.GONE);
            }

            for (Uri uri : uriList) {
                String path = uri.getPath();// "file:///mnt/sdcard/FileName.mp3"
                File file = new File(path);
                mPresenter.uploadFile(file);
            }
        }

    }

    private void setSyncGroup(int count) {
        mTvSyncCount.setText(String.valueOf(count));
        mTvSyncCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private void publishTask(String desc) {
        StringBuilder sb = new StringBuilder(targetId);

        if (classInfoList != null && classInfoList.size() > 0) {
            for (ClassInfo classInfo : classInfoList) {
                if (classInfo.getClass_id().equals(targetId)) {
                    continue;
                }
                sb.append(",").append(classInfo.getClass_id());
            }
        }
        if (uriList != null && uriList.size() > 0) {
            for (Uri uri : uriList) {
                Glide.with(this).asBitmap().load(uri).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

//                        mPresenter.uploadFile(ConvertUtils.bitmap2Bytes(resource, Bitmap.CompressFormat.JPEG));

                        return false;
                    }
                });
            }
        }


        mPresenter.publishTask(sb.toString(), mClassInfo.getMaster_id(), desc, null, null, null);

    }


    @Override
    public void showGroupInfo(ClassInfo info) {
        mClassInfo = info;

    }

    @Override
    public void showTaskDetail() {
        Intent intent = new Intent();

//        intent.putExtra("task", desc);
//                intent.putExtra("")
        setResult(700, intent);
        finish();
    }

    private int count;

    @Override
    public void showMyGroupList(List<ClassInfo> list) {
        if (list != null && list.size() > 0) {
            for (ClassInfo classInfo : list) {
                boolean aBoolean = SPUtils.getInstance().getBoolean(classInfo.getClass_id());
                if (aBoolean) {
                    count++;
                }
            }
        }
        setSyncGroup(count);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
        if(!PermissionCheckUtil.checkPermissions(this, permissions)) {
            if(event.getAction() == 0) {
                PermissionCheckUtil.requestPermissions(this, permissions, 100);
            }

        }else {

            if(event.getAction() == 0) {
                AudioPlayManager.getInstance().stopPlay();
//                AudioRecordManager.getInstance().startRecord(v.getRootView(), this.mConversationType, this.mTargetId);
//                this.mLastTouchY = event.getY();
//                this.mUpDirection = false;
//                ((Button)v).setText(io.rong.imkit.R.string.rc_audio_input_hover);
            } else if(event.getAction() == 1 || event.getAction() == 3) {
                AudioRecordManager.getInstance().stopRecord();
//                ((Button)v).setText(io.rong.imkit.R.string.rc_audio_input);
            }

//            if(this.mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
//                RongIMClient.getInstance().sendTypingStatus(this.mConversationType, this.mTargetId, "RC:VcMsg");
//            }



        }



        return false;
    }
}
