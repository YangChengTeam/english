package com.yc.english.intelligent.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.intelligent.contract.IntelligentReportContract
import com.yc.english.intelligent.model.engin.IntelligentReportEngin

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentReportPresenter : BasePresenter<IntelligentReportEngin,
        IntelligentReportContract.View> {
    constructor(context: Context?, v: IntelligentReportContract.View?) : super(context, v) {
        mEngine = IntelligentReportEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun getReportInfo(unit_id: String) {
        mView.showLoading()
        mEngine.getReportInfo(unit_id, "").subscribe({
            mView.hide()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data != null) {
                    mView.showInfo(it.data)
//                    getPlanDetail("" + it.data.id, "grammar")
                    return@subscribe
                }
            }
            mView.showNoData()
        }, {
            mView.hide()
            mView.showNoNet()
        })
    }

    fun getPlanDetail(report_id: String, type: String) {

        mEngine.getPlanDetail(report_id, type).subscribe({
            val code = it.code
            if (code == HttpConfig.STATUS_OK) {
                if (it.data != null && it.data.list != null) {
//                    mView.showPlanDetail(it.data.list!!)
                    return@subscribe
                }
            }
        })

    }


}