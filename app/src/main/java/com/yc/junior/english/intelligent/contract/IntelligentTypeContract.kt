package com.yc.junior.english.intelligent.contract

import android.support.annotation.MainThread
import com.yc.junior.english.intelligent.model.domain.UnitInfoWrapper
import yc.com.base.IHide
import yc.com.base.ILoading
import yc.com.base.IView



/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentTypeContract {
    interface View : IView, ILoading, IHide {
        @MainThread
        fun showInfo(titles :Array<String?>, types: Array<UnitInfoWrapper.UnitInfo?>)
        @MainThread
        fun showTitle(title: String)
    }

    interface Presenter {

    }
}