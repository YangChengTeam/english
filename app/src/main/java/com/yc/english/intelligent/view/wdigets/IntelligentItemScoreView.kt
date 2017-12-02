package com.yc.english.intelligent.view.wdigets

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yc.english.R
import com.yc.english.base.view.BaseView
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Build
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator


/**
 * Created by zhangkai on 2017/11/29.
 */

class IntelligentItemScoreView : BaseView {
    override fun getLayoutId() = R.layout.intelligent_view_item_score

    var mTitleTextView = findViewById<TextView>(R.id.tv_title)
    var mEnTitleTextView = findViewById<TextView>(R.id.tv_en_title)
    var mProgressView = findViewById<View>(R.id.v_progress)
    var mScoreTextView = findViewById<TextView>(R.id.tv_score)

    var mProgressWidth: Int = 0

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


    fun progress(value: Int) {
        val viewTreeObserver = mProgressView.getViewTreeObserver()
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mProgressView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mProgressWidth = (mProgressView.getParent() as ViewGroup).width
                    progress = value
                }
            })
        }
    }

    var progress: Int = 0
        set(value) {
            val anim = ValueAnimator.ofFloat(mProgressWidth * value * 1.0f / 100f)
            anim.duration = 1000
            anim.interpolator = AccelerateInterpolator()
            anim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    mProgressView.layoutParams.width = (animation.animatedValue as Float).toInt()
                    mProgressView.requestLayout()
                }
            })
            anim.start()
            mScoreTextView.text = "${value}%"
            field = value
        }


}