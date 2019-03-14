package com.yc.junior.english.intelligent.view.fragments

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.yc.junior.english.R
import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.junior.english.intelligent.view.activitys.IntelligentQuestionsActivity
import com.yc.junior.english.intelligent.view.adpaters.IntelligentOptionsAdapter
import com.yc.junior.english.main.model.domain.Constant
import kotlinx.android.synthetic.main.intelligent_fragment_inner_question.*
import yc.com.base.BaseEngine
import yc.com.base.BaseFragment
import yc.com.base.BasePresenter
import yc.com.base.IView
import yc.com.blankj.utilcode.util.SPUtils

/**
 * Created by zhangkai on 2017/12/4.
 */

class IntelligentInnerQuestionFragment : BaseFragment<BasePresenter<BaseEngine, IView>>() {

    var questionInfo: QuestionInfoWrapper.QuestionInfo? = null
    var adpater: IntelligentOptionsAdapter? = null

    init {
        isUseInKotlin = true
    }

    override fun init() {
        adpater = IntelligentOptionsAdapter(questionInfo, (IntelligentQuestionsActivity.getInstance()?.isResultIn ?: false))
        mQuestionView.mRecyclerView.adapter = adpater
        adpater?.setNewData(questionInfo?.options?.options)

        mQuestionView.text = questionInfo?.title
        mQuestionView.media = questionInfo?.voiceUrl

        adpater?.onItemClickListener = object : BaseQuickAdapter.OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (IntelligentQuestionsActivity.getInstance()?.isResultIn ?: false) return
                questionInfo?.userAnswer = (65 + position).toChar().toString()
                adapter?.notifyDataSetChanged()
                SPUtils.getInstance().put("userAnswer${questionInfo?.id}", questionInfo?.userAnswer ?: "")
                (parentFragment as IntelligentQuestionsFragment).next()
            }
        }
        showAns("")
    }

    fun stop() {
        if (mQuestionView.mTitleAudioPlayerView.visibility == View.VISIBLE)
            mQuestionView.mTitleAudioPlayerView.pause()
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constant.RESULT_ANS)))
    fun showAns(tag: String) {
        if (IntelligentQuestionsActivity.getInstance()?.isResultIn ?: false) {
            mQuestionView.analysis(questionInfo?.answer, questionInfo?.analysis)
            adpater?.isResult = true
        }

        adpater?.notifyDataSetChanged()
    }

    override fun getLayoutId() = R.layout.intelligent_fragment_inner_question
}