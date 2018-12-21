package com.yc.english.intelligent.view.activitys

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.kk.securityhttp.net.contains.HttpConfig
import com.umeng.analytics.MobclickAgent
import com.yc.english.R
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.SharePopupWindow
import com.yc.english.intelligent.contract.IntelligentReportContract
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.domain.ReportInfo
import com.yc.english.intelligent.presenter.IntelligentReportPresenter
import com.yc.english.intelligent.utils.fromHtml
import com.yc.english.intelligent.view.adpaters.IntelligentReportErrorAdapter
import com.yc.english.intelligent.view.adpaters.IntelligentReportWeakAdapter
import com.yc.english.main.model.domain.Constant
import kotlinx.android.synthetic.main.intelligent_activity_report.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentReportActivity : BaseActivity<IntelligentReportPresenter>(), IntelligentReportContract.View {


    var unitId: Int = 0
    var reportId = 0
    override fun init() {
        MobclickAgent.onEvent(this, "intelligent_report", "智能评测-报告")
        mPresenter = IntelligentReportPresenter(this, this)
        StatusBarCompat.light(this)
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar, mStatusBar)


        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            finish()
        }

        mStartPushBtn.text = "进入个性化学习"
//        if (UserInfoHelper.getUserInfo() != null && UserInfoHelper.getUserInfo().isVip == 2) {
//            mStartPushBtn.text = "进入个性化学习"
//        }

        RxView.clicks(mStartPushBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {

            val intent = Intent(this@IntelligentReportActivity, IntelligentsPushQuestionActivity::class.java)
            intent.putExtra("reportId", reportId)
            startActivity(intent)
//            if (UserInfoHelper.getUserInfo() != null && UserInfoHelper.getUserInfo().isVip == 2) {
//                mStartPushBtn.text = "进入个性化学习"
//                val intent = Intent(this@IntelligentReportActivity, IntelligentsPushQuestionActivity::class.java)
//                intent.putExtra("reportId", reportId)
//                startActivity(intent)
//            } else {
//                val bundle = Bundle()
//                bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_SINGLE_INDIVIDUALITY_PLAN)
//                VipDialogHelper.showVipDialog(supportFragmentManager, "", bundle)
//            }
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

    override fun hide() {
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
        mReportTextView.text = fromHtml(reportInfo.desp)
        star((reportInfo.score + 4) / 20)

        ringView.setText("" + reportInfo.score)
        tv_correct_answer.text = String.format(getString(R.string.intell_correct_answer), reportInfo.getRightSum())
        tv_error_answer.text = String.format(getString(R.string.intell_error_answer), reportInfo.getErrorSum())

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexWrap = FlexWrap.WRAP
        layoutManager.alignItems = AlignItems.STRETCH
        weakness_recyclerView.layoutManager = FlexboxLayoutManager(this)

        val list = (1..10).map { "" + it }

        val adapter = IntelligentReportWeakAdapter(reportInfo.error_grammar)
//        val adapter = IntelligentReportWeakAdapter(list)
        weakness_recyclerView.adapter = adapter
        weakness_recyclerView.addItemDecoration(MyItemDecoration())
        tv_sum_weakness.text = String.format(getString(R.string.weakness_knowledge), reportInfo.error_grammar?.size)

        errorsRecyclerView.layoutManager = LinearLayoutManager(this)
        errorsRecyclerView.adapter = IntelligentReportErrorAdapter(reportInfo.err_tips)

        if (reportInfo.score == 100) {
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

    override fun showPlanDetail(data: List<QuestionInfoWrapper.QuestionInfo>) {

    }

    private inner class MyItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(0, 0, UIUtil.dip2px(this@IntelligentReportActivity, 10.0), UIUtil.dip2px(this@IntelligentReportActivity, 10.0))
        }
    }
}