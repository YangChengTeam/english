package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.subutil.util.PinyinUtils;
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
import com.yc.english.group.contract.GroupTaskPublishContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.presenter.GroupTaskPublishPresenter;
import com.yc.english.group.view.adapter.GroupFileAdapter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;
import com.yc.english.group.view.adapter.GroupVoiceAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.activity.FileListActivity;
import io.rong.imkit.model.FileInfo;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utils.FileTypeUtils;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/27 15:37.
 * 发布作业
 */

public class GroupIssueTaskActivity extends FullScreenActivity<GroupTaskPublishPresenter> implements GroupTaskPublishContract.View {


    @BindView(R.id.m_et_issue_task)
    EditText mEtIssueTask;
    @BindView(R.id.m_iv_issue_picture)
    ImageView mIvIssuePicture;
    @BindView(R.id.recyclerView_picture)
    RecyclerView recyclerViewPicture;
    @BindView(R.id.m_iv_issue_voice)
    ImageView mIvIssueVoice;
    @BindView(R.id.voice_recyclerView)
    RecyclerView voiceRecyclerView;
    @BindView(R.id.m_iv_issue_file)
    ImageView mIvIssueFile;
    @BindView(R.id.file_recyclerView)
    RecyclerView fileRecyclerView;
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
    private GroupVoiceAdapter voiceAdapter;
    private GroupFileAdapter fileAdapter;

    @Override
    public void init() {
        mPresenter = new GroupTaskPublishPresenter(this, this);
        mToolbar.setTitle(getResources().getString(R.string.issue_task));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.all_task));
        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GroupPictureAdapter(this, null);
        recyclerViewPicture.setAdapter(adapter);

        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        voiceAdapter = new GroupVoiceAdapter(this, null);
        voiceRecyclerView.setAdapter(voiceAdapter);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new GroupFileAdapter(this, null);
        fileRecyclerView.setAdapter(fileAdapter);


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
                TipsHelper.tips(GroupIssueTaskActivity.this, "请输入要发布的作业");
                return !TextUtils.isEmpty(mEtIssueTask.getText().toString().trim());
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {


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
                startActivity(intent);
            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_issue_task;
    }

    @OnClick({R.id.m_rl_async_to_other, R.id.m_iv_issue_picture, R.id.m_iv_issue_voice, R.id.m_iv_issue_file})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.m_rl_async_to_other:
                startActivityForResult(new Intent(this, GroupSyncGroupListActivity.class), 200);
                break;
            case R.id.m_iv_issue_picture:
                startActivityForResult(new Intent(this, PictureSelectorActivity.class), 300);
                break;
            case R.id.m_iv_issue_voice:
                Intent recordIntent = new Intent(
                        MediaStore.Audio.Media.RECORD_SOUND_ACTION);//初始化播放

                startActivityForResult(recordIntent, 400);
                break;

            case R.id.m_iv_issue_file:
                Intent intent = new Intent(GroupIssueTaskActivity.this, FileListActivity.class);
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
            for (Uri uri : uriList) {//上传图片
                String path = uri.getPath();// "file:///mnt/sdcard/FileName.mp3"
                File file = new File(path);
                String substring = path.substring(path.lastIndexOf("/") + 1);
                mPresenter.uploadFile(file, substring, substring);
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
                mPresenter.uploadFile(file, fileInfo.getFileName(), "");
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

    private void setSyncGroup(int count) {
        mTvSyncCount.setText(String.valueOf(count));
        mTvSyncCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private void publishTask(String desc) {


        for (Voice voice : voiceList) {//上传语音
            String uri = voice.getUri();

        }

        for (FileInfo fileInfo : fileInfos) {//上传文件
            Uri filePath = Uri.parse("file://" + fileInfo.getFilePath());
        }

        StringBuilder sb = new StringBuilder(targetId);
        StringBuilder picSb = new StringBuilder();

        if (classInfoList != null && classInfoList.size() > 0) {
            for (ClassInfo classInfo : classInfoList) {
                if (classInfo.getClass_id().equals(targetId)) {
                    continue;
                }
                sb.append(",").append(classInfo.getClass_id());
            }
        }
        if (picturePath.size() > 0) {
            for (String s : picturePath) {
                picSb.append(s).append(",");
            }
            picSb.deleteCharAt(picSb.length() - 1);
        }

        mPresenter.publishTask(sb.toString(), mClassInfo.getMaster_id(), desc, picSb.toString(), null, null);
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

    private List<String> picturePath = new ArrayList<>();

    @Override
    public void showUploadReslut(String file_path) {
        picturePath.add(file_path);
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.DELETE_FILE)
            }
    )
    public void deleteFile(FileInfo fileInfo) {
        fileInfos.remove(fileInfo);
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
    public void deletePicture(Uri uri) {
        uriList.remove(uri);
    }

}
