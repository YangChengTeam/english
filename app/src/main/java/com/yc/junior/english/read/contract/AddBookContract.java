package com.yc.junior.english.read.contract;

import com.yc.junior.english.read.model.domain.CourseVersionInfo;
import com.yc.junior.english.read.model.domain.GradeInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;


/**
 * Created by zhangkai on 2017/7/25.
 */

public interface AddBookContract {
    interface View extends IView, IDialog, IFinish,ILoading, INoData, INoNet,IHide {

        void showGradeListData(List<GradeInfo> gradeInfos);

        void showCVListData(List<CourseVersionInfo> list);
    }

    interface Presenter extends IPresenter {

        void gradeList();//获取年级集合

        void getCVListByGradeId(String gradeId,String partType);//根据查询条件获取教材集合

        void getGradeListFromLocal();//获取本地数据
    }
}
