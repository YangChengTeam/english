package com.yc.english.intelligent.utils

import android.text.Html
import android.text.Spanned
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by zhangkai on 2017/11/28.
 */
@SuppressWarnings("deprecation")
inline fun fromHtml(html: String): Spanned {
    val result: Spanned
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        result = Html.fromHtml(html)
    }
    return result
}

inline fun timeShortFormat(time: Int) = if (time >= 10) "${time}" else "0$time"

fun getLevel1QuestionInfo(infos: List<QuestionInfoWrapper.QuestionInfo>): List<QuestionInfoWrapper.QuestionInfo> {
    var actIndex = 0
    var frgIndex = 0
    val questionInfos = ArrayList<QuestionInfoWrapper.QuestionInfo>()
    for (questionInfo in infos) {
        questionInfo.actIndex = actIndex
        questionInfos.add(questionInfo)
        if (questionInfo.data != null) {
            for (questionInfo2 in questionInfo.data!!) {
                questionInfo2.frgIndex = frgIndex++
                questionInfo2.actIndex = actIndex
                questionInfos.add(questionInfo2)
            }
        }
        actIndex++
        frgIndex = 0
    }
    return questionInfos
}


