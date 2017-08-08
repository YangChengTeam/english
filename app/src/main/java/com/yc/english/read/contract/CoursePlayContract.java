package com.yc.english.read.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.IView;
import com.yc.english.read.model.domain.EnglishCourseInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface CoursePlayContract {
    interface View extends IView, IDialog, IFinish {
        void showCourseListData(List<EnglishCourseInfo> englishCourseInfos);
    }

    interface Presenter extends IPresenter {
        void getCourseList(int currentPage, int pageCount);//获取教材集合
    }
}
