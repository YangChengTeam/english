package com.yc.english.intelligent.presenter

import android.content.Context
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.intelligent.contract.IntelligentHandInContract
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.engin.IntelligentHandInEngin
import yc.com.base.BasePresenter

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentHandInPresenter : BasePresenter<IntelligentHandInEngin,
        IntelligentHandInContract.View> {
    constructor(context: Context?, v: IntelligentHandInContract.View?) : super(context, v) {
        mEngine = IntelligentHandInEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }

    fun submitAnswers(questionInfoList: List<QuestionInfoWrapper.QuestionInfo>, use_time: String) {
        mView.showLoadingDialog("正在提交...")
        var answersList = "["
        for (i in 0..(questionInfoList.size - 1)) {
            val questionInfo = questionInfoList.get(i)
            if (questionInfo.count > 0) {
                continue
            }
            answersList += "{\"topic_id\":${questionInfo.id}, \"user_answer\": \"${questionInfo.userAnswer}\"}"
            if (i != questionInfoList.size - 1) {
                answersList += ","
            }
        }
        answersList += "]"
        mEngine.submitAnwsers(answersList, use_time).subscribe({
            mView.dismissDialog()
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                mView.showSuccess(it?.message ?: "提交成功")
            } else {
                mView.showFail("提交失败，请重试")
            }
        }, {
            mView.dismissDialog()
            mView.showFail("服务器故障，请重试")
        })
    }
}