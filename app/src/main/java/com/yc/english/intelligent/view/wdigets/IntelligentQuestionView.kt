package com.yc.english.intelligent.view.wdigets

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView
import com.yc.english.news.view.widget.MediaPlayerView

/**
 * Created by zhangkai on 2017/12/6.
 */

class IntelligentQuestionView : BaseView {
    override fun getLayoutId() = R.layout.intelligent_view_question

    var mDescTextView = findViewById<TextView>(R.id.mDescTextView)
    var mDescAudioPlayerView = findViewById<MediaPlayerView>(R.id.mDescAudioPlayerView)
    var mWebView = findViewById<WebView>(R.id.mWebView)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val webSettings = mWebView.getSettings()
        webSettings.setLoadsImagesAutomatically(false)
        webSettings.setAllowUniversalAccessFromFileURLs(true)
        webSettings.setJavaScriptEnabled(true)
        webSettings.setDomStorageEnabled(true)
        webSettings.setNeedInitialFocus(false)
        webSettings.setSupportZoom(false)
        webSettings.setBuiltInZoomControls(false)
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true)
        webSettings.setAllowFileAccess(true)
        webSettings.setDefaultTextEncodingName("UTF-8")
        webSettings.setAppCacheEnabled(true)
        webSettings.setDatabaseEnabled(true)
        webSettings.setLoadsImagesAutomatically(true)
        mWebView.addJavascriptInterface(QuestionHandle(), "question")

    }

    var text: String? = ""
        set(value) {
            if (TextUtils.isEmpty(value)) {
                mDescTextView.visibility = View.GONE
            } else {
                mDescTextView.visibility = View.VISIBLE
                mDescTextView.text = value
            }
            field = value
        }

    var media: String? = ""
        set(value) {
            if (TextUtils.isEmpty(value)) {
                mDescAudioPlayerView.visibility = View.GONE
            } else {
                mDescAudioPlayerView.visibility = View.VISIBLE
                mDescAudioPlayerView.setPath(value)
            }
            field = value
        }

    var webview: String? = ""
        set(value) {
            if (TextUtils.isEmpty(value)) {
                mWebView.visibility = View.GONE
            } else {
                (this@IntelligentQuestionView.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                mWebView.visibility = View.VISIBLE
                mWebView.loadDataWithBaseURL(null, value, "text/html", "UTF-8", null);
            }
            field = value
        }

    class QuestionHandle {

    }
}