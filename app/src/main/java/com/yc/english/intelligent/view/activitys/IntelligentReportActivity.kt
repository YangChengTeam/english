package com.yc.english.intelligent.view.activitys

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.EnglishApp
import com.yc.english.R
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.intelligent.contract.IntelligentReportContract
import com.yc.english.intelligent.model.domain.ReportInfo
import com.yc.english.intelligent.presenter.IntelligentReportPresenter
import com.yc.english.main.hepler.UserInfoHelper
import com.yc.english.main.model.domain.Constant
import kotlinx.android.synthetic.main.intelligent_activity_report.*
import java.util.concurrent.TimeUnit
import com.yc.english.vip.utils.VipDialogHelper
import com.yc.english.vip.model.bean.GoodsType
import kotlinx.android.synthetic.main.intelligent_fragment_index.*


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentReportActivity : BaseActivity<IntelligentReportPresenter>(), IntelligentReportContract.View {

    var unitId: Int = 0
    var reportId = 0
    override fun init() {
        mPresenter = IntelligentReportPresenter(this, this)
        StatusBarCompat.light(this)
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar, mStatusBar)

        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            finish()
        }

        if (UserInfoHelper.getUserInfo() != null && UserInfoHelper.getUserInfo().isVip == 2) {
            mStartPushBtn.text = "进入个性化学习"
        }

        RxView.clicks(mStartPushBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            if (UserInfoHelper.getUserInfo() != null && UserInfoHelper.getUserInfo().isVip == 2) {
                mStartPushBtn.text = "进入个性化学习"
                val intent = Intent(this@IntelligentReportActivity, IntelligentsPushQuestionActivity::class.java)
                intent.putExtra("reportId", reportId)
                startActivity(intent)
            } else {
                val bundle = Bundle()
                bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_SINGLE_INDIVIDUALITY_PLAN)
                VipDialogHelper.showVipDialog(supportFragmentManager, "", bundle)
            }
        }

        RxView.clicks(mShareBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {

        }

        unitId = intent.getIntExtra("unitId", 0)
        mPresenter.getReportInfo(unitId.toString())
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

    override fun hideStateView() {
        mStateView.hide()
    }

    override fun showNoNet() {
        mStateView.showNoNet(mContentView, HttpConfig.NET_ERROR, {
            mPresenter.getReportInfo(unitId.toString())
        })
    }

    override fun showNoData() {
        mStateView.showNoData(mContentView)
    }

    override fun showLoading() {
        mStateView.showLoading(mContentView)
    }

    override fun showInfo(reportInfo: ReportInfo) {
        reportId = reportInfo.id

        mAbilityView.setDatas(floatArrayOf(reportInfo.vocabulary / 100f, reportInfo.oracy / 100f, reportInfo
                .hearing / 100f,reportInfo.grammar / 100f, reportInfo.read / 100f,
                reportInfo.writing / 100f)).setTitles(arrayOf("词汇", "口语", "听力", "语法", "阅读", "写作"))
        mIntelligentItemScoreView1.progress(reportInfo.vocabulary)
        mIntelligentItemScoreView2.progress(reportInfo.oracy)
        mIntelligentItemScoreView3.progress(reportInfo.grammar)
        mIntelligentItemScoreView4.progress(reportInfo.hearing)
        mIntelligentItemScoreView5.progress(reportInfo.read)
        mIntelligentItemScoreView6.progress(reportInfo.writing)
        mReportTextView.text = reportInfo.desp
        star((reportInfo.score + 4) / 20)

        mStartPushBtn.visibility = View.VISIBLE
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)))
    fun openVip(tag: String) {
        mStartPushBtn.text = "进入个性化学习"
    }

}