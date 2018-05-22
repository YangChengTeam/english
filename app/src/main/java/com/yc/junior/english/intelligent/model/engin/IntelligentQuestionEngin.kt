package com.yc.junior.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.junior.english.base.model.BaseEngin
import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.junior.english.intelligent.model.domain.URLConfig
import com.yc.junior.english.main.hepler.UserInfoHelper
import rx.Observable

/**
 * Created by zhangkai on 2017/12/4.
 */
class IntelligentQuestionEngin(context: Context?) : BaseEngin(context) {

    fun getQuestions(unitId: String, type: String): Observable<ResultInfo<QuestionInfoWrapper>> {
        var uid = ""
        if (UserInfoHelper.getUserInfo() != null) {
            uid = UserInfoHelper.getUserInfo().uid
        }
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.GET_QUESTION, object :
                TypeReference<ResultInfo<QuestionInfoWrapper>>() {}.type,

                mutableMapOf(
                        "unit_id" to unitId,
                        "kpoint_type" to type,
                        "user_id" to uid),
                true, true, true) as Observable<ResultInfo<QuestionInfoWrapper>>
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