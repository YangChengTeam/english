package com.yc.english.intelligent.view.adpaters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R
import com.yc.english.intelligent.model.domain.ReportErrorInfo

/**
 *
 * Created by wanglin  on 2018/4/19 16:32.
 */
class IntelligentReportErrorItemAdapter(errors: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.intelligent_report_error_answer, errors) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper!!.setText(R.id.tv_answer, item)

    }
}