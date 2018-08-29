package com.yc.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.english.base.model.BaseEngin
import com.yc.english.intelligent.model.domain.URLConfig
import com.yc.english.intelligent.model.domain.UnitInfoWrapper
import com.yc.english.main.hepler.UserInfoHelper
import rx.Observable

/**
 * Created by zhangkai on 2017/12/4.
 */

class IntelligentPushQuestionEngin(context: Context?) : BaseEngin(context) {
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