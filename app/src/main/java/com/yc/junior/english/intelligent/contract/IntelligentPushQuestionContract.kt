package com.yc.junior.english.intelligent.contract

import com.yc.junior.english.base.presenter.IPresenter
import com.yc.junior.english.base.view.*
import com.yc.junior.english.intelligent.model.domain.UnitInfoWrapper

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentPushQuestionContract {
    interface View : IView, ILoading, INoNet, INoData {
        fun showInfo(comleteInfo: UnitInfoWrapper.ComleteInfo)
    }

    interface Presenter : IPresenter {

    }
}