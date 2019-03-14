package com.yc.junior.english.intelligent.presenter

import android.content.Context
import com.yc.junior.english.intelligent.contract.IntelligentTypeContract
import com.yc.junior.english.intelligent.model.engin.IntelligentTypeEngin
import yc.com.base.BasePresenter


/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentPresenter : BasePresenter<IntelligentTypeEngin,
        IntelligentTypeContract.View> {
    constructor(context: Context?, v: IntelligentTypeContract.View?) : super(context, v) {
        mEngine = IntelligentTypeEngin(context)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }



}