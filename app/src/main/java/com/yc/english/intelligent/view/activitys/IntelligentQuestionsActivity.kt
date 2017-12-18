package com.yc.english.intelligent.view.activitys

import android.content.Intent
import android.os.Parcelable
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
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
import com.yc.english.intelligent.utils.getLevel1QuestionInfo
import com.yc.english.intelligent.view.fragments.IntelligentQuestionsFragment
import com.yc.english.main.hepler.UserInfoHelper
import com.yc.english.main.model.domain.Constant
import com.yc.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_activity_questions.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionsActivity : BaseActivity<IntelligentQuestionPresenter>(), IntelligentQuestionContract.View {

    var isHandIn = false

    var unitId: Int = 0
    var reportId: Int = 0
    var type: String = ""
    var isResultIn = false

    companion object {
        var INSTANCE: IntelligentQuestionsActivity? = null
        fun getInstance(): IntelligentQuestionsActivity? {
            return INSTANCE
        }
    }

    override fun init() {
        mPresenter = IntelligentQuestionPresenter(this, this)
        StatusBarCompat.light(this)
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbarWarpper.mToolbar, mToolbarWarpper.mStatubar)
        RxView.clicks(mToolbarWarpper.mBackBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            eixt()
        }

        unitId = intent.getIntExtra("unitId", 0)
        reportId = intent.getIntExtra("reportId", 0)
        type = intent.getStringExtra("type")
        isResultIn = intent.getBooleanExtra("isResultIn", false)


        RxView.clicks(mSubmitBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            goToResult()
        }

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                mToolbarWarpper.index = i + 1
                stop()
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })


        INSTANCE = this

        loadData()

    }

    fun getResultKey(): String {
        var key = "result"
        if (unitId != 0) {
            key += "${UserInfoHelper.getUserInfo().uid}-unitId${unitId}${type}"
        } else {
            key += "${UserInfoHelper.getUserInfo().uid}-reportId${reportId}${type}"
        }
        return key
    }

    fun getFinishTimeKey(): String {
        var key = "finish-time"
        if (unitId != 0) {
            key += "${UserInfoHelper.getUserInfo().uid}-unitId${unitId}${type}"
        } else {
            key += "${UserInfoHelper.getUserInfo().uid}-reportId${reportId}${type}"
        }
        return key
    }

    fun getFinishKey(): String {
        var key = "finish"
        if (unitId != 0) {
            key += "${UserInfoHelper.getUserInfo().uid}-unitId${unitId}${type}"
        } else {
            key += "${UserInfoHelper.getUserInfo().uid}-reportId${reportId}${type}"
        }
        return key
    }

    fun loadData() {
        if (unitId != 0) {
            mPresenter.getQuestion(unitId.toString(), type)
        } else {
            mPresenter.getPlanDetail(reportId.toString(), type)
        }
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
        }
        mViewPager.setCurrentItem(actIndex)
        (mFragmentAdapter?.getItem(actIndex) as IntelligentQuestionsFragment).next(frgIndex)
    }

    private fun stop(){
        if(mFragmentAdapter == null) return
        for (j in 0..((mFragmentAdapter?.count ?: 0) - 1)) {
            (mFragmentAdapter?.getItem(j) as IntelligentQuestionsFragment).stop()
        }
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constant.RESULT_ANS)))
    fun reset(tag: String) {
        isHandIn = false
        isResultIn = true
        for (i in 0..((mFragmentAdapter?.count ?: 0) - 1)) {
            (mFragmentAdapter?.getItem(i) as IntelligentQuestionsFragment).next(0)
            (mFragmentAdapter?.getItem(i) as IntelligentQuestionsFragment).showDescView()
        }
        mViewPager.setCurrentItem(0)
    }


    var mFragmentAdapter: TabsUtils.IntelligentQuestionsFragmentAdapter? = null


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
            mToolbarWarpper.stopTime()
            (mSubmitBtn.parent as ViewGroup).visibility = View.VISIBLE
            mToolbarWarpper.mTimeTextView.text = SPUtils.getInstance().getString(getFinishTimeKey(), "")
        }
    }

    private fun goToResult() {
        isResultIn = true
        intent = Intent(this, IntelligentResultActivity::class.java)
        val infos = getLevel1QuestionInfo(questionInfos!!)
        intent.putParcelableArrayListExtra("questionInfos", infos as ArrayList<out Parcelable>)
        startActivity(intent)
    }

    fun getMaxIndex(idx: Int): Int {
        if (idx < 0) return 0
        var count = 0
        for (i in 0..(idx)) {
            val questionInfo = questionInfos?.get(i)!!
            count += questionInfo.count
        }
        return count
    }

    fun getTotalCount(): Int {
        var count = 0
        for (questionInfo in questionInfos!!) {
            count += questionInfo.count
        }
        return count
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
            loadData()
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
            finish()
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