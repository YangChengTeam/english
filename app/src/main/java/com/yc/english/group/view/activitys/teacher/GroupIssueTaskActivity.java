package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.AudioRecordManager;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupTaskPublishContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupTaskPublishPresenter;
import com.yc.english.group.view.adapter.GroupFileAdapter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;
import com.yc.english.group.view.adapter.GroupVoiceAdapter;

import java.io.File;
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
 * Created by wanglin  on 2017/7/27 15:37.
 * 发布作业
 */

public class GroupIssueTaskActivity extends FullScreenActivity<GroupTaskPublishPresenter> implements GroupTaskPublishContract.View {


    @BindView(R.id.m_et_issue_task)
    EditText mEtIssueTask;

    @BindView(R.id.recyclerView_picture)
    RecyclerView recyclerViewPicture;

    @BindView(R.id.voice_recyclerView)
    RecyclerView voiceRecyclerView;

    @BindView(R.id.file_recyclerView)
    RecyclerView fileRecyclerView;
    @BindView(R.id.m_tv_sync_group)
    TextView mTvSyncGroup;
    @BindView(R.id.m_tv_sync_count)
    TextView mTvSyncCount;

    @BindView(R.id.m_btn_submit)
    Button mBtnSubmit;
    private GroupPictureAdapter pictureAdapter;
    private String targetId;
    private List<ClassInfo> classInfoList;
    private ClassInfo mClassInfo;

    private GroupVoiceAdapter voiceAdapter;
    private GroupFileAdapter fileAdapter;
    private List<ClassInfo> infoList;

    @Override
    public void init() {
        mPresenter = new GroupTaskPublishPresenter(this, this);
        mToolbar.setTitle(getResources().getString(R.string.issue_task));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pictureAdapter = new GroupPictureAdapter(this, true, null);
        recyclerViewPicture.setAdapter(pictureAdapter);

        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        voiceAdapter = new GroupVoiceAdapter(this, true, null);
        voiceRecyclerView.setAdapter(voiceAdapter);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new GroupFileAdapter(this, true, null);
        fileRecyclerView.setAdapter(fileAdapter);

        if (getIntent() != null) {
            targetId = getIntent().getStringExtra("targetId");
            mPresenter.getGroupInfo(this, targetId);
        }
        restoreTaskData();

        initListener();
    }


