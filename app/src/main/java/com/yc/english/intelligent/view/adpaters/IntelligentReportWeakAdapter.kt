package com.yc.english.intelligent.view.adpaters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper

/**
 *
 * Created by wanglin  on 2018/4/17 10:59.
 */
class IntelligentReportWeakAdapter(reports: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.intelligent_report_item, reports) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper!!.setText(R.id.tv_title, item)
    }


}