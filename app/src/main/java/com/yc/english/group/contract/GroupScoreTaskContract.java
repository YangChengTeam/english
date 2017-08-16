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

/**
 * Created by wanglin  on 2017/8/10 21:23.
 */

public interface GroupScoreTaskContract {
    interface View extends IView,IFinish,ILoading,INoData,INoNet,IDialog {
        void showDoneTaskInfo(TaskInfo info);


        void showPublishTaskInfo(TaskInfo info);


        void showScoreResult();
    }

    interface Presenter extends IPresenter {

        void getDoneTaskDetail(Context context, String id, String user_id);

        void getPublishTaskDetail(Context context, String task_id, String class_id, String user_id);

        void taskScore(Context context,String id, String score);
    }
}
