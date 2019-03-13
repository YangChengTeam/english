package com.yc.english.intelligent.presenter

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.utils.LogUtil
import com.yc.english.base.utils.SimpleCacheUtils
import com.yc.english.intelligent.contract.IntelligentTypeContract
import com.yc.english.intelligent.model.domain.UnitInfoWrapper
import com.yc.english.intelligent.model.domain.VGInfoWarpper
import com.yc.english.intelligent.model.engin.IntelligentTypeEngin
import com.yc.english.main.model.domain.Constant
import yc.com.base.BasePresenter
import yc.com.blankj.utilcode.util.SPUtils
import java.util.*

/**
 * Created by zhangkai on 2017/11/24.
 */
open class IntelligentTypePresenter : BasePresenter<IntelligentTypeEngin,
        IntelligentTypeContract.View> {
    constructor(context: Context?, v: IntelligentTypeContract.View?) : super(context, v) {
        mEngine = IntelligentTypeEngin(context)
        RxBus.get().register(this)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (!forceUpdate) return
    }

    @Subscribe(thread = EventThread.NEW_THREAD, tags = arrayOf(Tag(Constant.GET_UNIT)))
    fun getUnit(tag: String) {

        var versionInfo = JSON.parseObject(SPUtils.getInstance().getString(Constant.DEFAULT_VERSION_KEY, ""), VGInfoWarpper.VGInfo::class.java)

        var gradeInfo = JSON.parseObject(SPUtils.getInstance().getString(Constant.DEFAULT_GRADE_KEY, ""), VGInfoWarpper.VGInfo::class.java)
        var title = ""
        var versionId = 0
        if (versionInfo != null) {
            LogUtil.msg("getUnit:  " + versionInfo.title + "--" + versionInfo.alias + "--" + versionInfo.name)

            title = versionInfo.alias ?: versionInfo.title ?: ""
            if (TextUtils.isEmpty(title)) {
                title = if (versionInfo.name!!.contains("PEP")) {
                    "小学"
                } else {
                    "初中"
                }
            }
            versionId = versionInfo.versionId ?: 22
        } else {
            versionInfo = VGInfoWarpper.VGInfo()
            val period = SPUtils.getInstance().getString("period", "0")
            if (period == "0") {
                title = "小学"
                versionId = 9
                versionInfo.id = 69
                versionInfo.name = "人教版PEP"
            } else {
                title = "初中"
                versionId = 22
                versionInfo.id = 200
                versionInfo.name = "人教版"
            }
            versionInfo.title = title
            versionInfo.versionId = versionId
        }

        SPUtils.getInstance().put(Constant.DEFAULT_VERSION_KEY, JSON.toJSONString(versionInfo))

        if (gradeInfo == null) {
            gradeInfo = VGInfoWarpper.VGInfo()
            var grade = SPUtils.getInstance().getInt("grade", 0)
            gradeInfo.id = -1
            gradeInfo.grade = grade
            val name = title     //替换字符的奇技淫巧 1
            when (grade) {
                1, 2, 3, 4 -> title += "四年级"
                5 -> title += "五年级"
                6 -> title += "六年级"
                7 -> title += "七年级"
                8 -> title += "八年级"
                9 -> title += "九年级"
            }
            if (title.contains("九年级")) {
                gradeInfo.partType = 2
                title += "全"
            } else {
                when (Calendar.getInstance().get(Calendar.MONTH)) {
                    in 3..6 -> {
                        gradeInfo.partType = 0
                        title += "上"
                    }
                    in 1..2, in 7..12 -> {
                        gradeInfo.partType = 1
                        title += "下"
                    }
                }
            }

            gradeInfo.title = title.replace(name, "") //替换字符的奇技淫巧 2
        } else {
            title += gradeInfo.title ?: ""
        }
        mView.showTitle(title)
        SPUtils.getInstance().put(Constant.DEFAULT_GRADE_KEY, JSON.toJSONString(gradeInfo))
        gradeInfo.versionId = versionId

        val subriction = mEngine.getUnit(gradeInfo).subscribe({
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK) {
                if (it?.data?.list != null) {
                    SimpleCacheUtils.writeCache(mContext, "getUnit", JSON.toJSONString(it.data?.list
                            ?: ""))
                    showInfo(it?.data?.list!!)
                }
            }
        }, {})
        mSubscriptions.add(subriction)
    }

    private fun showInfo(list: List<UnitInfoWrapper.UnitInfo>) {
        val titles = arrayOfNulls<String>(list.size)
        val types = arrayOfNulls<UnitInfoWrapper.UnitInfo>(list.size)
        var i = 0
        for (unitInfo in list) {
            titles[i] = unitInfo.simpleName
            types[i] = unitInfo
            i++
        }
        mView.showInfo(titles, types)
    }

    @Subscribe(thread = EventThread.NEW_THREAD, tags = arrayOf(Tag(Constant.GET_VERSION)))
    fun getVersionInfo(tag: String) {
        mEngine.getVersion().subscribe({
            val code = it?.code ?: -1
            if (code == HttpConfig.STATUS_OK && it.data?.list != null) {
                SPUtils.getInstance().put(Constant.DEFAULT_VERSION_KEY, JSON.toJSONString(it.data?.list?.get(0)!!))
                getUnit("after getVersionInfo")
            }
        }, {})
    }


}