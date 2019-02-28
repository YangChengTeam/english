package com.yc.junior.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.junior.english.base.model.BaseEngin
import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.junior.english.intelligent.model.domain.ReportInfo
import com.yc.junior.english.intelligent.model.domain.URLConfig
import com.yc.junior.english.main.hepler.UserInfoHelper
import rx.Observable

/**
 * Created by zhangkai on 2017/12/4.
 */

class IntelligentReportEngin(context: Context?) : BaseEngin(context) {
    fun getReportInfo(unit_id: String, use_time: String): Observable<ResultInfo<ReportInfo>> {
        var uid = ""
        if (UserInfoHelper.getUserInfo() != null) {
            uid = UserInfoHelper.getUserInfo().uid
        }
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.UNIT_REPORT, object :
                TypeReference<ResultInfo<ReportInfo>>() {}.type, mutableMapOf("unit_id" to unit_id,
                "user_id" to uid,
                "use_time" to use_time),
                true, true, true) as Observable<ResultInfo<ReportInfo>>
    }


    fun getPlanDetail(report_id: String, type: String): Observable<ResultInfo<QuestionInfoWrapper>> {
        var uid = ""
        if (UserInfoHelper.getUserInfo() != null) {
            uid = UserInfoHelper.getUserInfo().uid
        }
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.UNIT_PLAN_DETAIL, object :
                TypeReference<ResultInfo<QuestionInfoWrapper>>() {}.type, mutableMapOf("report_id" to report_id,
                "user_id" to uid,
                "type" to type),
                true, true, true) as Observable<ResultInfo<QuestionInfoWrapper>>
    }
}