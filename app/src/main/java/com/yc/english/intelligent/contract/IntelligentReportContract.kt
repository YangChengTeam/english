package com.yc.english.intelligent.contract

import android.support.annotation.MainThread
import com.yc.english.base.presenter.IPresenter
import com.yc.english.base.view.*
import com.yc.english.intelligent.model.domain.ReportInfo
import com.yc.english.intelligent.model.domain.UnitInfoWrapper

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