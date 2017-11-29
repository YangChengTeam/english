package com.yc.english.intelligent.view.activitys

import android.support.v4.view.ViewPager
import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import com.yc.english.weixin.views.utils.TabsUtils
import kotlinx.android.synthetic.main.intelligent_activity_questions.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */
class IntelligentQuestionsActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {
    override fun init() {
        mToolbarWarpper.total = 3
        mToolbarWarpper.index = 1

        val mFragmentAdapter = TabsUtils.IntelligentQuestionsFragmentAdapter(supportFragmentManager, arrayOf("", "", ""))
        mViewPager.setAdapter(mFragmentAdapter)
        mViewPager.setCurrentItem(0)
        mViewPager.setOffscreenPageLimit(1)
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                mToolbarWarpper.index = i + 1
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })



        StatusBarCompat.compat(this, mToolbarWarpper, mToolbarWarpper.mToolbar, mToolbarWarpper.mStatubar)

        RxView.clicks(mToolbarWarpper.mBackBtn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { finish() }
        mToolbarWarpper.startTime()
    }

    override fun getLayoutId() = R.layout.intelligent_activity_questions

}