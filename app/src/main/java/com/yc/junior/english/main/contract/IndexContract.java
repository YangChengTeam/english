package com.yc.junior.english.main.contract;

import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.main.model.domain.IndexInfo;
import com.yc.junior.english.main.model.domain.SlideInfo;
import com.yc.junior.english.main.model.domain.UserInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/26.
 */

public interface IndexContract {
    interface View extends IView, ILoading, INoNet, INoData {
        void showInfo(IndexInfo indexInfo);
        void showAvatar(UserInfo userInfo);
        void showBanner(List<String> images);
    }
    interface Presenter extends IPresenter {
        void getIndexInfo();
        void getAvatar();
        SlideInfo getSlideInfo(int position);
    }
}
