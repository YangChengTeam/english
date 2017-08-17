package com.yc.english.group.contract;

import android.content.Context;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/11 15:42.
 */

public interface GroupCommonClassContract {
    interface View extends IView ,IDialog,ILoading,INoData,INoNet{
        void showCommonClassList(List<ClassInfo> list);

        void showIsMember(int is_member);
    }

    interface Presenter extends IPresenter {
        void  getCommonClassList();
        void applyJoinGroup(String user_id, String sn);
        void isGroupMember(String class_id, String user_id);
    }

}
