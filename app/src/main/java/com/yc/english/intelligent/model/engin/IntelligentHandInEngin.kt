package com.yc.english.intelligent.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.yc.english.base.model.BaseEngin
import com.yc.english.intelligent.model.domain.URLConfig
import com.yc.english.intelligent.model.domain.VGInfoWarpper
import com.yc.english.main.hepler.UserInfoHelper
import rx.Observable

/**
 * Created by zhangkai on 2017/12/4.
 */

class IntelligentHandInEngin(context: Context?) : BaseEngin(context) {
    fun submitAnwsers(answer_list: String, use_time: String): Observable<ResultInfo<VGInfoWarpper>> {
        var uid = ""
        if (UserInfoHelper.getUserInfo() != null) {
            uid = UserInfoHelper.getUserInfo().uid
        }
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.UPDATE_ANSWERS, object :
                TypeReference<ResultInfo<VGInfoWarpper>>() {}.type, mutableMapOf("answer_list" to answer_list,
                "user_id" to uid,
                "use_time" to use_time),
                true, true, true) as Observable<ResultInfo<VGInfoWarpper>>
    }

    fun submitAnwsers(answer_list: String): Observable<ResultInfo<VGInfoWarpper>> {
        return submitAnwsers(answer_list, "")
    }
}