package com.yc.english.group.utils;

import android.media.MediaPlayer;
import android.widget.ImageView;

import com.yc.english.R;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.view.widget.MultifunctionLinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.model.FileInfo;

/**
 * Created by wanglin  on 2017/8/16 09:10.
 */

public class TaskUtil {
    public static void showContextView(ImageView mIvTaskIcon, TaskInfo info, MultifunctionLinearLayout linearLayout) {
        int type = Integer.parseInt(info.getType());
        linearLayout.setText(info.getDesp());
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER:
                if (linearLayout.getType() == MultifunctionLinearLayout.Type.PUBLISH) {
                    mIvTaskIcon.setImageResource(R.mipmap.group36);
                }
                break;
            case GroupConstant.TASK_TYPE_PICTURE:
                if (linearLayout.getType() == MultifunctionLinearLayout.Type.PUBLISH) {
                    mIvTaskIcon.setImageResource(R.mipmap.group40);
                }
                break;
            case GroupConstant.TASK_TYPE_WORD:
                if (linearLayout.getType() == MultifunctionLinearLayout.Type.PUBLISH) {
                    mIvTaskIcon.setImageResource(R.mipmap.group42);
                }
                break;
            case GroupConstant.TASK_TYPE_VOICE:
                if (linearLayout.getType() == MultifunctionLinearLayout.Type.PUBLISH) {
                    mIvTaskIcon.setImageResource(R.mipmap.group38);
                }
                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE:
                if (linearLayout.getType() == MultifunctionLinearLayout.Type.PUBLISH) {
                    mIvTaskIcon.setImageResource(R.mipmap.group44);
                }
                break;

        }
        linearLayout.showSynthesizeView();
        linearLayout.showUrlView(info.getBody().getImgs());
        linearLayout.showVoiceView(getVoiceList(info));
        linearLayout.showFileView(getFileInfos(info));
    }

    private static List<Voice> getVoiceList(TaskInfo taskInfo) {
        List<String> voice = taskInfo.getBody().getVoices();
        List<Voice> voiceList = new ArrayList<>();
        try {
            if (voice != null && voice.size() > 0) {
                for (String s : voice) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
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

    private static List<FileInfo> getFileInfos(TaskInfo taskInfo) {

        List<String> list = taskInfo.getBody().getDocs();
        List<FileInfo> fileInfos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (String s : list) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(s.substring(s.lastIndexOf("/") + 1));
                fileInfo.setFilePath(s);
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }

}
