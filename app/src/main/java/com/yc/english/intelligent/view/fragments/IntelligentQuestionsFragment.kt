package com.yc.english.intelligent.view.fragments

import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.view.BaseFragment
import com.yc.english.base.view.IView
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.utils.fromHtml
import com.yc.english.intelligent.view.activitys.IntelligentQuestionDescPopupWindow
import com.yc.english.intelligent.view.activitys.IntelligentQuestionsActivity
import com.yc.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_fragment_questions.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionsFragment : BaseFragment<BasePresenter<BaseEngin, IView>>() {

    var questionInfo: QuestionInfoWrapper.QuestionInfo? = null

    init {
        isUseInKotlin = true
    }

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

        val viewTreeObserver = mQestionView.getViewTreeObserver()
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mQestionView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                }
            })
        }

        mAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            if(TextUtils.isEmpty(questionInfo?.desc ?: "")) return@OnOffsetChangedListener

            if(appBarLayout.getBottom()  <= SizeUtils.dp2px(60f)){
                mViewDescBtn.visibility = View.VISIBLE
            } else {
                mViewDescBtn.visibility = View.GONE
            }
        })

        val mFragmentAdapter = TabsUtils.IntelligentInnerQuestionsFragmentAdapter(childFragmentManager, data)
        mViewPager.setAdapter(mFragmentAdapter)
        mViewPager.setCurrentItem(0)
        index = 1
        mViewPager.setOffscreenPageLimit(data.size)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                index = i + 1
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        RxView.clicks(mViewDescBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val ppw = IntelligentQuestionDescPopupWindow(activity!!)
            ppw.loadHtml(questionInfo?.desc ?: "")
            ppw.show(mRootView, Gravity.BOTTOM)
        }
    }

    var index: Int = 1
        set(value) {
            mIndexTextView.text = fromHtml("<font color=#FB4C30>${value}</font>/${mViewPager.adapter?.count ?: 0}")
            field = value
        }

    fun next() {
        if ((activity as IntelligentQuestionsActivity).isHandIn || index >= mViewPager.adapter?.count ?: 0) {
            (activity as IntelligentQuestionsActivity).next()
            return@next
        }
        mViewPager.setCurrentItem(index)
    }

    fun next(index: Int){
        mViewPager.setCurrentItem(index)
    }


    override fun getLayoutId() = R.layout.intelligent_fragment_questions

}