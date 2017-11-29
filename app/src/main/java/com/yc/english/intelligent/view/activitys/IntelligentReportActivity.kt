package com.yc.english.intelligent.view.activitys

import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import kotlinx.android.synthetic.main.intelligent_activity_report.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentReportActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {
    override fun init() {
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar, mStatusBar)
        mAbilityView.setDatas(floatArrayOf(0.62f, 0.92f, 0.29f, 0.45f, 0.82f, 0.74f))
                .setTitles(arrayOf("阅读", "听力", "口语", "写作", "语法", "词汇"))

        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            finish()
        }


        mIntelligentItemScoreView1.progress = 80
        mIntelligentItemScoreView2.progress = 90
        mIntelligentItemScoreView3.progress = 70
        mIntelligentItemScoreView4.progress = 40
        mIntelligentItemScoreView5.progress = 30
        mIntelligentItemScoreView6.progress = 20
    }

    override fun getLayoutId() = R.layout.intelligent_activity_report
}