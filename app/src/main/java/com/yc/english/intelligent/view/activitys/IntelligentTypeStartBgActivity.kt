package com.yc.english.intelligent.view.activitys

import com.jakewharton.rxbinding.view.RxView
import com.umeng.analytics.MobclickAgent
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import com.yc.english.main.view.activitys.MainActivity
import kotlinx.android.synthetic.main.intelligent_activity_type_start_bg.*
import yc.com.blankj.utilcode.util.SPUtils
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/30.
 */

class IntelligentTypeStartBgActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {

    override fun init() {
        RxView.clicks(mStartType).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            SPUtils.getInstance().put(MainActivity.BGKEY, MainActivity.BGKEY)
            MainActivity.getMainActivity().goToIntelligent()
            MobclickAgent.onEvent(this@IntelligentTypeStartBgActivity, "intelligent", "智能评测-开始")
            finish()
        }
    }

    override fun getLayoutId() = R.layout.intelligent_activity_type_start_bg
}