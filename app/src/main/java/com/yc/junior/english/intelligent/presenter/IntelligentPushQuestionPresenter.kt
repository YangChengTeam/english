package com.yc.junior.english.intelligent.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.junior.english.base.presenter.BasePresenter
import com.yc.junior.english.intelligent.contract.IntelligentPushQuestionContract
import com.yc.junior.english.intelligent.model.engin.IntelligentPushQuestionEngin

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentPushQuestionPresenter : BasePresenter<IntelligentPushQuestionEngin,
        IntelligentPushQuestionContract.View> {
    constructor(context: Context?, v: IntelligentPushQuestionContract.View?) : super(context, v) {
        mEngine = IntelligentPushQuestionEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun getPlan(report_id: String) {
        mView.showLoading()
        mEngine.getPlan(report_id).subscribe({
            mView.hide()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data != null) {
                    mView.showInfo(it.data)
                    return@subscribe
                }
            }
            mView.showNoData()
        }, {
            mView.hide()
            mView.showNoNet()
        })
    }
}