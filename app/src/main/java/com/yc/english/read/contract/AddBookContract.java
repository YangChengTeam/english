package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;

import java.util.ArrayList;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface AddBookContract {
    interface View extends IView, IDialog, IFinish {

        void showGradeListData(ArrayList<GradeInfo> list);

        void showCVListData(ArrayList<CourseVersionInfo> list);
    }

    interface Presenter extends IPresenter {

        void gradeList();//获取年级集合

        void getCVListByGradeId(String gradeId);//根据年级ID获取教材集合
    }
}
