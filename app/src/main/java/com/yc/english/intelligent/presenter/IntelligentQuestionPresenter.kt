package com.yc.english.intelligent.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.hwangjr.rxbus.RxBus
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.SimpleCacheUtils
import com.yc.english.intelligent.contract.IntelligentQuestionContract
import com.yc.english.intelligent.contract.IntelligentTypeContract
import com.yc.english.intelligent.model.engin.IntelligentQuestionEngin
import com.yc.english.intelligent.model.engin.IntelligentTypeEngin

/**
 * Created by zhangkai on 2017/12/6.
 */
open class IntelligentQuestionPresenter :
        BasePresenter<IntelligentQuestionEngin,
                IntelligentQuestionContract.View> {

    constructor(context: Context?, v: IntelligentQuestionContract.View?) : super(context, v) {
        mEngin = IntelligentQuestionEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun getQuestion(unitId: String, type: String) {
        mView.showLoading()
        val s = mEngin.getQuestions(unitId, type).subscribe({
            mView.hideStateView()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data?.list != null) {
                    SimpleCacheUtils.writeCache(mContext, "getQuestion", JSON.toJSONString(it.data?.list))
                    mView.showInfo(it.data?.list!!)
                    return@subscribe
                }
            }
            mView.showNoData()
        }, {
            mView.showNoNet()
        })
        mSubscriptions.add(s)
    }

}