    private void initListener() {

        RxView.clicks(mBtnSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {


            @Override
            public void call(Void aVoid) {
                String desc = mEtIssueTask.getText().toString().trim();
                publishTask(desc);

            }
        });
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(GroupIssueTaskActivity.this, GroupPublishTaskListActivity.class);
                intent.putExtra("targetId", targetId);
                startActivity(intent);
            }
        });

        mToolbar.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTaskData();
                finish();
            }
        });
        recyclerViewPicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startActivityForResult(new Intent(GroupIssueTaskActivity.this, PictureSelectorActivity.class), 300);
                }
                return false;
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_issue_task;
    }

    @OnClick({R.id.m_rl_async_to_other, R.id.m_ll_issue_picture, R.id.m_ll_issue_voice, R.id.m_ll_issue_file})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.m_rl_async_to_other:
                intent = new Intent(this, GroupSyncGroupListActivity.class);
                intent.putExtra("classId", targetId);
                startActivityForResult(intent, 200);
                break;
            case R.id.m_ll_issue_picture:
                intent = new Intent(this, PictureSelectorActivity.class);
                startActivityForResult(intent, 300);
                break;
            case R.id.m_ll_issue_voice:
                audioRecord(v);
                KeyboardUtils.hideSoftInput(this);
                break;

            case R.id.m_ll_issue_file:
                intent = new Intent(GroupIssueTaskActivity.this, FileListActivity.class);
                intent.putExtra("rootDirType", 100);
                intent.putExtra("fileFilterType", 5);
                intent.putExtra("fileTraverseType", 201);
                startActivityForResult(intent, 500);
                break;
        }

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

    private List<Voice> voiceList = new ArrayList<>();
    private List<FileInfo> fileList = new ArrayList<>();
    private List<String> pictureList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {

            classInfoList = data.getParcelableArrayListExtra("selectedList");
            setSyncGroup(classInfoList.size());
        }

        if (requestCode == 300 && resultCode == -1 && data != null) {

            List<Uri> uriList = data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");

            if (uriList != null && uriList.size() > 0) {
                for (Uri uri : uriList) {//上传图片
                    String path = uri.getPath();// "file:///mnt/sdcard/FileName.mp3"
                    pictureList.add(path);
                    File file = new File(path);
                    String substring = path.substring(path.lastIndexOf("/") + 1);
                    mPresenter.uploadFile(this, file, substring, substring);
                }
                setFileInfo(pictureAdapter, pictureList);
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
                fileList.add(fileInfo);
            }
            setFileInfo(fileAdapter, fileList);
        }
    }


    private void setSyncGroup(int count) {
        mTvSyncCount.setText(String.valueOf(count));
        mTvSyncCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private boolean isPictureExsited;
    private String currentPicturePath;
    private boolean isVoiceExsited;
    private String currentVoicePath;
    private boolean isFileExsited;
    private String currentFilePath;

    private void publishTask(String desc) {

        StringBuilder sb = new StringBuilder(targetId);
        StringBuilder picSb = new StringBuilder();
        StringBuilder voiceSb = new StringBuilder();
        StringBuilder wordSb = new StringBuilder();

        if (classInfoList != null && classInfoList.size() > 0) {
            for (ClassInfo classInfo : classInfoList) {
                if (classInfo.getClass_id().equals(targetId)) {
                    continue;
                }
                sb.append(",").append(classInfo.getClass_id());
            }
        }

        if (pictureList.size() > 0) {
            for (String s : pictureList) {
                for (TaskUploadInfo taskUploadInfo : picturePath) {
                    if (s.substring(s.lastIndexOf("/") + 1).equals(taskUploadInfo.getFile_name())) {
                        isPictureExsited = true;
                        currentPicturePath = taskUploadInfo.getFile_path();
                        break;
                    }
                }
                if (isPictureExsited) {
                    picSb.append(currentPicturePath).append(",");
                    isPictureExsited = false;
                }
            }
            picSb.deleteCharAt(picSb.length() - 1);
        }

        if (voiceList.size() > 0) {
            for (Voice voice : voiceList) {
                for (TaskUploadInfo taskUploadInfo : voicePath) {
                    if (voice.getFile().getPath().substring(voice.getFile().getPath().lastIndexOf("/") + 1).equals(taskUploadInfo.getFile_name())) {
                        isVoiceExsited = true;
                        currentVoicePath = taskUploadInfo.getFile_path();
                        break;
                    }
                }
                if (isVoiceExsited) {
                    voiceSb.append(currentVoicePath).append(",");
                    isVoiceExsited = false;
                }
            }
            voiceSb.deleteCharAt(voiceSb.length() - 1);
        }

        if (fileList.size() > 0) {
            for (FileInfo fileInfo : fileList) {
                for (TaskUploadInfo taskUploadInfo : wordPath) {
                    if (fileInfo.getFileName().equals(taskUploadInfo.getFile_name())) {
                        isFileExsited = true;
                        currentFilePath = taskUploadInfo.getFile_path();
                        break;
                    }
                }
                if (isFileExsited) {
                    wordSb.append(currentFilePath).append(",");
                    isFileExsited = false;
                }
            }
            wordSb.deleteCharAt(wordSb.length() - 1);
        }

        mPresenter.publishTask(sb.toString(), mClassInfo.getMaster_id(), desc, picSb.toString(), voiceSb.toString(), wordSb.toString());
    }


    @Override
    public void showGroupInfo(ClassInfo info) {
        mClassInfo = info;
    }

    @Override
    public void showTaskDetail(TaskInfo info) {
        clearTaskData();
        Intent intent = new Intent();
        intent.putExtra("task", info);
        setResult(700, intent);
        finish();
    }

    private int count;

    @Override
    public void showMyGroupList(List<ClassInfo> list) {
        this.infoList = list;
        if (list != null && list.size() > 0) {
            for (ClassInfo classInfo : list) {
                boolean aBoolean = SPUtils.getInstance().getBoolean(classInfo.getClass_id() + "class");
                if (aBoolean) {
                    count++;
                    if (classInfoList == null) {
                        classInfoList = new ArrayList<>();
                    }
                    classInfoList.add(classInfo);
                }
            }
        }
        setSyncGroup(count);
    }

    private List<TaskUploadInfo> picturePath = new ArrayList<>();
    private List<TaskUploadInfo> voicePath = new ArrayList<>();
    private List<TaskUploadInfo> wordPath = new ArrayList<>();

    @Override
    public void showUploadReslut(TaskUploadInfo taskUploadInfo) {
        String file_path = taskUploadInfo.getFile_path();
        if (file_path.endsWith(".png") || file_path.endsWith(".jpg") || file_path.endsWith(".jpeg"))
            picturePath.add(taskUploadInfo);
        else if (file_path.endsWith(".voice")) voicePath.add(taskUploadInfo);
        else wordPath.add(taskUploadInfo);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.DELETE_FILE)
            }
    )
    public void deleteFile(FileInfo fileInfo) {
        fileList.remove(fileInfo);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.DELETE_VOICE)
            }
    )
    public void deleteVoice(Voice voice) {
        voiceList.remove(voice);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.DELETE_PICTURE)
            }
    )
    public void deletePicture(String path) {
        pictureList.remove(path);
    }


    private void audioRecord(View view) {
        AudioRecordManager.getInstance().startRecord(view);
        AudioRecordManager.getInstance().setCallback(new AudioRecordManager.Callback() {
            @Override
            public void onSuccess(File file, int duration) {

                Voice voice = new Voice(file, duration + "''");

                voiceList.add(voice);

                setFileInfo(voiceAdapter, voiceList);

                mPresenter.uploadFile(GroupIssueTaskActivity.this, file, file.getPath().substring(file.getPath().lastIndexOf("/") + 1), "");

                LogUtils.i("AudioRecordManager" + file);
            }

            @Override
            public void onFail(String message) {

            }
        });

    }

    private void saveTaskData() {
        SPUtils.getInstance().put(GroupConstant.TEXT_TASK, mEtIssueTask.getText().toString());
        SPUtils.getInstance().put(GroupConstant.PICTUE_TASK, JSONObject.toJSONString(pictureList));
        SPUtils.getInstance().put(GroupConstant.VOICE_TASK, JSONObject.toJSONString(voiceList));
        SPUtils.getInstance().put(GroupConstant.WORD_TASK, JSONObject.toJSONString(fileList));
    }

    private void clearTaskData() {
        SPUtils.getInstance().put(GroupConstant.TEXT_TASK, "");
        SPUtils.getInstance().put(GroupConstant.PICTUE_TASK, "");
        SPUtils.getInstance().put(GroupConstant.VOICE_TASK, "");
        SPUtils.getInstance().put(GroupConstant.WORD_TASK, "");
        if (infoList != null && infoList.size() > 0) {
            for (ClassInfo classInfo : infoList) {
                SPUtils.getInstance().put(classInfo.getClass_id() + "class", false);
            }
        }

    }

    private void restoreTaskData() {
        mEtIssueTask.setText(SPUtils.getInstance().getString(GroupConstant.TEXT_TASK));
        String pictureTask = SPUtils.getInstance().getString(GroupConstant.PICTUE_TASK);
        List<String> pictureList = JSONObject.parseArray(pictureTask, String.class);
        pictureAdapter.setData(pictureList);

        String voiceTask = SPUtils.getInstance().getString(GroupConstant.VOICE_TASK);
        List<Voice> voiceList = JSONObject.parseArray(voiceTask, Voice.class);
        voiceAdapter.setData(voiceList);
        String wordTask = SPUtils.getInstance().getString(GroupConstant.WORD_TASK);
        List<FileInfo> fileInfos = JSONObject.parseArray(wordTask, FileInfo.class);
        fileAdapter.setData(fileInfos);
        if (pictureList != null && pictureList.size() > 0) {
            for (String path : pictureList) {
                File file = new File(path);
                String substring = path.substring(path.lastIndexOf("/") + 1);
                mPresenter.uploadFile(this, file, substring, substring);
            }
        }
        if (voiceList != null && voiceList.size() > 0) {
            for (Voice voice : voiceList) {
                mPresenter.uploadFile(GroupIssueTaskActivity.this, voice.getFile(), voice.getFile().getPath().substring(voice.getFile().getPath().lastIndexOf("/") + 1), "");
            }
        }

        if (fileInfos != null && fileInfos.size() > 0) {
            for (FileInfo fileInfo : fileInfos) {
                Uri filePath = Uri.parse("file://" + fileInfo.getFilePath());
                File file = new File(filePath.getPath());
                mPresenter.uploadFile(this, file, fileInfo.getFileName(), "");
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveTaskData();
    }
}
