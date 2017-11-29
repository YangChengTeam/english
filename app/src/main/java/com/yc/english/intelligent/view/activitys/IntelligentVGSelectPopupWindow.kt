package com.yc.english.intelligent.view.activitys

import android.app.Activity
import com.yc.english.R
import com.yc.english.R.id.mVersionRecyclerView
import com.yc.english.base.view.BasePopupWindow
import kotlinx.android.synthetic.main.intelligent_ppw_vg_select.*


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentVGSelectPopupWindow(context: Activity) : BasePopupWindow(context){
    override fun getAnimationID() = 0

    override fun getLayoutId() = R.layout.intelligent_ppw_vg_select

    override fun init() {

    }
}