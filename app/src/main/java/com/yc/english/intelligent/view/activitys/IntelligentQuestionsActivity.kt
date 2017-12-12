package com.yc.english.intelligent.view.activitys

import android.content.Intent
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.R
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.AlertDialog
import com.yc.english.base.view.BaseActivity
import com.yc.english.intelligent.contract.IntelligentQuestionContract
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.presenter.IntelligentQuestionPresenter
import com.yc.english.intelligent.view.fragments.IntelligentQuestionsFragment
import com.yc.english.main.model.domain.Constant
import com.yc.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_activity_questions.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionsActivity : BaseActivity<IntelligentQuestionPresenter>(), IntelligentQuestionContract.View {
    var unitId: Int = 0
    var type: String = ""
    var isHandIn = false
    var isResultIn = false

    companion object {
        var INSTANCE: IntelligentQuestionsActivity? = null
        fun getInstance(): IntelligentQuestionsActivity? {
            return INSTANCE
        }
    }

    override fun init() {
        mPresenter = IntelligentQuestionPresenter(this, this)
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbarWarpper.mToolbar, mToolbarWarpper.mStatubar)
        RxView.clicks(mToolbarWarpper.mBackBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            eixt()
        }

        RxView.clicks(mSubmitBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (isResultIn) {
                goToResult()
                return@subscribe
            }

            if (isHandIn) {
                startActivity(Intent(this, IntelligentHandInActivity::class.java))
            }
        }

        unitId = intent.getIntExtra("unitId", 0)
        type = intent.getStringExtra("type")
        isResultIn = intent.getBooleanExtra("isResultIn", false)

        if (isResultIn) {
            mToolbarWarpper.stopTime()
            (mSubmitBtn.parent as ViewGroup).visibility = View.VISIBLE
            (mSubmitBtn.getChildAt(0) as TextView).text = "练习成绩"
        }

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                mToolbarWarpper.index = i + 1
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        mPresenter.getQuestion(unitId.toString(), type)
        INSTANCE = this

    }

    var questionInfos: List<QuestionInfoWrapper.QuestionInfo>? = null
        set(value) {
            field = value
        }

    fun next() {
        if (isHandIn || mToolbarWarpper.index >= mViewPager.adapter?.count ?: 0) {
            startActivity(Intent(this, IntelligentHandInActivity::class.java))
            return@next
        }
        mViewPager.setCurrentItem(mToolbarWarpper.index)
    }

    fun next(actIndex: Int, frgIndex: Int) {
        if (!isResultIn) {
            isHandIn = true
            (mSubmitBtn.parent as ViewGroup).visibility = View.VISIBLE
            (mSubmitBtn.getChildAt(0) as TextView).text = "提交练习"
        }
        mViewPager.setCurrentItem(actIndex)
        (mFragmentAdapter?.getItem(actIndex) as IntelligentQuestionsFragment).next(frgIndex)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constant.RESULT_ANS)))
    fun reset(tag: String) {
        isHandIn = false
        isResultIn = true
        for (i in 0..((mFragmentAdapter?.count ?: 0) - 1)) {
            (mFragmentAdapter?.getItem(i) as IntelligentQuestionsFragment).next(0)
        }
        mViewPager.setCurrentItem(0)
        showSubmit()
    }


    var mFragmentAdapter: TabsUtils.IntelligentQuestionsFragmentAdapter? = null

    private fun showSubmit() {
        if (!isResultIn) {
            (mSubmitBtn.parent as ViewGroup).visibility = View.VISIBLE
            (mSubmitBtn.getChildAt(0) as TextView).text = "提交练习"
        } else {
            (mSubmitBtn.parent as ViewGroup).visibility = View.VISIBLE
            (mSubmitBtn.getChildAt(0) as TextView).text = "练习成绩"
        }
    }

    override fun showInfo(list: List<QuestionInfoWrapper.QuestionInfo>) {
        questionInfos = list
        mToolbarWarpper.total = list.size
        mToolbarWarpper.index = 1
        mViewPager.setOffscreenPageLimit(list.size)
        mFragmentAdapter = TabsUtils.IntelligentQuestionsFragmentAdapter(supportFragmentManager, list)
        mViewPager.setAdapter(mFragmentAdapter)
        mViewPager.setCurrentItem(0)
        if (!isResultIn) {
            mToolbarWarpper.startTime()
        } else {
            mToolbarWarpper.mTimeTextView.text = SPUtils.getInstance().getString("unitInfo-complete-time-${unitId}${type}", "")
            goToResult()
        }
    }

    private fun goToResult() {
        isResultIn = true
        intent = Intent(this, IntelligentResultActivity::class.java)
        intent.putExtra("unitId", unitId)
        intent.putExtra("type", type)
        startActivity(intent)
    }

    fun usedTime(): String {
        return mToolbarWarpper.mTimeTextView.text.toString()
    }

    override fun getLayoutId() = R.layout.intelligent_activity_questions

    override fun hideStateView() {
        mStateView.hide()
    }

    override fun showNoNet() {
        mStateView.showNoNet(mViewPager, HttpConfig.NET_ERROR, {
            mPresenter.getQuestion(unitId.toString(), type)
        })
    }

    override fun showNoData() {
        mStateView.showNoData(mViewPager)
    }

    override fun showLoading() {
        mStateView.showLoading(mViewPager)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            eixt()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun eixt() {
        if (isResultIn) {
            goToResult()
            return
        }
        val alertDialog = AlertDialog(this)
        alertDialog.setDesc("确定退出练习？")
        alertDialog.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
        alertDialog.show()
    }
}