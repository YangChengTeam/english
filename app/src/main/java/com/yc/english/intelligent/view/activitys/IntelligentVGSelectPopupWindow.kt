package com.yc.english.intelligent.view.activitys

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.yc.english.R
import com.yc.english.base.view.BasePopupWindow
import com.yc.english.intelligent.model.domain.VGInfo
import com.yc.english.intelligent.view.adpaters.IntelligentVGAdpater


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentVGSelectPopupWindow(context: Activity) : BasePopupWindow(context){
    override fun getAnimationID() = 0

    override fun getLayoutId() = R.layout.intelligent_ppw_vg_select

    @BindView(R.id.rv_version)
    lateinit var mVersionRecyclerView: RecyclerView

    @BindView(R.id.rv_grade)
    lateinit var mGradeRecyclerView: RecyclerView


    lateinit var mVersionAdapter: IntelligentVGAdpater

    lateinit var mGradeAdapter: IntelligentVGAdpater

    override fun init() {

        mVersionAdapter =  IntelligentVGAdpater()
        mVersionRecyclerView.adapter = mVersionAdapter
        mVersionRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        mVersionAdapter.setNewData(listOf(VGInfo("人教版")))

        mGradeAdapter = IntelligentVGAdpater()
        mGradeRecyclerView.adapter = mGradeAdapter
        mGradeRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        mGradeAdapter.setNewData(listOf(VGInfo("七年级上"), VGInfo("七年级下"), VGInfo("八年级上"), VGInfo("八年级下"), VGInfo
        ("九年级全")))
    }
}