package com.yc.english.intelligent.view.activitys

import android.support.v7.widget.GridLayoutManager
import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import com.yc.english.intelligent.model.domain.QuestionInfo
import com.yc.english.intelligent.utils.fromHtml
import com.yc.english.intelligent.view.adpaters.IntelligentResultAdapter
import kotlinx.android.synthetic.main.intelligent_activity_result.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */

class IntelligentResultActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {

    lateinit var adpater: IntelligentResultAdapter
    override fun init() {
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar, mStatusBar)

        adpater = IntelligentResultAdapter()
        mRecyclerView.adapter = adpater
        mRecyclerView.layoutManager = GridLayoutManager(this, 5)


        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            finish()
        }

        val datas=listOf(QuestionInfo("D"),
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
                QuestionInfo("B"))

        for(questionInfo in datas){
            questionInfo.right = if(questionInfo.answer.equals("D")) true else false
        }
        adpater.setNewData(datas)


        mRightTextView.text = fromHtml("对<font color='#6ec82d'> 11 </font>题")
        mErrorTextView.text = fromHtml("错<font color='#ee5757'> 5 </font>题")

    }

    override fun getLayoutId() = R.layout.intelligent_activity_result

}