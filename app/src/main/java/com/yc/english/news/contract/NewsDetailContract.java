package com.yc.english.news.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.news.bean.CourseInfoWrapper;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2017/9/7 10:28.
 */

public interface NewsDetailContract {
    interface View extends IView,ILoading,INoData,INoNet,IHide {
        void showCourseResult(CourseInfoWrapper data);
    }

    interface Presenter extends IPresenter {
        void getWeixinInfo(String news_id);
        void getWeiKeDetail(String news_id,String userId);
    }
}
