package com.yc.english.intelligent.contract

import android.support.annotation.MainThread
import com.yc.english.base.presenter.IPresenter
import com.yc.english.intelligent.model.domain.UnitInfoWrapper
import yc.com.base.*

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentTypeContract {
    interface View :IView,ILoading,IHide{
        @MainThread
        fun showInfo(titles :Array<String?>, types: Array<UnitInfoWrapper.UnitInfo?>)
        @MainThread
        fun showTitle(title: String)
    }

    interface Presenter : IPresenter {

    }
}