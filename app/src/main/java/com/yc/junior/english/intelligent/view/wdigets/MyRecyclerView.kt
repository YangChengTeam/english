package com.yc.junior.english.intelligent.view.wdigets

import android.content.Context

import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * Created by wanglin  on 2018/4/20 08:49.
 */
class MyRecyclerView(context: Context, attrs: AttributeSet, defStyle: Int) : RecyclerView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs!!, 0)
    constructor(context: Context) : this(context, null)


    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return false
    }


}