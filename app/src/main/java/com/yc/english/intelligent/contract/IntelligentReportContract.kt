package com.yc.english.intelligent.contract

import com.yc.english.base.presenter.IPresenter
import com.yc.english.base.view.*
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.domain.ReportInfo

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentReportContract {
    interface View : IView, ILoading, INoData, INoNet {
        fun showInfo(reportInfo: ReportInfo)
        fun showPlanDetail(data: List<QuestionInfoWrapper.QuestionInfo>)
    }

    interface Presenter : IPresenter {

    }
}