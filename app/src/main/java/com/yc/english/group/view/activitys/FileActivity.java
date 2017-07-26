package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.yc.english.R;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.activity.FileListActivity;

/**
 * Created by wanglin  on 2017/7/19 11:16.
 */

public class FileActivity extends AppCompatActivity {

    @BindView(R.id.rc_action_bar_title)
    TextView rcActionBarTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_activity_plugin);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        rcActionBarTitle.setText("选择附件");
    }

    @OnClick({R.id.rc_action_bar_back, R.id.tv_word, R.id.tv_audio, R.id.tv_video})
    public void onClick(View view) {
        Intent intent = new Intent(this, FileListActivity.class);
        switch (view.getId()) {
            case R.id.rc_action_bar_back:
                finish();
                break;
            case R.id.tv_word://上传word
                intent.putExtra("rootDirType", 100);
                intent.putExtra("fileFilterType", 5);
                intent.putExtra("fileTraverseType", 201);
//                intent.putExtra("rootDirType", 100);
//                intent.putExtra("fileFilterType", 1);
//                intent.putExtra("fileTraverseType", 200);
                break;
            case R.id.tv_audio://上传音频

                break;
            case R.id.tv_video://上传视频

                break;

        }
        this.startActivityForResult(intent, 730);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 730 && data != null) {
            HashSet selectedFileInfos = (HashSet) data.getSerializableExtra("selectedFiles");
            Intent intent = new Intent();
            intent.putExtra("sendSelectedFiles", selectedFileInfos);
            this.setResult(700, intent);
            this.finish();
        }

    }
}
