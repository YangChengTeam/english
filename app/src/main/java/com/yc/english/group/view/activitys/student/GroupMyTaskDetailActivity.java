package com.yc.english.group.view.activitys.student;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.TimeUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupDoTaskDetailContract;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupDoTaskDetailPresenter;
import com.yc.english.group.view.adapter.GroupFileAdapter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;
import com.yc.english.group.view.adapter.GroupVoiceAdapter;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;
import com.yc.english.main.hepler.UserInfoHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
 */

public class GroupMyTaskDetailActivity extends FullScreenActivity<GroupDoTaskDetailPresenter> implements GroupDoTaskDetailContract.View {


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
    private GroupPictureAdapter adapter;
    private TaskInfo taskInfo;


    @Override
    public void init() {
        mPresenter = new GroupDoTaskDetailPresenter(this, this);
        mToolbar.setTitle(getString(R.string.task_detail));
        mToolbar.showNavigationIcon();
        if (getIntent() != null) {
            String taskDetailInfo = getIntent().getStringExtra("extra");
            taskInfo = JSONObject.parseObject(taskDetailInfo, TaskInfo.class);
            mPresenter.getPublishTaskDetail(this, taskInfo.getId(), taskInfo.getClass_ids().get(0), "");
        }
        RxView.clicks(mBtnSubmit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String desc = mEtFinishTask.getText().toString().trim();
                doTask(desc);
            }
        });


        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GroupPictureAdapter(this, true, null);
        recyclerViewPicture.setAdapter(adapter);

        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        voiceAdapter = new GroupVoiceAdapter(this, true, null);
        voiceRecyclerView.setAdapter(voiceAdapter);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new GroupFileAdapter(this, true, null);
        fileRecyclerView.setAdapter(fileAdapter);

    }

    private void doTask(String desc) {


        String uid = UserInfoHelper.getUserInfo().getUid();

        mPresenter.doTask(taskInfo.getClass_ids().get(0), uid, taskInfo.getId(), desc, "", "", "");

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
                Intent recordIntent = new Intent(
                        MediaStore.Audio.Media.RECORD_SOUND_ACTION);//初始化播放

                startActivityForResult(recordIntent, 400);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 300 && resultCode == -1 && data != null) {

            uriList = data.getParcelableArrayListExtra("android.intent.extra.RETURN_RESULT");

            if (uriList != null && uriList.size() > 0) {
                adapter.setData(uriList);
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
        if (requestCode == 400 && data != null) {
            Uri mRecordingUri = data.getData();
            String mRecordingFilename = getFilenameFromUri(mRecordingUri);
            File file = new File(mRecordingFilename);

            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mRecordingFilename);
                mediaPlayer.prepare();
                int duration = mediaPlayer.getDuration();
                int second = duration / 1000;
                mediaPlayer.release();
                Voice voice = new Voice(mRecordingFilename, second + "''");
                voiceList.add(voice);
                voiceAdapter.setData(voiceList);

            } catch (IOException e) {
                e.printStackTrace();
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

    private String getFilenameFromUri(Uri uri) {
        Cursor c = managedQuery(uri, null, "", null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToFirst();
        int dataIndex = c.getColumnIndexOrThrow(
                MediaStore.Audio.Media.DATA);
        int type = c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE);

        return c.getString(dataIndex);
    }

    @Override
    public void showTaskDetail(TaskInfo taskInfo) {

        setTaskType(taskInfo);

        mTvIssueTime.setText(taskInfo.getAdd_date() + " " + taskInfo.getAdd_week() + " " +
                TimeUtils.date2String(TimeUtils.millis2Date(Long.parseLong(taskInfo.getAdd_time())), new SimpleDateFormat("HH:mm:ss")));
    }

    @Override
    public void showUploadResult(TaskUploadInfo data) {

    }

    @Override
    public void showDoneWorkResult(TaskInfo data) {

    }

    private void setTaskType(TaskInfo taskInfo) {
        int type = Integer.parseInt(taskInfo.getType());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group36));
                mLlTaskDetail.setText(taskInfo.getDesp());
                mLlTaskDetail.showTextView();
                break;
            case GroupConstant.TASK_TYPE_PICTURE:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group40));
                mLlTaskDetail.showPictureView();
                mLlTaskDetail.setUriList(getPictures(taskInfo));
                break;
            case GroupConstant.TASK_TYPE_VOICE:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group38));
                mLlTaskDetail.showVoiceView();

                mLlTaskDetail.setVoices(getVoiceList(taskInfo));

                break;
            case GroupConstant.TASK_TYPE_WORD:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group42));
                mLlTaskDetail.showWordView();

                mLlTaskDetail.setFileInfos(getFileInfos(taskInfo));
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:
                mIvTaskTypeIcon.setImageDrawable(getResources().getDrawable(R.mipmap.group44));
                mLlTaskDetail.setText(taskInfo.getDesp());
                mLlTaskDetail.showSynthesizeView();
                mLlTaskDetail.setUriList(getPictures(taskInfo));
                mLlTaskDetail.setVoices(getVoiceList(taskInfo));
                mLlTaskDetail.setFileInfos(getFileInfos(taskInfo));
                break;
        }
    }


    private List<Uri> getPictures(TaskInfo taskInfo) {
        List<String> imgs = taskInfo.getBody().getImgs();
        List<Uri> uriList = new ArrayList<>();
        if (imgs != null && imgs.size() > 0) {
            for (String img : imgs) {
                uriList.add(Uri.parse(img));
            }
        }
        return uriList;
    }

    private List<Voice> getVoiceList(TaskInfo taskInfo) {
        List<String> voice = taskInfo.getBody().getVoices();
        List<Voice> voiceList = new ArrayList<>();

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            if (voice != null && voice.size() > 0) {
                for (String s : voice) {
                    mediaPlayer.setDataSource(s);
                    mediaPlayer.prepare();
                    int duration = mediaPlayer.getDuration();
                    int second = duration / 1000;
                    mediaPlayer.release();

                    voiceList.add(new Voice(s, second + "''"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return voiceList;
    }


    private List<FileInfo> getFileInfos(TaskInfo taskInfo) {

        List<String> list = taskInfo.getBody().getDocs();
        List<FileInfo> fileInfos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String s : list) {

                FileInfo fileInfo = new FileInfo();
                fileInfo.setFilePath(s);
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }

}
