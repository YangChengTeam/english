package com.yc.english.intelligent.view.fragments

import com.yc.english.R
import com.yc.english.base.view.BaseFragment
import com.yc.english.intelligent.presenter.IntelligentPresenter

/**
 * Created by zhangkai on 2017/11/24.
 */

open class IntelligentFragment : BaseFragment<IntelligentPresenter>() {
    override fun init() {
    }

    override fun getLayoutId() = R.layout.intelligent_fragment_index
}
