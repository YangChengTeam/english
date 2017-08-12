package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TaskInfo;

import java.io.File;

/**
 * Created by wanglin  on 2017/8/8 17:36.
 */

public interface GroupDoneTaskDetailContract {
    interface View extends IView ,IFinish{
        void showDoneTaskDetail(TaskInfo info);

        void showPublishTaskDetail(TaskInfo info);
    }

    interface Presenter extends IPresenter {

        void getDoneTaskDetail(Context context,String id, String user_id);

        void getPublishTaskDetail(Context context, String task_id, String class_id, String user_id);

        void updateDoTask(String id, String user_id, String desp, String imgs, String voices, String docs);

        void uploadFile(Context context, File file, String fileName, String name);
    }
}
