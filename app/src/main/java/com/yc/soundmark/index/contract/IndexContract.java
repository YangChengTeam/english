package com.yc.soundmark.index.contract;

import com.yc.soundmark.index.model.domain.ContactInfo;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/10/29 09:10.
 */
public interface IndexContract {

    interface View extends IView {
        void showIndexInfo(ContactInfo contactInfo);
    }

    interface Presenter extends IPresenter {
        void getIndexInfo();
    }
}
