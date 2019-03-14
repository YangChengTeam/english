package com.yc.junior.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.junior.english.intelligent.model.domain.URLConfig
import com.yc.junior.english.intelligent.model.domain.UnitInfoWrapper
import com.yc.junior.english.main.hepler.UserInfoHelper
import rx.Observable
import yc.com.base.BaseEngine

/**
 * Created by zhangkai on 2017/12/4.
 */

class IntelligentPushQuestionEngin(context: Context?) : BaseEngine(context) {
    fun getPlan(report_id: String): Observable<ResultInfo<UnitInfoWrapper.ComleteInfo>> {
        var uid = ""
        if (UserInfoHelper.getUserInfo() != null) {
            uid = UserInfoHelper.getUserInfo().uid
        }
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.UNIT_PLAN, object :
                TypeReference<ResultInfo<UnitInfoWrapper.ComleteInfo>>() {}.type, mutableMapOf("report_id" to report_id,
                "user_id" to uid),
                true, true, true) as Observable<ResultInfo<UnitInfoWrapper.ComleteInfo>>
    }




}