package com.yc.english.intelligent.view.fragments

import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.support.annotation.MainThread
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.blankj.subutil.util.ThreadPoolUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.UIUitls
import com.shizhefei.view.indicator.slidebar.ColorBar
import com.shizhefei.view.indicator.transition.OnTransitionTextListener
import com.yc.english.R
import com.yc.english.base.utils.Blur
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.BaseFragment
import com.yc.english.intelligent.contract.IntelligentTypeContract
import com.yc.english.intelligent.model.domain.UnitInfoWrapper
import com.yc.english.intelligent.model.domain.VGInfoWarpper
import com.yc.english.intelligent.presenter.IntelligentTypePresenter
import com.yc.english.intelligent.utils.fromHtml
import com.yc.english.intelligent.view.activitys.IntelligentVGSelectPopupWindow
import com.yc.english.main.model.domain.Constant
import com.yc.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_type_fragment_index.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/24.
 */

open class IntelligentTypeFragment : BaseFragment<IntelligentTypePresenter>(), IntelligentTypeContract.View {
    init {
        isUseInKotlin = true
    }

    override fun init() {
        mPresenter = IntelligentTypePresenter(activity, this)
        StatusBarCompat.compat(activity as BaseActivity<*>, mToolbarWarpper, mToolbar, R.mipmap.base_actionbar)

        mScrollIndicatorView.setScrollBar(ColorBar(activity, ContextCompat.getColor(context!!, R.color
                .primary), 6))
        val unSelectSize = 15f
        val selectSize = 15f
        val selectColor = ContextCompat.getColor(activity, R.color.primary)
        val unSelectColor = ContextCompat.getColor(activity, R.color.black_333)
        mScrollIndicatorView.setOnTransitionListener(OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize))
        mScrollIndicatorView.setOnIndicatorItemClickListener({ clickItemView, position ->
            mViewPager.setCurrentItem(position)
            false
        })
        mViewPager.setOffscreenPageLimit(1)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                mScrollIndicatorView.setCurrentItem(i, true)
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        RxView.clicks(mIntelligentType).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            IntelligentVGSelectPopupWindow(activity).show(activity.window.decorView.rootView, Gravity.CENTER)
        }

        ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute {
            val bimap = Blur.fastblur(activity, BitmapFactory.decodeResource(context!!.resources, R.mipmap
                    .intellgent_main_bg)
                    , 25)
            UIUitls.post {
                mInfoBg.setImageBitmap(bimap)
            }
        }
    }

    override fun hideStateView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @MainThread
    override fun showTitle(title: String) {
        activity.runOnUiThread {
            mTitleTextView.text = title
        }
    }

    @MainThread
    override fun showInfo(titles: Array<String?>, types: Array<UnitInfoWrapper.UnitInfo?>) {
        activity.runOnUiThread {
            mScrollIndicatorView.setAdapter(TabsUtils.MyAdapter(activity, titles, SizeUtils.dp2px(72f)))
            val mFragmentAdapter = TabsUtils.IntelligentFragmentAdapter(childFragmentManager, types)
            mViewPager.setAdapter(mFragmentAdapter)
            mScrollIndicatorView.setCurrentItem(0, true)
            mViewPager.setCurrentItem(0)
        }
    }




    override fun getLayoutId() = R.layout.intelligent_type_fragment_index


}
