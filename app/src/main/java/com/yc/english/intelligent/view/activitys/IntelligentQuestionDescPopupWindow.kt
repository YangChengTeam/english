package com.yc.english.intelligent.view.activitys

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.webkit.WebView
import butterknife.BindView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.hwangjr.rxbus.RxBus
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.yc.english.R
import com.yc.english.base.helper.RxUtils
import com.yc.english.base.utils.SimpleCacheUtils
import com.yc.english.base.view.BasePopupWindow
import com.yc.english.intelligent.model.domain.VGInfoWarpper
import com.yc.english.intelligent.model.engin.IntelligentTypeEngin
import com.yc.english.intelligent.view.adpaters.IntelligentVGAdpater
import com.yc.english.main.model.domain.Constant
import java.util.*


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionDescPopupWindow(context: Activity) : BasePopupWindow(context) {
    override fun getAnimationID() = 0
    override fun getLayoutId() = R.layout.intelligent_ppw_question_desc


    @BindView(R.id.webView)
    lateinit var mWebView: WebView

    override fun init() {

    }
}