package com.yc.english.intelligent.view.activitys

import android.app.Activity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import butterknife.BindView
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.hwangjr.rxbus.RxBus
import com.jakewharton.rxbinding.view.RxView
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
import java.util.concurrent.TimeUnit


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionDescPopupWindow(context: Activity) : BasePopupWindow(context) {
    override fun getAnimationID() = R.style.share_anim
    override fun getLayoutId() = R.layout.intelligent_ppw_question_desc


    @BindView(R.id.webView)
    lateinit var mWebView: WebView

    @BindView(R.id.mCloseBtn)
    lateinit var mCloseBtn: LinearLayout

    override fun init() {
        val webSettings = mWebView.getSettings()
        webSettings.setJavaScriptEnabled(true)

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true) //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小


        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK) //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true) //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true) //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true) //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8")//设置编码格式
        webSettings.setBlockNetworkImage(true)//设置是否加载网络图片 true 为不加载 false 为加载

        RxView.clicks(mCloseBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            dismiss()
        }
    }

    fun loadHtml(body: String) {
        mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null)
    }
}