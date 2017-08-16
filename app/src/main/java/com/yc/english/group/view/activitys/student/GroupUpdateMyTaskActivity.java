package com.yc.english.group.view.activitys.student;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.AudioRecordManager;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupDoneTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupDoneTaskDetailPresenter;
import com.yc.english.group.utils.TaskUtil;
import com.yc.english.group.view.adapter.GroupFileAdapter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;
import com.yc.english.group.view.adapter.GroupVoiceAdapter;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;
import com.yc.english.main.hepler.UserInfoHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.activity.FileListActivity;
import io.rong.imkit.model.FileInfo;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/27 18:01.
 * 学生提交作业后修改
 */

public class GroupUpdateMyTaskActivity extends FullScreenActivity<GroupDoneTaskDetailPresenter> implements GroupDoneTaskDetailContract.View {


    @BindView(R.id.m_iv_task_type_icon)
    ImageView mIvTaskTypeIcon;
    @BindView(R.id.m_tv_issue_time)
    TextView mTvIssueTime;
    @BindView(R.id.m_ll_task_detail)
    MultifunctionLinearLayout mLlTaskDetail;
    @BindView(R.id.m_et_finish_task)
    EditText mEtFinishTask;


    @BindView(R.id.voice_recyclerView)
    RecyclerView voiceRecyclerView;

    @BindView(R.id.file_recyclerView)
    RecyclerView fileRecyclerView;
    @BindView(R.id.m_btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.recyclerView_picture)
    RecyclerView recyclerViewPicture;

    private List<Uri> uriList;
    private GroupVoiceAdapter voiceAdapter;
    private GroupFileAdapter fileAdapter;
    private GroupPictureAdapter pictureAdapter;
    private TaskInfo taskInfo;


    @Override
    public void init() {
        mPresenter = new GroupDoneTaskDetailPresenter(this, this);
        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        if (getIntent() != null) {
            String taskDetailInfo = getIntent().getStringExtra("extra");
            taskInfo = JSONObject.parseObject(taskDetailInfo, TaskInfo.class);

            mPresenter.getPublishTaskDetail(this, taskInfo.getTask_id(), taskInfo.getClass_id(), "");
            mPresenter.getDoneTaskDetail(this, taskInfo.getId(), UserInfoHelper.getUserInfo().getUid());
        }
        RxView.clicks(mBtnSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String desc = mEtFinishTask.getText().toString().trim();
                doTask(desc);
            }
        });

        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pictureAdapter = new GroupPictureAdapter(this, true, null);
        recyclerViewPicture.setAdapter(pictureAdapter);

        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        voiceAdapter = new GroupVoiceAdapter(this, true, null);
        voiceRecyclerView.setAdapter(voiceAdapter);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new GroupFileAdapter(this, true, null);
        fileRecyclerView.setAdapter(fileAdapter);

    }

    private void doTask(String desc) {

        String uid = UserInfoHelper.getUserInfo().getUid();
        mPresenter.updateDoTask(taskInfo.getId(), uid, desc, "", "", "");

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_detail;
    }


    @OnClick({R.id.m_iv_issue_picture, R.id.m_iv_issue_voice, R.id.m_iv_issue_file})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_iv_issue_picture:
                startActivityForResult(new Intent(this, PictureSelectorActivity.class), 300);
                break;
            case R.id.m_iv_issue_voice:
                audioRecord(v);
                break;

            case R.id.m_iv_issue_file:
                Intent intent = new Intent(this, FileListActivity.class);
                intent.putExtra("rootDirType", 100);
                intent.putExtra("fileFilterType", 5);
                intent.putExtra("fileTraverseType", 201);
                startActivityForResult(intent, 500);
                break;
        }

    }

    private List<Voice> voiceList = new ArrayList<>();
    private List<FileInfo> fileInfos = new ArrayList<>();
    private List<String> pictureList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 300 && resultCode == -1 && data != null) {

            uriList = data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");

            if (uriList != null && uriList.size() > 0) {
                for (Uri uri : uriList) {
                    pictureList.add(uri.getPath());
                }
                pictureAdapter.setData(pictureList);
                recyclerViewPicture.setVisibility(View.VISIBLE);
            } else {
                recyclerViewPicture.setVisibility(View.GONE);
            }
            for (Uri uri : uriList) {//上传图片
                String path = uri.getPath();// "file:///mnt/sdcard/FileName.mp3"
                File file = new File(path);
                String substring = path.substring(path.lastIndexOf("/") + 1);
                mPresenter.uploadFile(this, file, substring, substring);
            }
        }
        if (requestCode == 500 && data != null) {
            HashSet selectedFileInfos = (HashSet) data.getSerializableExtra("selectedFiles");
            Iterator iterator = selectedFileInfos.iterator();
            while (iterator.hasNext()) {
                FileInfo fileInfo = (FileInfo) iterator.next();
                Uri filePath = Uri.parse("file://" + fileInfo.getFilePath());
                File file = new File(filePath.getPath());
                mPresenter.uploadFile(this, file, fileInfo.getFileName(), "");
                fileInfos.add(fileInfo);
            }
            fileAdapter.setData(fileInfos);

        }

    }


    private void audioRecord(View view) {
        AudioRecordManager.getInstance().startRecord(view);
        AudioRecordManager.getInstance().setCallback(new AudioRecordManager.Callback() {
            @Override
            public void onSuccess(File file, int duration) {

                Voice voice = new Voice(file, duration + "''");
                voiceList.add(voice);
                voiceAdapter.setData(voiceList);
                LogUtils.i("AudioRecordManager" + file);
            }

            @Override
            public void onFail(String message) {

            }
        });


    }

    @Override
    public void showDoneTaskDetail(TaskInfo info) {
        pictureAdapter.setData(info.getBody().getImgs());
//        voiceAdapter.setData(info.getBody().getVoices());
//        fileAdapter.setData(info.getBody().getDocs());
    }

    @Override
    public void showPublishTaskDetail(TaskInfo info) {
        mTvIssueTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        mLlTaskDetail.setType(MultifunctionLinearLayout.Type.PUSHLISH);
        TaskUtil.showContextView(mIvTaskTypeIcon, info, mLlTaskDetail);
    }
}
