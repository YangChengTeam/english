package com.yc.junior.english.read.contract;

import com.yc.junior.english.read.model.domain.EnglishCourseInfoList;

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

public interface CoursePlayContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet,IHide {
        void showCourseListData(EnglishCourseInfoList englishCourseInfoList);
    }

    interface Presenter extends IPresenter {
        void getCourseListByUnitId(int currentPage, int pageCount, String uintId);//根据单元ID获取课程列表集合
    }
}
