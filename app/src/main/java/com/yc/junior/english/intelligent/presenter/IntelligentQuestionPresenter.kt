package com.yc.junior.english.intelligent.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.hwangjr.rxbus.RxBus
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.junior.english.base.presenter.BasePresenter
import com.yc.junior.english.base.utils.SimpleCacheUtils
import com.yc.junior.english.intelligent.contract.IntelligentQuestionContract
import com.yc.junior.english.intelligent.model.engin.IntelligentQuestionEngin
import com.yc.junior.english.main.hepler.UserInfoHelper
import com.yc.junior.english.main.model.domain.Constant
import yc.com.blankj.utilcode.util.SPUtils

/**
 * Created by zhangkai on 2017/12/6.
 */
open class IntelligentQuestionPresenter :
        BasePresenter<IntelligentQuestionEngin,
                IntelligentQuestionContract.View> {

    constructor(context: Context?, v: IntelligentQuestionContract.View?) : super(context, v) {
        mEngine = IntelligentQuestionEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun getQuestion(unitId: String, type: String) {
        mView.showLoading()
        val s = mEngine.getQuestions(unitId, type).subscribe({
            mView.hide()
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
            mView.hide()
            mView.showNoNet()
        })
        mSubscriptions.add(s)
    }

    fun getPlanDetail(report_id: String, type: String) {
        mView.showLoading()
        val s = mEngine.getPlanDetail(report_id, type).subscribe({
            mView.hide()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data?.list != null) {
                    SimpleCacheUtils.writeCache(mContext, "getQuestion${report_id}${type}", JSON.toJSONString(it.data?.list))
                    mView.showInfo(it.data?.list!!, it.data?.use_time)
                    return@subscribe
                }
            }
            mView.showNoData(it?.message ?: "根据人教版教材教学大纲进度，此单元不在本时段学习范围内，暂不开放，请选择其他单元学习！")
        }, {
            mView.hide()
            mView.showNoNet()
        })
        mSubscriptions.add(s)
    }

    fun removeAnswer(unitId: String, kpoint_type: String, test_type: String) {
        val subscription = mEngine.removeAnswer(unitId, kpoint_type, test_type).subscribe({
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                SPUtils.getInstance().put("finish${UserInfoHelper.getUserInfo().uid}-unitId$unitId$kpoint_type", 0)
                RxBus.get().post(Constant.REMOVE_ANSWER, kpoint_type)
                mView.finish()
            }
        })
        mSubscriptions.add(subscription)
    }



}