package com.yc.junior.english.weixin.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.weixin.model.domain.CourseInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public interface CourseContract {
    interface View extends IView, ILoading, INoData, INoNet {
        void showWeixinList(List<CourseInfo> list);
        void fail();
        void end();
    }

    interface Presenter extends IPresenter {
        void getWeiXinList(String type_id, String page,
                           String
                                   page_size);
    }
}
