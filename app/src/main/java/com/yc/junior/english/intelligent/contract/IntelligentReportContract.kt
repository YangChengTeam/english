package com.yc.junior.english.intelligent.contract

import com.yc.junior.english.base.presenter.IPresenter
import com.yc.junior.english.base.view.*
import com.yc.junior.english.intelligent.model.domain.ReportInfo

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentReportContract {
    interface View : IView, ILoading, INoData, INoNet {
        fun showInfo(reportInfo: ReportInfo)
    }

    interface Presenter : IPresenter {

    }
}