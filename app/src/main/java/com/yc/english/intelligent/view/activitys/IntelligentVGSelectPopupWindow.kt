package com.yc.english.intelligent.view.activitys

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.hwangjr.rxbus.RxBus
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.R
import com.yc.english.base.helper.RxUtils
import com.yc.english.base.utils.SimpleCacheUtils
import com.yc.english.base.view.BasePopupWindow
import com.yc.english.intelligent.model.domain.VGInfoWarpper
import com.yc.english.intelligent.model.engin.IntelligentTypeEngin
import com.yc.english.intelligent.view.adpaters.IntelligentVGAdpater
import com.yc.english.main.model.domain.Constant
import java.util.*


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
            if (!view.isSelected) {
                val vgInfo = mVersionAdapter.data.get(position)
                mVersionAdapter.defaultInfo = vgInfo
                SPUtils.getInstance().put(DEFAULT_VERSION_KEY, JSON.toJSONString(vgInfo))
                getGrade(vgInfo)
            }
        }


        mGradeAdapter.setOnItemClickListener { adapter, view, position ->
            val vgInfo = mGradeAdapter.data.get(position)
            mGradeAdapter.defaultInfo = vgInfo
            SPUtils.getInstance().put(DEFAULT_GRADE_KEY, JSON.toJSONString(vgInfo))
            adapter.notifyDataSetChanged()
            RxBus.get().post(Constant.GET_UNIT, "from select")
            dismiss()
        }

        getVersionInfo()
    }


    fun getVersionInfo() {
        SimpleCacheUtils.readCache(mContext, "getVersionInfo", object : SimpleCacheUtils.CacheRunnable() {
            override fun run() {
                val list = Gson().fromJson<List<VGInfoWarpper.VGInfo>>(json, object : TypeReference<List<VGInfoWarpper
                .VGInfo>>() {}.type)
                if (list != null && list.size > 0) {
                    mContext.runOnUiThread {
                        mVersionAdapter.setNewData(list)
                    }
                }
            }
        })

        typeEngin.getVersion().subscribe {
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {

                if (mVersionAdapter.defaultInfo == null) {
                    getGrade(it.data?.list?.get(0)!!)
                    SPUtils.getInstance().put(IntelligentVGSelectPopupWindow.DEFAULT_VERSION_KEY, JSON.toJSONString(it.data?.list?.get(0)!!))
                } else {
                    getGrade(mVersionAdapter.defaultInfo!!)
                }
                SimpleCacheUtils.writeCache(mContext, "getVersionInfo", JSON.toJSONString(it.data?.list ?: ""))
                mVersionAdapter.setNewData(it.data?.list)
            }
        }
    }

    fun getGrade(vgInfo: VGInfoWarpper.VGInfo) {
        SimpleCacheUtils.readCache(mContext, "getGrade", object : SimpleCacheUtils.CacheRunnable() {
            override fun run() {
                val list = Gson().fromJson<List<VGInfoWarpper.VGInfo>>(json, object : TypeReference<List<VGInfoWarpper
                .VGInfo>>() {}.type)
                if (list != null && list.size > 0) {
                    mContext.runOnUiThread {
                        mGradeAdapter.setNewData(list)
                    }
                }
            }
        })
        typeEngin.getGrade(vgInfo).subscribe {
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK && it.data?.list != null && it.data?.list!!.size  > 0) {
                mGradeAdapter.setNewData(it.data?.list)
                SimpleCacheUtils.writeCache(mContext, "getGrade", JSON.toJSONString(it.data?.list ?: ""))
            }
        }
    }
}