package com.yc.junior.english.setting.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TaskUploadInfo;

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
