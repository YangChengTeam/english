package com.yc.english.intelligent.view.activitys

import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView

/**
 * Created by zhangkai on 2017/11/28.
 */

class IntelligentEvaluationActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {
    override fun init() {
    }

    override fun getLayoutId() = R.layout.intelligent_activity_evaluation

}