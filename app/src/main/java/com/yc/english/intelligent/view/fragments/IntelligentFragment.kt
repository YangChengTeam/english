package com.yc.english.intelligent.view.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.Gravity
import com.blankj.subutil.util.ThreadPoolUtils
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.UIUitls
import com.yc.english.R
import com.yc.english.base.utils.Blur
import com.yc.english.base.view.BaseFragment
import com.yc.english.intelligent.presenter.IntelligentPresenter
import com.yc.english.intelligent.view.activitys.IntelligentQuestionsActivity
import com.yc.english.intelligent.view.activitys.IntelligentReportActivity
import com.yc.english.intelligent.view.activitys.IntelligentVGSelectPopupWindow
import com.yc.english.intelligent.view.wdigets.IntelligentTypeItemView
import kotlinx.android.synthetic.main.intelligent_fragment_index.*
import java.util.concurrent.TimeUnit


/**
 * Created by zhangkai on 2017/11/27.
 */

class IntelligentFragment : BaseFragment<IntelligentPresenter>() {
    init {
        isUseInKotlin = true
    }

    lateinit var type: String

    override fun init() {
        ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute {
            val bimap = Blur.fastblur(activity, BitmapFactory.decodeResource(context!!.resources, R.mipmap
                    .intellgent_main_bg)
                    , 25)
            UIUitls.post {
                mInfoBg.setImageBitmap(bimap)
            }
        }

        mIntelligentType1.complete = true

        initIntelligentTypeEvents(mIntelligentType1, mIntelligentType3, mIntelligentType4,
                mIntelligentType5, mIntelligentType6)

        RxView.clicks(mReportBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            startActivity(Intent(activity, IntelligentReportActivity::class.java))
        }
    }

    private fun setIntelligentTypeEvent(mIntelligentType: IntelligentTypeItemView) {
        if (!mIntelligentType.complete) {
            mIntelligentType.mDoTextView.isClickable = true
            RxView.clicks(mIntelligentType.mDoTextView).throttleFirst(200, TimeUnit
                    .MILLISECONDS).subscribe {
                if (!mIntelligentType.complete)
                    startActivity(Intent(activity, IntelligentQuestionsActivity::class.java))
            }
        } else {
            mIntelligentType.mDoTextView.isClickable = false
        }
    }

    private fun initIntelligentTypeEvents(vararg intelligentTypes: IntelligentTypeItemView) {
        for (intelligentType in intelligentTypes) {
            setIntelligentTypeEvent(intelligentType)
        }
    }


    override fun getLayoutId() = R.layout.intelligent_fragment_index

}