package com.yc.junior.english.intelligent.view.fragments

import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.Gravity
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.shizhefei.view.indicator.slidebar.ColorBar
import com.shizhefei.view.indicator.transition.OnTransitionTextListener
import com.yc.junior.english.R
import com.yc.junior.english.base.utils.Blur
import com.yc.junior.english.base.utils.StatusBarCompat
import com.yc.junior.english.base.view.BaseActivity
import com.yc.junior.english.base.view.BaseFragment
import com.yc.junior.english.intelligent.contract.IntelligentTypeContract
import com.yc.junior.english.intelligent.model.domain.UnitInfoWrapper
import com.yc.junior.english.intelligent.presenter.IntelligentTypePresenter
import com.yc.junior.english.intelligent.view.activitys.IntelligentVGSelectPopupWindow
import com.yc.junior.english.main.model.domain.Constant
import com.yc.junior.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_type_fragment_index.*
import yc.com.base.ThreadPoolUtils
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.blankj.utilcode.util.SizeUtils
import yc.com.blankj.utilcode.util.UIUitls
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/24.
 */

open class IntelligentTypeFragment : BaseFragment<IntelligentTypePresenter>(), IntelligentTypeContract.View {
    init {
        isUseInKotlin = true
    }

    var popupWindow: IntelligentVGSelectPopupWindow? = null
    override fun init() {
        mPresenter = IntelligentTypePresenter(activity!!, this)
        StatusBarCompat.compat(activity as BaseActivity<*>, mToolbarWarpper, mToolbar, R.mipmap.base_actionbar)

        mScrollIndicatorView.setScrollBar(ColorBar(activity, ContextCompat.getColor(context!!, R.color
                .primary), 6))
        val unSelectSize = 15f
        val selectSize = 15f
        val selectColor = ContextCompat.getColor(context!!, R.color.primary)
        val unSelectColor = ContextCompat.getColor(context!!, R.color.black_333)
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

                SPUtils.getInstance().put(Constant.INTELLIGENT_POS, i)

            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })

        RxView.clicks(mIntelligentType).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val popupWindow = IntelligentVGSelectPopupWindow(activity!!)
            popupWindow.show(activity!!.window.decorView.rootView, Gravity.CENTER)
            iv_select.setImageResource(R.mipmap.arrow_up)
            popupWindow.setOnDismissListener { iv_select.setImageResource(R.mipmap.arrow_down) }

        }


        ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute {
            val bimap = Blur.fastblur(activity, BitmapFactory.decodeResource(context!!.resources, R.mipmap
                    .intellgent_main_bg)
                    , 25)
            UIUitls.post {
                mInfoBg.setImageBitmap(bimap)
            }
        }

        if (!SPUtils.getInstance().getString("period", "").isEmpty()) {
            mPresenter.getUnit("loadData")
        }

    }



    override fun showLoading() {
    }

    override fun showTitle(title: String) {
        activity!!.runOnUiThread {

            mTitleTextView.text = title
        }
    }

    var unitInfos: Array<UnitInfoWrapper.UnitInfo?>? = null
    override fun showInfo(titles: Array<String?>, types: Array<UnitInfoWrapper.UnitInfo?>) {
        activity!!.runOnUiThread {
            unitInfos = types
            mScrollIndicatorView.setAdapter(TabsUtils.MyAdapter(activity, titles, SizeUtils.dp2px(72f)))
            val mFragmentAdapter = TabsUtils.IntelligentFragmentAdapter(childFragmentManager, types)
            mViewPager.adapter = mFragmentAdapter

            val index = SPUtils.getInstance().getInt(Constant.INTELLIGENT_POS, 0)
            mScrollIndicatorView.setCurrentItem(index, true)
            mViewPager.currentItem = index
        }
    }

    override fun getLayoutId() = R.layout.intelligent_type_fragment_index


    @Subscribe(thread = EventThread.NEW_THREAD, tags = arrayOf(Tag(Constant.REMOVE_ANSWER)))
    fun clear(tag: String) {
        mPresenter.getUnit("loadData")
    }


}
