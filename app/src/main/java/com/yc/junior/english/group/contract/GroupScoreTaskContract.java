package com.yc.junior.english.group.contract;

import android.content.Context;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.TaskInfo;

/**
 * Created by wanglin  on 2017/8/10 21:23.
 */

public interface GroupScoreTaskContract {
    interface View extends IView, IFinish, ILoading, INoData, INoNet, IDialog {
        void showDoneTaskInfo(TaskInfo info);


        void showPublishTaskInfo(TaskInfo info);

    }

    interface Presenter extends IPresenter {

        void getDoneTaskDetail(Context context, String id, String user_id);

        void getPublishTaskDetail(Context context, String task_id, String class_id, String user_id);

        void taskScore(Context context, String id, String score);
    }
}
