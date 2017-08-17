package com.yc.english.group.view.activitys.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.helper.AudioRecordManager;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupDoTaskDetailContract;
import com.yc.english.group.contract.GroupDoneTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupDoTaskDetailPresenter;
import com.yc.english.group.presenter.GroupDoneTaskDetailPresenter;
import com.yc.english.group.utils.TaskUtil;
import com.yc.english.group.view.adapter.GroupFileAdapter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;
import com.yc.english.group.view.adapter.GroupVoiceAdapter;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;
import com.yc.english.main.hepler.UserInfoHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.activity.FileListActivity;
import io.rong.imkit.model.FileInfo;
import io.rong.imkit.plugin.image.PictureSelectorActivity;


/**
 * Created by wanglin  on 2017/7/27 18:30.
 * 学生查看作业评分详情
 */

public class GroupTaskGradeActivity extends FullScreenActivity<GroupDoTaskDetailPresenter> implements GroupDoTaskDetailContract.View {
    @BindView(R.id.m_tv_issue_time)
    TextView mTvIssueTime;
    @BindView(R.id.m_iv_grade)
    ImageView mIvGrade;
    @BindView(R.id.m_iv_task_icon)
    ImageView mIvTaskIcon;
    @BindView(R.id.publish_multifunctionLinearLayout)
    MultifunctionLinearLayout publishMultifunctionLinearLayout;
    @BindView(R.id.do_multifunctionLinearLayout)
    MultifunctionLinearLayout doMultifunctionLinearLayout;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.sl_container)
    ScrollView slContainer;
    @BindView(R.id.ll_grade_container)
    LinearLayout llGradeContainer;
    @BindView(R.id.m_et_finish_task)
    EditText mEtFinishTask;
    @BindView(R.id.recyclerView_picture)
    RecyclerView recyclerViewPicture;
    @BindView(R.id.m_ll_issue_picture)
    LinearLayout mLlIssuePicture;
    @BindView(R.id.voice_recyclerView)
    RecyclerView voiceRecyclerView;
    @BindView(R.id.m_ll_issue_voice)
    LinearLayout mLlIssueVoice;
    @BindView(R.id.file_recyclerView)
    RecyclerView fileRecyclerView;
    @BindView(R.id.m_ll_issue_file)
    LinearLayout mLlIssueFile;
    @BindView(R.id.m_btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.ll_do_task)
    LinearLayout llDoTask;
    private TaskInfo taskInfo;
    private List<Uri> uriList;
    private GroupPictureAdapter groupPictureAdapter;
    private GroupVoiceAdapter groupVoiceAdapter;
    private GroupFileAdapter groupFileAdapter;

    @Override
    public void init() {
        mPresenter = new GroupDoTaskDetailPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getString(R.string.task_detail));
        if (getIntent() != null) {
            if (getIntent().getStringExtra("extra") != null) {
                String detailTask = getIntent().getStringExtra("extra");
                taskInfo = JSONObject.parseObject(detailTask, TaskInfo.class);
                getData();
            } else {
                String taskId = getIntent().getStringExtra("taskId");
                String classId = getIntent().getStringExtra("classId");
                String id = getIntent().getStringExtra("id");
                mPresenter.getDoneTaskDetail(this, taskId, UserInfoHelper.getUserInfo().getUid());
                mPresenter.getPublishTaskDetail(this, taskId, classId, UserInfoHelper.getUserInfo().getUid());
            }
        }

        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        groupPictureAdapter = new GroupPictureAdapter(this, true, null);
        recyclerViewPicture.setAdapter(groupPictureAdapter);

        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupVoiceAdapter = new GroupVoiceAdapter(this, true, null);
        voiceRecyclerView.setAdapter(groupVoiceAdapter);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupFileAdapter = new GroupFileAdapter(this, true, null);
        fileRecyclerView.setAdapter(groupFileAdapter);
        recyclerViewPicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startActivityForResult(new Intent(GroupTaskGradeActivity.this, PictureSelectorActivity.class), 300);
                }
                return false;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_task_grade;
    }


    private void setScore(TaskInfo info) {
        if (info.getScore() != null) {
            if (info.getScore().equals("A+")) {
                mIvGrade.setImageResource(R.mipmap.group30);
            } else if (info.getScore().equals("A")) {
                mIvGrade.setImageResource(R.mipmap.group31);
            } else if (info.getScore().equals("B+")) {
                mIvGrade.setImageResource(R.mipmap.group32);
            } else if (info.getScore().equals("B")) {
                mIvGrade.setImageResource(R.mipmap.group33);
            } else if (info.getScore().equals("C")) {
                mIvGrade.setImageResource(R.mipmap.group34);
            }
        }
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(slContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(slContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(slContainer);
    }

    private void getData() {
        mPresenter.getDoneTaskDetail(this, taskInfo.getTask_id(), UserInfoHelper.getUserInfo().getUid());
        mPresenter.getPublishTaskDetail(this, taskInfo.getTask_id(), taskInfo.getClass_id(), UserInfoHelper.getUserInfo().getUid());
    }


    @Override
    public void showTaskDetail(TaskInfo info) {
        mTvIssueTime.setText(info.getAdd_date() + " " + info.getAdd_week() + " " + info.getAdd_time());
        publishMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.PUSHLISH);
        TaskUtil.showContextView(mIvTaskIcon, info, publishMultifunctionLinearLayout);
    }


    @Override
    public void showDoneWorkResult(TaskInfo info) {
        doMultifunctionLinearLayout.setType(MultifunctionLinearLayout.Type.DONE);
        TaskUtil.showContextView(mIvTaskIcon, info, doMultifunctionLinearLayout);
        setScore(info);
        showPage(info);
    }

    private void showPage(TaskInfo info) {
        if (info != null) {
            if (info.getIs_done() == 1) {
                llGradeContainer.setVisibility(View.VISIBLE);
                llDoTask.setVisibility(View.GONE);
            } else {
                llGradeContainer.setVisibility(View.GONE);
                llDoTask.setVisibility(View.VISIBLE);
            }
        }

    }

    @OnClick({R.id.m_ll_issue_picture, R.id.m_ll_issue_voice, R.id.m_ll_issue_file})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.m_ll_issue_picture:
                startActivityForResult(new Intent(this, PictureSelectorActivity.class), 300);
                break;
            case R.id.m_ll_issue_voice:
                audioRecord(v);
                break;
            case R.id.m_ll_issue_file:
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
                setFileInfo(groupPictureAdapter, pictureList);

                for (Uri uri : uriList) {//上传图片
                    String path = uri.getPath();// "file:///mnt/sdcard/FileName.mp3"
                    File file = new File(path);
                    String substring = path.substring(path.lastIndexOf("/") + 1);
                    mPresenter.uploadFile(this, file, substring, substring);
                }
            }
        }
        if (requestCode == 500 && data != null) {
            HashSet selectedFileInfos = (HashSet) data.getSerializableExtra("selectedFiles");
            Iterator iterator = selectedFileInfos.iterator();
            while (iterator.hasNext()) {
                FileInfo fileInfo = (FileInfo) iterator.next();
                Uri filePath = Uri.parse("file://" + fileInfo.getFilePath());
                File file = new File(filePath.getPath());
                fileInfos.add(fileInfo);
                setFileInfo(groupFileAdapter, fileInfos);
                mPresenter.uploadFile(this, file, fileInfo.getFileName(), "");

            }

        }

    }

    private List<String> picturePath = new ArrayList<>();
    private List<String> voicePath = new ArrayList<>();
    private List<String> wordPath = new ArrayList<>();

    @Override
    public void showUploadResult(TaskUploadInfo data) {
        String file_path = data.getFile_path();
        if (file_path.endsWith(".png") || file_path.endsWith(".jpg") || file_path.endsWith(".jpeg"))
            picturePath.add(file_path);
        else if (file_path.endsWith(".voice")) voicePath.add(file_path);
        else wordPath.add(file_path);
    }

    @Override
    public void showFile() {
        mAdapter.setData(mList);
    }

    private BaseAdapter mAdapter;
    private List mList;

    private void setFileInfo(BaseAdapter adapter, List list) {
        this.mAdapter = adapter;
        this.mList = list;
    }

    private void audioRecord(View view) {
        AudioRecordManager.getInstance().startRecord(view);
        AudioRecordManager.getInstance().setCallback(new AudioRecordManager.Callback() {
            @Override
            public void onSuccess(File file, int duration) {
                Voice voice = new Voice(file, duration + "''");
                voiceList.add(voice);
                setFileInfo(groupVoiceAdapter, voiceList);
                mPresenter.uploadFile(GroupTaskGradeActivity.this, file, file.getPath().substring(file.getPath().lastIndexOf("/") + 1), "");
                LogUtils.i("AudioRecordManager" + file);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }
}
