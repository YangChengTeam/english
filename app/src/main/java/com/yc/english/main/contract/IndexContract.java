package com.yc.english.main.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.main.model.domain.CountInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.model.domain.UserInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/26.
 */

public interface IndexContract {
    interface View extends IView, ILoading, INoNet, INoData {
        void showBanner(List<String> images);
        void showAvatar(UserInfo userInfo);
        void showCountInfo(CountInfo countInfo);

    }
    interface Presenter extends IPresenter {
        void getIndexInfo();
        void getAvatar();
        SlideInfo getSlideInfo(int position);
    }
}
