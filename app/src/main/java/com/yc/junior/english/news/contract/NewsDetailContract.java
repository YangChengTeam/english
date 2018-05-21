package com.yc.junior.english.news.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.news.bean.CourseInfoWrapper;

/**
 * Created by wanglin  on 2017/9/7 10:28.
 */

public interface NewsDetailContract {
    interface View extends IView,ILoading,INoData,INoNet {
        void showCourseResult(CourseInfoWrapper data);
    }

    interface Presenter extends IPresenter {
        void getWeixinInfo(String news_id);
        void getWeiKeDetail(String news_id,String userId);
    }
}
