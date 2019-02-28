package com.yc.junior.english.intelligent.contract

import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.junior.english.intelligent.model.domain.ReportInfo
import yc.com.base.*

/**
 * Created by zhangkai on 2017/12/5.
 */

interface IntelligentReportContract {
    interface View : IView, ILoading, INoData, INoNet,IHide {
        fun showInfo(reportInfo: ReportInfo)
        fun showPlanDetail(data: List<QuestionInfoWrapper.QuestionInfo>)
    }

    interface Presenter : IPresenter {

    }
}