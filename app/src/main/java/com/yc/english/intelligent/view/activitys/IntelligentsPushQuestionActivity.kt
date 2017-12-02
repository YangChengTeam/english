package com.yc.english.intelligent.view.activitys

import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import com.yc.english.main.view.activitys.MainActivity
import kotlinx.android.synthetic.main.intelligent_activity_push_question.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/30.
 */

class IntelligentsPushQuestionActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {
    override fun init() {
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar, R.mipmap.base_actionbar)

        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            finish()
        }
    }

    override fun getLayoutId() = R.layout.intelligent_activity_push_question

}