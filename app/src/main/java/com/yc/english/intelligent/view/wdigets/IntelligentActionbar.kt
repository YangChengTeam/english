package com.yc.english.intelligent.view.wdigets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView
import com.yc.english.intelligent.utils.fromHtml
import com.yc.english.intelligent.utils.timeShortFormat
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentActionbar : BaseView {

    var mStatubar = findViewById(R.id.status_bar) as View
    var mToolbar = findViewById(R.id.toolbar) as RelativeLayout
    var mBackBtn = findViewById(R.id.ll_back) as LinearLayout
    var mTitleTextView = findViewById(R.id.tv_title) as TextView
    var mTimeTextView = findViewById(R.id.tvTime) as TextView
    var mIndexTextView = findViewById(R.id.tvIndex) as TextView
    private var mTimes = 0

    override fun getLayoutId() = R.layout.intelligent_actionbar

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }

    var total: Int = 0
    var index: Int = 0
        set(value) {
            mIndexTextView.text = fromHtml("<font color=#FB4C30>${value}</font>/${total}")
            field = value
        }

    var title: String = ""
        set(value) {
            mTitleTextView.text = value
            field = title
        }

    var subscribetion: Subscription? = null
    fun startTime() {
        subscribetion = Observable.interval(1, TimeUnit.SECONDS).timeInterval().observeOn(AndroidSchedulers
                .mainThread())
                .subscribe {
                    mTimes++
                    val minutes = mTimes / 60
                    if (minutes > 60) {
                        if (!(subscribetion?.isUnsubscribed ?: false)) {
                            subscribetion?.unsubscribe()
                        }
                    } else {
                        var seconds = mTimes - minutes * 60
                        mTimeTextView.text = "${timeShortFormat(minutes)}:${timeShortFormat(seconds)}"
                    }
                }
    }

    fun stopTime() {
        if (subscribetion != null && subscribetion?.isUnsubscribed ?: false) {
            subscribetion?.unsubscribe()
        }
    }

}