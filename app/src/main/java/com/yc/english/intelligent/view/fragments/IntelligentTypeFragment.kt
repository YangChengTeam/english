package com.yc.english.intelligent.view.fragments

import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.blankj.utilcode.util.SizeUtils
import com.jakewharton.rxbinding.view.RxView
import com.shizhefei.view.indicator.slidebar.ColorBar
import com.shizhefei.view.indicator.transition.OnTransitionTextListener
import com.yc.english.R
import com.yc.english.base.view.BaseFragment
import com.yc.english.intelligent.presenter.IntelligentPresenter
import com.yc.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_type_fragment_index.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/24.
 */

open class IntelligentTypeFragment : BaseFragment<IntelligentPresenter>() {
    private val titles = arrayOf("Unit 1", "Unit 2", "Unit 3", "Unit 4", "Unit 5", "Unit 6", "Unit 7", "Unit 8")
    private val types = arrayOf("Unit 1", "Unit 2", "Unit 3", "Unit 4", "Unit 5", "Unit 6", "Unit 7", "Unit 8")

    init {
        isUseInKotlin = true
    }

    override fun init() {
        mPresenter = IntelligentPresenter(getActivity(), this)

        mScrollIndicatorView.setAdapter(TabsUtils.MyAdapter(activity, titles, SizeUtils.dp2px(72f)))
        mScrollIndicatorView.setScrollBar(ColorBar(activity, ContextCompat.getColor(activity, R.color
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
        mScrollIndicatorView.setCurrentItem(0, true)

        val mFragmentAdapter = TabsUtils.MyFragmentAdapter(childFragmentManager, types)
        mViewPager.setAdapter(mFragmentAdapter)
        mViewPager.setCurrentItem(0)
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

        }

        toolbarCompat()
    }

    private fun toolbarCompat() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mToolbarWarpper.getLayoutParams().height = SizeUtils.dp2px(48f)
            mToolbarWarpper.setBackground(ContextCompat.getDrawable(activity, R.mipmap.base_actionbar))
        } else {
            setToolbarTopMargin(mToolbar)
            mToolbar.layoutParams.height = SizeUtils.dp2px(72f) - statusbarHeight
        }
    }

    override fun getLayoutId() = R.layout.intelligent_type_fragment_index
}
