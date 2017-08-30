package com.yc.english.community.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IDialog;
import com.yc.english.base.view.IFinish;
import com.yc.english.base.view.ILoading;
import com.yc.english.base.view.INoData;
import com.yc.english.base.view.INoNet;
import com.yc.english.base.view.IView;
import com.yc.english.community.model.domain.CommunityInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface CommunityInfoContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet {
        void showCommunityInfoListData(List<CommunityInfo> list);
    }

    interface Presenter extends IPresenter {
        void communityInfoList(int currentPage, int pageCount);
    }
}
