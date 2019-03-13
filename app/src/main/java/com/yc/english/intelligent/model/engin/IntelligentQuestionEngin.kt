package com.yc.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.domain.URLConfig
import com.yc.english.main.hepler.UserInfoHelper
import rx.Observable
import yc.com.base.BaseEngine

/**
 * Created by zhangkai on 2017/12/4.
 */
class IntelligentQuestionEngin(context: Context?) : BaseEngine(context) {

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

    fun removeAnswer(unitId: String, kpoint_type: String, test_type: String): Observable<ResultInfo<String>> {
        var uid = if (UserInfoHelper.getUserInfo() != null) UserInfoHelper.getUserInfo().uid else ""
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.REMOVE_ANSWER, object : TypeReference<ResultInfo<String>>() {}.type, mutableMapOf("unit_id" to unitId,
                "kpoint_type" to kpoint_type,
                "test_type" to test_type,
                "user_id" to uid), true, true, true) as Observable<ResultInfo<String>>
    }
}