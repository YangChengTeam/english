package com.yc.english.intelligent.view.fragments

import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.view.BaseFragment
import com.yc.english.base.view.IView

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionsFragment : BaseFragment<BasePresenter<BaseEngin, IView>>() {

    lateinit var type: String

    override fun init() {
    }

    override fun getLayoutId() = R.layout.intelligent_fragment_questions
}