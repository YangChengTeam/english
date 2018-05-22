package com.yc.english.intelligent.view.wdigets

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView
import com.yc.english.news.view.widget.MediaPlayerView
import kotlinx.android.synthetic.main.intelligent_view_question.view.*

/**
 * Created by zhangkai on 2017/12/6.
 */

class IntelligentQuestionView : BaseView {
    override fun getLayoutId() = R.layout.intelligent_view_question

    var mTitleTextView = findViewById(R.id.mTitleTextView) as TextView
    val mDescAudioPlayerView = findViewById(R.id.mDescAudioPlayerView) as MediaPlayerView
    var mWebView = findViewById(R.id.mWebView) as WebView

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
                mTitleTextView.visibility = View.GONE
            } else {
                mTitleTextView.visibility = View.VISIBLE
                mTitleTextView.text = value
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
                mLineView.visibility = View.GONE
            } else {
                mWebView.visibility = View.VISIBLE
                mLineView.visibility = View.VISIBLE
                mWebView.loadDataWithBaseURL(null, value, "text/html", "UTF-8", null);
            }
            field = value
        }

    class QuestionHandle {

    }
}