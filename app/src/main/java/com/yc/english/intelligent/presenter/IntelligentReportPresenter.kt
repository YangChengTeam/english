package com.yc.english.intelligent.presenter

import android.content.Context
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.intelligent.contract.IntelligentReportContract
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.engin.IntelligentReportEngin

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentReportPresenter : BasePresenter<IntelligentReportEngin,
        IntelligentReportContract.View> {
    constructor(context: Context?, v: IntelligentReportContract.View?) : super(context, v) {
        mEngin = IntelligentReportEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun getReportInfo(unit_id: String, use_time: String) {

    }
}