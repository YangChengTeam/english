package com.yc.english.weixin.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.weixin.model.domain.CourseInfo;

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
