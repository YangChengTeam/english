package com.yc.english.intelligent.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.SimpleCacheUtils
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

    fun getReportInfo(unit_id: String) {
        mView.showLoading()
        mEngin.getReportInfo(unit_id, "").subscribe({
            mView.hideStateView()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data != null) {
                    mView.showInfo(it.data)
                    return@subscribe
                }
            }
            mView.showNoData()
        }, {
            mView.hideStateView()
            mView.showNoNet()
        })
    }
}