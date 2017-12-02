package com.yc.english.intelligent.view.wdigets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView

/**
 * Created by zhangkai on 2017/11/30.
 */
class IntelligentPushQuestionView: BaseView {
    val mTitleTextView = findViewById<TextView>(R.id.tvTitle)
    val mIconImageView = findViewById<ImageView>(R.id.ivIcon)

    override fun getLayoutId() = R.layout.intelligent_view_push_question

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        val a = context?.obtainStyledAttributes(attrs, R.styleable.tab_item)
        val title = a?.getText(R.styleable.tab_item_text)
        if (title != null) {
            mTitleTextView.setText(title)
        }
        val iconSrc = a?.getDrawable(R.styleable.tab_item_src)
        if (iconSrc != null) {
            mIconImageView.setImageDrawable(iconSrc)
        }
    }

}