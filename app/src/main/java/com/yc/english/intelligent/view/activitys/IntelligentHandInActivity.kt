package com.yc.english.intelligent.view.activitys

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import com.yc.english.intelligent.model.domain.QuestionInfo
import com.yc.english.intelligent.view.adpaters.IntelligentHandInAdapter
import kotlinx.android.synthetic.main.intelligent_avtivity_hand_in.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentHandInActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {

    lateinit var adpater: IntelligentHandInAdapter

    override fun init() {
        mToolbar.mIndexTextView.visibility = View.GONE
        StatusBarCompat.compat(this, mToolbar, mToolbar.mToolbar, mToolbar.mStatubar)

        adpater = IntelligentHandInAdapter()
        mRecyclerView.adapter = adpater
        mRecyclerView.layoutManager = GridLayoutManager(this, 5)


        RxView.clicks(mToolbar.mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            finish()
        }

        RxView.clicks(mSubmitBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            startActivity(Intent(this@IntelligentHandInActivity, IntelligentResultActivity::class.java))
        }


        mToolbar.mTimeTextView.text = "15: 00"

        adpater.setNewData(listOf(QuestionInfo("D"),
                QuestionInfo("D"),
                QuestionInfo("A"),
                QuestionInfo("D"),
                QuestionInfo("B"),
                QuestionInfo("C"),
                QuestionInfo("D"),
                QuestionInfo("B"),
                QuestionInfo("D"),
                QuestionInfo("A"),
                QuestionInfo("D"),
                QuestionInfo("C"),
                QuestionInfo("B"),
                QuestionInfo("C"),
                QuestionInfo("B")))

    }

    override fun getLayoutId() = R.layout.intelligent_avtivity_hand_in

}