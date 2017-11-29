package com.yc.english.intelligent.view.wdigets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView


/**
 * Created by zhangkai on 2017/11/29.
 */

class IntelligentItemScoreView : BaseView {
    override fun getLayoutId() = R.layout.intelligent_view_item_score

    var mTitleTextView = findViewById<TextView>(R.id.tv_title)
    var mEnTitleTextView = findViewById<TextView>(R.id.tv_en_title)
    var mProgressView = findViewById<View>(R.id.v_progress)
    var mScoreTextView = findViewById<TextView>(R.id.tv_score)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.tab_item)
        val title = a?.getText(R.styleable.tab_item_text)
        if (title != null) {
            mTitleTextView.setText(title)
        }
        val entitle = a?.getText(R.styleable.tab_item_desc)
        if (entitle != null) {
            mEnTitleTextView.setText(entitle)
        }
    }

    var progress: Int = 0
        set(value) {
            mProgressView.layoutParams.width = (mScoreTextView.parent as ViewGroup).width * value / 100
            mScoreTextView.text = "${value}%"
        }


}