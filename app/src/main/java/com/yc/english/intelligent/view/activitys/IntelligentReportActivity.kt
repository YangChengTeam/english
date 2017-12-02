package com.yc.english.intelligent.view.activitys

import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
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

        RxView.clicks(mStartPushBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            startActivity(Intent(this@IntelligentReportActivity, IntelligentsPushQuestionActivity::class.java))
        }



        mIntelligentItemScoreView1.progress(80)
        mIntelligentItemScoreView2.progress(90)
        mIntelligentItemScoreView3.progress(20)
        mIntelligentItemScoreView4.progress(60)
        mIntelligentItemScoreView5.progress(70)
        mIntelligentItemScoreView6.progress(20)
        star(4)
    }

    override fun getLayoutId() = R.layout.intelligent_activity_report

    var star: Int = 0
        set(value) {
            for (i in 0..(mStarLinearLayout.childCount - 1)) {
                val starImageView = mStarLinearLayout.getChildAt(i) as ImageView
                if (i < value) {
                    starImageView.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.intelligents_report_star_hover))
                }
            }
        }

    fun star(n: Int) {
        val anim = ObjectAnimator.ofInt(this, "star", 0, n)
        anim.interpolator = AccelerateInterpolator()
        anim.duration = 1500
        anim.start()
    }
}