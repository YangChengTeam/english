package com.yc.english.intelligent.view.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.blankj.subutil.util.ThreadPoolUtils
import com.blankj.utilcode.util.FileIOUtils
import com.kk.utils.PathUtils
import com.kk.utils.UIUitls
import com.yc.english.R
import com.yc.english.R.mipmap.intellgent_main_bg
import com.yc.english.base.utils.Blur
import com.yc.english.base.view.BaseFragment
import com.yc.english.group.utils.BitmapUtils
import com.yc.english.intelligent.presenter.IntelligentPresenter
import kotlinx.android.synthetic.main.intelligent_fragment_index.*


/**
 * Created by zhangkai on 2017/11/27.
 */

class IntelligentFragment : BaseFragment<IntelligentPresenter>() {
    init {
        isUseInKotlin = true
    }

    lateinit var type: String

    override fun init() {
        ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute {
            val bimap = Blur.fastblur(activity, BitmapFactory.decodeResource(context!!.resources, R.mipmap
                    .intellgent_main_bg)
                    , 25)
            UIUitls.post {
                mInfoBg.setImageBitmap(bimap)
            }
        }

        mIntelligentType1.complete = true
    }


    override fun getLayoutId() = R.layout.intelligent_fragment_index

}