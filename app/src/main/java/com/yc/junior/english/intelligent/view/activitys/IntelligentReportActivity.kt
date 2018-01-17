package com.yc.junior.english.intelligent.view.activitys

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.junior.english.R
import com.yc.junior.english.base.utils.StatusBarCompat
import com.yc.junior.english.base.view.BaseActivity
import com.yc.junior.english.base.view.SharePopupWindow
import com.yc.junior.english.intelligent.contract.IntelligentReportContract
import com.yc.junior.english.intelligent.model.domain.ReportInfo
import com.yc.junior.english.intelligent.presenter.IntelligentReportPresenter
import com.yc.junior.english.intelligent.utils.fromHtml
import com.yc.junior.english.main.hepler.UserInfoHelper
import com.yc.junior.english.main.model.domain.Constant
import com.yc.junior.english.vip.model.bean.GoodsType
import com.yc.junior.english.vip.utils.VipDialogHelper
import kotlinx.android.synthetic.main.intelligent_activity_report.*
import java.util.concurrent.TimeUnit

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
            val sharePopupWindow = SharePopupWindow(this@IntelligentReportActivity)
            sharePopupWindow.show()
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
                .hearing / 100f, reportInfo.grammar / 100f, reportInfo.read / 100f,
                reportInfo.writing / 100f)).setTitles(arrayOf("词汇", "口语", "听力", "语法", "阅读", "写作"))
        mIntelligentItemScoreView1.progress(reportInfo.vocabulary)
        mIntelligentItemScoreView2.progress(reportInfo.oracy)
        mIntelligentItemScoreView3.progress(reportInfo.grammar)
        mIntelligentItemScoreView4.progress(reportInfo.hearing)
        mIntelligentItemScoreView5.progress(reportInfo.read)
        mIntelligentItemScoreView6.progress(reportInfo.writing)
        mReportTextView.text = fromHtml( reportInfo.desp )
        star((reportInfo.score + 4) / 20)

        if(reportInfo.score == 100){
            mStartPushBtn.visibility = View.GONE
        } else {
            mStartPushBtn.visibility = View.VISIBLE
        }
        RxBus.get().post(Constant.USER_INFO, "from question report")
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)))
    fun openVip(tag: String) {
        mStartPushBtn.text = "进入个性化学习"
    }
}