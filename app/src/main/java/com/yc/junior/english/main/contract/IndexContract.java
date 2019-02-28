package com.yc.junior.english.main.contract;

import com.yc.junior.english.main.model.domain.IndexInfo;
import com.yc.junior.english.main.model.domain.SlideInfo;
import com.yc.junior.english.main.model.domain.UserInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public interface IndexContract {
    interface View extends IView, ILoading, INoNet, INoData,IHide {
        void showInfo(IndexInfo indexInfo, boolean isCached);

        void showAvatar(UserInfo userInfo);

        void showBanner(List<String> images);
    }

    interface Presenter extends IPresenter {
        void getIndexInfo(boolean isFresh);

        void getAvatar();

        SlideInfo getSlideInfo(int position);
    }
}
