package com.yc.english.intelligent.view.wdigets

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView

/**
 * Created by zhangkai on 2017/11/27.
 */
class IntelligentTypeItemView : BaseView {
    override fun getLayoutId() = R.layout.intelligent_type_item_view

    val mTitleTextView = findViewById(R.id.tvTitle) as TextView
    val mDoTextView = findViewById(R.id.tvDo) as TextView
    val mIconImageView = findViewById(R.id.ivIcon) as ImageView

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.tab_item)

        val title = a?.getText(R.styleable.tab_item_text)
        if (title != null) {
            mTitleTextView.setText(title)
        }
        val iconSrc = a?.getDrawable(R.styleable.tab_item_src)
        if (iconSrc != null) {
            mIconImageView.setImageDrawable(iconSrc)
        }
//
    }



    var complete: Boolean = false
        get() {
            return field
        }
        set(value) {
            if (value) {
                mDoTextView.setText(context.getString(R.string.intelligents_end))
                mDoTextView.setBackground(ContextCompat.getDrawable(context, (R.drawable.intelligents_type_btn_end_bg)))
                field = value
            } else {
                mDoTextView.setText(context.getString(R.string.intelligents_start))
                mDoTextView.setBackground(ContextCompat.getDrawable(context, (R.drawable.intelligents_type_btn_bg)))
                field = value
            }
        }

}