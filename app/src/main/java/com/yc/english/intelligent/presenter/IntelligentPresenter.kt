package com.yc.english.intelligent.presenter

import android.content.Context
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.intelligent.contract.IntelligentTypeContract
import com.yc.english.intelligent.model.engin.IntelligentTypeEngin

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentPresenter : BasePresenter<IntelligentTypeEngin,
        IntelligentTypeContract.View> {
    constructor(context: Context?, v: IntelligentTypeContract.View?) : super(context, v) {
        mEngin = IntelligentTypeEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }



}