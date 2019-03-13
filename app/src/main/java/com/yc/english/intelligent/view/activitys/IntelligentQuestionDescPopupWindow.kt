package com.yc.english.intelligent.view.activitys

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView

import com.jakewharton.rxbinding.view.RxView
import com.umeng.analytics.MobclickAgent
import com.yc.english.R
import com.yc.english.base.view.BasePopupWindow
import java.util.concurrent.TimeUnit


/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionDescPopupWindow(context: Activity) : BasePopupWindow(context) {
    override fun getAnimationID() = R.style.share_anim
    override fun getLayoutId() = R.layout.intelligent_ppw_question_desc


    @BindView(R.id.webView)
    lateinit var mWebView: WebView

    @BindView(R.id.textView)
    lateinit var mTextView: TextView

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


    fun loadHtml(body: String, type: Int) {
        if (type == 1) {
            mWebView.visibility = View.VISIBLE
            mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null)
        } else {
            mTextView.text = body
            (mTextView.parent.parent as ViewGroup).visibility = View.VISIBLE
        }
    }
}