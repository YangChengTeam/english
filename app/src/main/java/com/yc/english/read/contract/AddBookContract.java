package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.CourseVersionInfo;
import com.yc.english.read.model.domain.GradeInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface AddBookContract {
    interface View extends IView, IDialog, IFinish {

        void showGradeListData(List<GradeInfo> gradeInfos);

        void showCVListData(List<CourseVersionInfo> list);
    }

    interface Presenter extends IPresenter {

        void gradeList();//获取年级集合

        void getCVListByGradeId(String gradeId,String partType);//根据查询条件获取教材集合

        void getGradeListFromLocal();//获取本地数据
    }
}
