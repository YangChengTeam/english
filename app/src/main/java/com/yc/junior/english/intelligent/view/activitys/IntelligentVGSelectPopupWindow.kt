package com.yc.junior.english.intelligent.view.activitys

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SPUtils
import com.hwangjr.rxbus.RxBus
import com.jakewharton.rxbinding.view.RxView
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.junior.english.EnglishApp
import com.yc.junior.english.R
import com.yc.junior.english.base.view.BasePopupWindow
import com.yc.junior.english.intelligent.model.domain.VGInfoWarpper
import com.yc.junior.english.intelligent.model.engin.IntelligentTypeEngin
import com.yc.junior.english.intelligent.view.adpaters.IntelligentVGAdpater
import com.yc.junior.english.main.model.domain.Constant
import java.util.concurrent.TimeUnit


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentVGSelectPopupWindow(context: Activity) : BasePopupWindow(context) {
    override fun getAnimationID() = 0
    override fun getLayoutId() = R.layout.intelligent_ppw_vg_select

    @BindView(R.id.rv_version)
    lateinit var mVersionRecyclerView: RecyclerView
    @BindView(R.id.rv_grade)
    lateinit var mGradeRecyclerView: RecyclerView


    lateinit var mVersionAdapter: IntelligentVGAdpater
    lateinit var mGradeAdapter: IntelligentVGAdpater
    lateinit var typeEngin: IntelligentTypeEngin

    companion object {
        const val DEFAULT_VERSION_KEY = "DEFAULT_VERSION_KEY"
        const val DEFAULT_GRADE_KEY = "DEFAULT_GRADE_KEY"
    }

    override fun init() {
        typeEngin = IntelligentTypeEngin(mContext)

        mVersionAdapter = IntelligentVGAdpater()
        mVersionAdapter.defaultInfo = JSON.parseObject(SPUtils.getInstance().getString(DEFAULT_VERSION_KEY, ""),
                VGInfoWarpper.VGInfo::class.java)
        mVersionRecyclerView.adapter = mVersionAdapter
        mVersionRecyclerView.layoutManager = GridLayoutManager(mContext, 3)


        mGradeAdapter = IntelligentVGAdpater()
        mGradeAdapter.defaultInfo = JSON.parseObject(SPUtils.getInstance().getString(DEFAULT_GRADE_KEY, ""),
                VGInfoWarpper.VGInfo::class.java)
        mGradeRecyclerView.adapter = mGradeAdapter
        mGradeRecyclerView.layoutManager = GridLayoutManager(mContext, 3)

        mVersionAdapter.setOnItemClickListener { adapter, view, position ->
            val vgInfo = mVersionAdapter.data.get(position)
            if ((mVersionAdapter.defaultInfo?.id ?: 0) == vgInfo.id) {
                return@setOnItemClickListener
            }
            SPUtils.getInstance().put(DEFAULT_VERSION_KEY, JSON.toJSONString(vgInfo))
            if (vgInfo.name!!.contains("PEP")) {
                SPUtils.getInstance().put("period", "0")
            } else {
                SPUtils.getInstance().put("period", "1")
            }
            getGrade(vgInfo)
            mVersionAdapter.defaultInfo = vgInfo
            adapter.notifyDataSetChanged()
        }


        mGradeAdapter.setOnItemClickListener { adapter, view, position ->
            val vgInfo = mGradeAdapter.data.get(position)
            if ((mGradeAdapter.defaultInfo?.id ?: 0) == vgInfo.id) {
                return@setOnItemClickListener
            }
            mGradeAdapter.defaultInfo = vgInfo
            SPUtils.getInstance().put(DEFAULT_GRADE_KEY, JSON.toJSONString(vgInfo))
            adapter.notifyDataSetChanged()
            RxBus.get().post(Constant.GET_UNIT, "from select")
            dismiss()
        }

        RxView.clicks(contentView).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            dismiss()
        }


        getVersionInfo()


    }


    fun getVersionInfo() {
        typeEngin.getVersion().subscribe {
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (mVersionAdapter.defaultInfo == null) {
                    getGrade(it.data?.list?.get(0)!!)
                    SPUtils.getInstance().put(IntelligentVGSelectPopupWindow.DEFAULT_VERSION_KEY, JSON.toJSONString(it.data?.list?.get(0)!!))
                } else {
                    getGrade(mVersionAdapter.defaultInfo!!)
                }
                mVersionAdapter.setNewData(it.data?.list)
            }
        }
    }

    fun getGrade(vgInfo: VGInfoWarpper.VGInfo) {


        EnglishApp.get().setHttpDefaultParams()
        typeEngin.getGrade(vgInfo).subscribe {
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK && it.data?.list != null && it.data?.list!!.size > 0) {
                mGradeAdapter.setNewData(it.data?.list)
            }
        }
    }
}