package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.model.bean.TaskUploadInfo;

import java.io.File;

/**
 * Created by wanglin  on 2017/8/8 16:12.
 */

public interface GroupDoTaskDetailContract {
    interface View extends IView, IFinish, IDialog,INoData,INoNet,ILoading {
        void showTaskDetail(TaskInfo info);

        void showUploadResult(TaskUploadInfo data);

        void showFile();

        void showDoneWorkResult(TaskInfo data);
    }

    interface Presenter extends IPresenter {

        void getPublishTaskDetail(Context context, String task_id, String class_id, String user_id);

        void uploadFile(Context context, File file, String fileName, String name);

        void doTask(String class_id, String user_id, String task_id, String desp, String imgs, String voices, String docs);

        void getDoneTaskDetail(Context context,String id, String user_id);
    }
}
