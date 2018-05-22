package com.yc.english.intelligent.contract

import android.support.annotation.MainThread
import com.yc.english.base.presenter.IPresenter
import com.yc.english.base.view.*
import com.yc.english.intelligent.model.domain.UnitInfoWrapper

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