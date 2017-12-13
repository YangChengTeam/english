package com.yc.english.intelligent.contract

import android.support.annotation.MainThread
import com.yc.english.base.presenter.IPresenter
import com.yc.english.base.view.IDialog
import com.yc.english.base.view.ILoading
import com.yc.english.base.view.IView
import com.yc.english.intelligent.model.domain.UnitInfoWrapper

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentReportContract {
    interface View : IView, IDialog {
        fun showSuccess(msg: String)
        fun showFail(msg: String)
    }

    interface Presenter : IPresenter {

    }
}