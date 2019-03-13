package com.yc.english.weixin.contract;

import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public interface CourseContract {
    interface View extends IView, ILoading, INoData, INoNet,IHide{
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
