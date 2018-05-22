package com.yc.english.intelligent.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.SimpleCacheUtils
import com.yc.english.intelligent.contract.IntelligentQuestionContract
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.domain.URLConfig
import com.yc.english.intelligent.model.engin.IntelligentQuestionEngin
import com.yc.english.main.hepler.UserInfoHelper
import rx.Observable

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
                    SimpleCacheUtils.writeCache(mContext, "getQuestion${unitId}${type}", JSON.toJSONString(it.data?.list))
                    mView.showInfo(it.data?.list!!, it?.data?.use_time)
                    return@subscribe
                }
            }
            mView.showNoData(it?.message ?: "根据人教版教材教学大纲进度，此单元不在本时段学习范围内，暂不开放，请选择其他单元学习！")
        }, {
            mView.hideStateView()
            mView.showNoNet()
        })
        mSubscriptions.add(s)
    }

    fun getPlanDetail(report_id: String, type: String) {
        mView.showLoading()
        val s = mEngin.getPlanDetail(report_id, type).subscribe({
            mView.hideStateView()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data?.list != null) {
                    SimpleCacheUtils.writeCache(mContext, "getQuestion${report_id}${type}", JSON.toJSONString(it.data?.list))
                    mView.showInfo(it.data?.list!!, it?.data?.use_time)
                    return@subscribe
                }
            }
            mView.showNoData(it?.message ?: "根据人教版教材教学大纲进度，此单元不在本时段学习范围内，暂不开放，请选择其他单元学习！")
        }, {
            mView.hideStateView()
            mView.showNoNet()
        })
        mSubscriptions.add(s)
    }

}