package com.yc.english.intelligent.contract

import com.yc.english.base.presenter.IPresenter
import com.yc.english.intelligent.model.domain.UnitInfoWrapper
import yc.com.base.*

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentPushQuestionContract {
    interface View:IView,ILoading,INoData,INoNet,IHide {
        fun showInfo(comleteInfo: UnitInfoWrapper.ComleteInfo)
    }

    interface Presenter : IPresenter {

    }
}