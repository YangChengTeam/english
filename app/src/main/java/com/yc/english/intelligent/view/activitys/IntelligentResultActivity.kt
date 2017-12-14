package com.yc.english.intelligent.view.activitys

import android.support.v7.widget.GridLayoutManager
import android.view.KeyEvent
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.google.gson.Gson
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.jakewharton.rxbinding.view.RxView
import com.yc.english.R
import com.yc.english.base.model.BaseEngin
import com.yc.english.base.presenter.BasePresenter
import com.yc.english.base.utils.SimpleCacheUtils
import com.yc.english.base.utils.StatusBarCompat
import com.yc.english.base.view.AlertDialog
import com.yc.english.base.view.BaseActivity
import com.yc.english.base.view.IView
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.utils.fromHtml
import com.yc.english.intelligent.view.adpaters.IntelligentResultAdapter
import com.yc.english.main.model.domain.Constant
import kotlinx.android.synthetic.main.intelligent_activity_result.*
import java.util.concurrent.TimeUnit

/**
 * Created by zhangkai on 2017/11/28.
 */

class IntelligentResultActivity : BaseActivity<BasePresenter<BaseEngin, IView>>() {

    lateinit var adapter: IntelligentResultAdapter
    override fun init() {
        StatusBarCompat.compat(this, mToolbarWarpper, mToolbar, mStatusBar)

        RxView.clicks(mBackBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            back()
        }

        RxView.clicks(mBackBtn2).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            back()
        }


        RxView.clicks(mViewBtn).throttleFirst(200, TimeUnit
                .MILLISECONDS).subscribe {
            RxBus.get().post(Constant.RESULT_ANS, "from result")
            finish()
        }
        var questionInfos: ArrayList<QuestionInfoWrapper.QuestionInfo>? = intent.getParcelableArrayListExtra("questionInfos")
        if (questionInfos == null) {
            SimpleCacheUtils.readCache(this, IntelligentQuestionsActivity.getInstance()?.getResultKey() ?: "error",
                    object :
                            SimpleCacheUtils
                            .CacheRunnable
                            () {
                        override fun run() {
                            questionInfos = JSON.parseObject<ArrayList<QuestionInfoWrapper.QuestionInfo>>(json, object :
                                    TypeReference<ArrayList<QuestionInfoWrapper
                                    .QuestionInfo>>
                                    () {}.type)
                            this@IntelligentResultActivity.runOnUiThread {
                                showInfo(questionInfos!!)
                            }
                        }
                    })
        } else {
            showInfo(questionInfos!!)
        }
    }

    private fun showInfo(questionInfos: ArrayList<QuestionInfoWrapper.QuestionInfo>) {
        adapter = IntelligentResultAdapter(questionInfos)
        val gridLayoutManager = GridLayoutManager(this, 5)
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val question = adapter.data.get(position)
                return if (question.count > 1) 5 else 1
            }
        })
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = gridLayoutManager
        adapter!!.setOnItemClickListener { adapter, view, position ->
            if (adapter.getItemViewType(position) == 0) return@setOnItemClickListener
            val questionInfo = adapter.data.get(position) as QuestionInfoWrapper.QuestionInfo
            IntelligentQuestionsActivity.getInstance()?.next(questionInfo.actIndex, questionInfo.frgIndex)
            finish()
        }
    }

    private fun back() {
        IntelligentQuestionsActivity.getInstance()?.finish()
        finish()
    }


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constant.RESULT_IN)))
    fun showResult(tag: String) {
        mRightTextView.text = fromHtml("对<font color='#6ec82d'> ${adapter.rightCount} </font>题")
        mErrorTextView.text = fromHtml("错<font color='#ee5757'> ${adapter.errorCount}  </font>题")
    }

    override fun getLayoutId() = R.layout.intelligent_activity_result

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}