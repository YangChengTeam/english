package com.yc.english.group.contract;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;
import com.yc.english.group.model.bean.TokenInfo;

/**
 * Created by wanglin  on 2017/7/31 15:12.
 */

public interface TokenContract {
    interface View extends IView{
        void contact(TokenInfo tokenInfo);
    }

    interface Presenter extends IPresenter{
        void getToken();
    }
}
