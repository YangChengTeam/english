package com.yc.english.intelligent.utils

import android.text.Html
import android.text.Spanned


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

inline fun timeShortFormat(time: Int) = if(time >= 10) "${time}" else "0$time"


