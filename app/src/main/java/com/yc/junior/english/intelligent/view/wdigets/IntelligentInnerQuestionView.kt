package com.yc.junior.english.intelligent.view.wdigets

import android.content.Context

import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yc.junior.english.R
import com.yc.junior.english.intelligent.utils.fromHtml
import com.yc.junior.english.news.view.widget.MediaPlayerView
import kotlinx.android.synthetic.main.intelligent_view_inner_question.view.*
import yc.com.base.BaseView

/**
 * Created by zhangkai on 2017/12/6.
 */
class IntelligentInnerQuestionView : BaseView {

    var mTitleTextView = findViewById(R.id.mTitleTextView) as TextView
    val mTitleAudioPlayerView by lazy {
        findViewById(R.id.mTitleAudioPlayerView) as MediaPlayerView
    }
    var mRecyclerView = findViewById(R.id.mRecyclerView) as RecyclerView

    var mAnswerTextView = findViewById(R.id.mAnswerTextView) as TextView
    var mAnalysisTextView = findViewById(R.id.mAnalysisTextView) as TextView
    var mAnalysisView = findViewById(R.id.ll_analysis) as LinearLayout

    override fun getLayoutId() = R.layout.intelligent_view_inner_question

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mAnalysisView.visibility = View.GONE

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
                mTitleAudioPlayerView.visibility = View.GONE
            } else {
                mTitleAudioPlayerView.visibility = View.VISIBLE
                mTitleAudioPlayerView.setPath(value)
            }
            field = value
        }


    fun analysis(answer: String?, analysis: String?) {
        mAnalysisView.visibility = View.VISIBLE
        mAnswerTextView.text = fromHtml("正确答案  <font color=#6ec82d>${answer ?: ""}</font>")
        mAnalysisTextView.text = analysis ?: ""
        if (TextUtils.isEmpty(analysis ?: "")) {
            mAnsTitleTextView.visibility = View.GONE
        }
    }

}