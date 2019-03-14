package com.yc.junior.english.setting.contract;

import com.yc.junior.english.group.model.bean.TaskUploadInfo;

import yc.com.base.IDialog;
import yc.com.base.IPresenter;
import yc.com.base.IView;


/**
 * Created by wanglin  on 2017/12/11 15:55.
 */

public interface CameraTaskContract {
    interface View extends IView, IDialog {

//        void showDiscernResult(String data);

        void showUploadResult(TaskUploadInfo data);
    }

    interface Presenter extends IPresenter {
    }
}
