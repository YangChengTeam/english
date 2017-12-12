package com.yc.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.english.base.model.BaseEngin
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.model.domain.URLConfig
import rx.Observable

/**
 * Created by zhangkai on 2017/12/4.
 */
class IntelligentQuestionEngin(context: Context?) : BaseEngin(context) {
    fun getQuestions(unitId: String, type: String): Observable<ResultInfo<QuestionInfoWrapper>> {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.GET_QUESTION, object :
                TypeReference<ResultInfo<QuestionInfoWrapper>>() {}.type,
                mutableMapOf(
                        "unit_id" to unitId,
                        "kpoint_type" to type),
                true, true, true) as Observable<ResultInfo<QuestionInfoWrapper>>
    }
}