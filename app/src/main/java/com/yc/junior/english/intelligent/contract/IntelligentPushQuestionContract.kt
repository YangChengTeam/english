package com.yc.junior.english.intelligent.contract

import com.yc.junior.english.intelligent.model.domain.UnitInfoWrapper
import yc.com.base.*

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentPushQuestionContract {
    interface View : IView, ILoading, INoNet, INoData ,IHide{
        fun showInfo(comleteInfo: UnitInfoWrapper.ComleteInfo)
    }

    interface Presenter : IPresenter {

    }
}