package com.yc.junior.english.intelligent.view.wdigets

import android.content.Context

import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.yc.junior.english.R
import yc.com.base.BaseView

/**
 * Created by zhangkai on 2017/11/27.
 */
class IntelligentTypeItemView : BaseView {
    override fun getLayoutId() = R.layout.intelligent_type_item_view

    val mTitleTextView = findViewById(R.id.tvTitle) as TextView
    val mDoTextView = findViewById(R.id.tvDo) as TextView
    val mIconImageView = findViewById(R.id.ivIcon) as ImageView


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.tab_item)

        try {
            val title = a?.getText(R.styleable.tab_item_text)
            if (title != null) {
                mTitleTextView.setText(title)
            }
            val iconSrc = a?.getDrawable(R.styleable.tab_item_src)
            if (iconSrc != null) {
                mIconImageView.setImageDrawable(iconSrc)
            }
        } finally {
            a.recycle()
        }


//
    }


    var complete: Boolean = false
        set(value) {
            field = if (value) {
                mDoTextView.text = context.getString(R.string.intelligents_end)
                mDoTextView.background = ContextCompat.getDrawable(context, (R.drawable.intelligents_type_btn_end_bg))
                value
            } else {
                mDoTextView.text = context.getString(R.string.intelligents_start)
                mDoTextView.background = ContextCompat.getDrawable(context, (R.drawable.intelligents_type_btn_bg))
                value
            }
        }

}