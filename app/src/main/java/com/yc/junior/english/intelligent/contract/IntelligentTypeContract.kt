package com.yc.junior.english.intelligent.contract

import android.support.annotation.MainThread
import com.yc.junior.english.base.presenter.IPresenter
import com.yc.junior.english.base.view.ILoading
import com.yc.junior.english.base.view.IView
import com.yc.junior.english.intelligent.model.domain.UnitInfoWrapper

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentTypeContract {
    interface View : IView, ILoading {
        @MainThread
        fun showInfo(titles :Array<String?>, types: Array<UnitInfoWrapper.UnitInfo?>)
        @MainThread
        fun showTitle(title: String)
    }

    interface Presenter : IPresenter {

    }
}