package com.yc.junior.english.group.contract;

import android.content.Context;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.group.model.bean.ClassInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/7 11:17.
 */

public interface GroupSyncGroupContract {

    interface View extends IView,ILoading,INoData,INoNet {
        void showMyGroupList(List<ClassInfo> list);
    }

    interface Presenter extends IPresenter {
        void getGroupList(Context context, String user_id,String is_admin,String type);
    }

}
