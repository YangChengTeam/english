package com.yc.junior.english.intelligent.view.fragments

import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.yc.junior.english.R
import com.yc.junior.english.base.model.BaseEngin
import com.yc.junior.english.base.presenter.BasePresenter
import com.yc.junior.english.base.view.BaseFragment
import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.junior.english.intelligent.utils.fromHtml
import com.yc.junior.english.intelligent.view.activitys.IntelligentQuestionDescPopupWindow
import com.yc.junior.english.intelligent.view.activitys.IntelligentQuestionsActivity
import com.yc.junior.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_fragment_questions.*
import yc.com.base.IView
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionsFragment : BaseFragment<BasePresenter<BaseEngin, IView>>() {

    var questionInfo: QuestionInfoWrapper.QuestionInfo? = null

    init {
        isUseInKotlin = true
    }

    lateinit var mFragmentAdapter: TabsUtils.IntelligentInnerQuestionsFragmentAdapter
    override fun init() {
        mQestionView.text = questionInfo?.title
        mQestionView.webview = questionInfo?.desc
        mQestionView.media = questionInfo?.voiceUrl
        var data: List<QuestionInfoWrapper.QuestionInfo>

        if (questionInfo?.data != null && questionInfo?.data!!.size > 1) {
            mSmallIndexRelativeLayout.visibility = View.VISIBLE
            data = questionInfo?.data!!
        } else {
            mSmallIndexRelativeLayout.visibility = View.GONE
            data = arrayListOf(questionInfo ?: QuestionInfoWrapper.QuestionInfo())
        }

        mFragmentAdapter = TabsUtils.IntelligentInnerQuestionsFragmentAdapter(childFragmentManager, data)
        mViewPager.setAdapter(mFragmentAdapter)
        mViewPager.setCurrentItem(0)
        index = 1
        mViewPager.setOffscreenPageLimit(1)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                for (j in 0..(mFragmentAdapter.count - 1)) {
                    if(mFragmentAdapter.getItem(j).isAdded) {
                        (mFragmentAdapter.getItem(j) as IntelligentInnerQuestionFragment).stop()
                    }
                }
                index = i + 1
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        RxView.clicks(mViewDescBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val ppw = IntelligentQuestionDescPopupWindow(activity!!)
            if (questionInfo?.example != "") {
                ppw.loadHtml(questionInfo?.example ?: "", 1)
            } else if (questionInfo?.voiceText != "") {
                ppw.loadHtml(questionInfo?.voiceText ?: "", 2)
            }
            ppw.show(mRootView, Gravity.BOTTOM)
        }

        showDescView()

    }

    fun showDescView(){
        if ((!TextUtils.isEmpty(questionInfo?.example) || !TextUtils.isEmpty(questionInfo?.voiceText)) &&
                IntelligentQuestionsActivity
                        .getInstance()?.isResultIn ?: false) {
            if (questionInfo?.type == "hearing") {
                mViewDescTextView.text = "原文"
            } else if (questionInfo?.type == "writing") {
                mViewDescTextView.text = "范文"
            }
            mViewDescBtn.visibility = View.VISIBLE

        } else {
            mViewDescBtn.visibility = View.GONE
        }

    }

    var index: Int = 0
        set(value) {
            if (IntelligentQuestionsActivity.getInstance()?.type.equals("hearing") || IntelligentQuestionsActivity.getInstance()?.type.equals("vocabulary")) {
                val pindex = questionInfo?.actIndex ?: 0
                val maxIndex = IntelligentQuestionsActivity.getInstance()?.getMaxIndex(pindex - 1)
                val count = IntelligentQuestionsActivity.getInstance()?.getTotalCount()
                mIndexTextView.text = fromHtml("<font color=#FB4C30>${maxIndex?.plus(value)}</font>/${count}")
            } else {
                mIndexTextView.text = fromHtml("<font color=#FB4C30>${value}</font>/${mViewPager.adapter?.count ?: 0}")
            }
            field = value
        }

    fun next() {
        if ((activity as IntelligentQuestionsActivity).isHandIn || index >= mViewPager.adapter?.count ?: 0) {
            (activity as IntelligentQuestionsActivity).next()
            return@next
        }
        mViewPager.setCurrentItem(index)
    }


    fun stop() {
        if (mQestionView.mDescAudioPlayerView.visibility == View.VISIBLE)
            mQestionView.mDescAudioPlayerView.pause()
    }

    fun next(index: Int) {
        mViewPager.setCurrentItem(index)
    }




    override fun getLayoutId() = R.layout.intelligent_fragment_questions

}