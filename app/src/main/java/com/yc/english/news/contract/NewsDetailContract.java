package com.yc.english.news.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.news.bean.CourseInfoWrapper;

/**
 * Created by wanglin  on 2017/9/7 10:28.
 */

public interface NewsDetailContract {
    interface View extends IView,ILoading,INoData,INoNet {
        void showCourseResult(CourseInfoWrapper data);
    }

    interface Presenter extends IPresenter {
        void getWeixinInfo(String news_id);
    }
}
