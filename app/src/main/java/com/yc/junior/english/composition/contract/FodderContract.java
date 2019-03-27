package com.yc.junior.english.composition.contract;

import com.yc.junior.english.composition.model.bean.FodderInfo;

import java.util.List;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2019/3/25 09:44.
 */
public interface FodderContract {

    interface View extends IView {
        void showFodderInfos(List<FodderInfo> fodderInfos);
    }

    interface Presenter extends IPresenter {
        void getFodderIndexInfo();
    }
}
