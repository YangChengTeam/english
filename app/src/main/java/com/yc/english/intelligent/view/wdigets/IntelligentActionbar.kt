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

    var mStatubar = findViewById<View>(R.id.status_bar)
    var mToolbar = findViewById<RelativeLayout>(R.id.toolbar)
    var mBackBtn = findViewById<LinearLayout>(R.id.ll_back)
    var mTimeTextView = findViewById<TextView>(R.id.tvTime)
    var mIndexTextView = findViewById<TextView>(R.id.tvIndex)
    private var mTimes = 0

    override fun getLayoutId() = R.layout.intelligent_actionbar

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        index = 2
    }

    var total: Int = 15
    var index: Int = 1
        set(value) {
            mIndexTextView.text = fromHtml("<font color=#FB4C30>${value}</font>/${total}")
        }

    lateinit var subscribetion: Subscription
    fun startTime() {
        subscribetion = Observable.interval(1, TimeUnit.SECONDS).timeInterval().observeOn(AndroidSchedulers
                .mainThread())
                .subscribe {
                    mTimes++
                    val minutes = mTimes / 60
                    if (minutes > 60) {
                        if (!subscribetion.isUnsubscribed) {
                            subscribetion.unsubscribe()
                        }
                    } else {
                        var seconds = mTimes - minutes * 60
                        mTimeTextView.text = "${timeShortFormat(minutes)}:${timeShortFormat(seconds)}"
                    }
                }
    }

}