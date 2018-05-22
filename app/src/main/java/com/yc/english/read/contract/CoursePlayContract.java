package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.EnglishCourseInfoList;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface CoursePlayContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet {
        void showCourseListData(EnglishCourseInfoList englishCourseInfoList);
    }

    interface Presenter extends IPresenter {
        void getCourseListByUnitId(int currentPage, int pageCount, String uintId);//根据单元ID获取课程列表集合
    }
}
