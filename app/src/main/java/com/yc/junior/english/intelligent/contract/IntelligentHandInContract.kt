package com.yc.junior.english.intelligent.contract

import com.yc.junior.english.base.presenter.IPresenter
import com.yc.junior.english.base.view.IDialog
import com.yc.junior.english.base.view.IView

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentHandInContract {
    interface View : IView, IDialog {
        fun showSuccess(msg: String)
        fun showFail(msg: String)
    }

    interface Presenter : IPresenter {

    }
}