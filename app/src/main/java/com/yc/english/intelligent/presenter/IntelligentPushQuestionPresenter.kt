package com.yc.english.intelligent.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.intelligent.contract.IntelligentPushQuestionContract
import com.yc.english.intelligent.model.engin.IntelligentPushQuestionEngin

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentPushQuestionPresenter : BasePresenter<IntelligentPushQuestionEngin,
        IntelligentPushQuestionContract.View> {
    constructor(context: Context?, v: IntelligentPushQuestionContract.View?) : super(context, v) {
        mEngin = IntelligentPushQuestionEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun getPlan(report_id: String) {
        mView.showLoading()
        mEngin.getPlan(report_id).subscribe({
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