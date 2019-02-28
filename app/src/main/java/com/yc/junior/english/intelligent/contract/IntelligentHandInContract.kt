package com.yc.junior.english.intelligent.contract

import yc.com.base.IDialog
import yc.com.base.IPresenter
import yc.com.base.IView

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