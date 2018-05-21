package com.yc.junior.english.group.view.activitys;

import android.content.Intent;
import android.net.Uri;

import com.yc.junior.english.base.helper.TipsHelper;

import io.rong.imkit.activity.FilePreviewActivity;

/**
 * Created by wanglin  on 2017/8/28 17:31.
 */

public class FilePreviewExActivity extends FilePreviewActivity {
    @Override
    public void openFile(String fileName, String fileSavePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + fileSavePath);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (fileName.endsWith(".m4a") || fileName.endsWith(".mp3")
                || fileName.endsWith(".ape") || fileName.endsWith(".wav") || fileName.endsWith(".wma")) {
            intent.setDataAndType(uri, "audio/*");

        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".3gp") || fileName.endsWith(".mov")) {
            intent.setDataAndType(uri, "video/*");

        } else {
            super.openFile(fileName, fileSavePath);
            return;
        }
        try {
            startActivity(intent);
            finish();
        } catch (Exception e) {
            TipsHelper.tips(this, "未知文件类型");
            e.printStackTrace();
        }
    }
}
