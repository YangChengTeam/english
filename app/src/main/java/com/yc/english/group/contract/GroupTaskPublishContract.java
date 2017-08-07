package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

import java.io.File;
import java.util.List;

/**
 * Created by wanglin  on 2017/8/7 10:47.
 */

public interface GroupTaskPublishContract {

    interface View extends IView,IDialog {
        void showGroupInfo(ClassInfo info);

        void showTaskDetail();

        void showMyGroupList(List<ClassInfo> list);
    }

    interface Presenter extends IPresenter {
        void publishTask(String class_ids, String publisher, String desp,byte[] imges, byte[] voices, byte[] docs);

        void getGroupInfo(Context context, String id);

        void uploadFile(File file);
    }
}
