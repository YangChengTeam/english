package com.yc.english.intelligent.view.adpaters

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R

/**
 *
 * Created by wanglin  on 2018/4/19 16:32.
 */
class IntelligentReportErrorItemAdapter(errors: List<String>, var type: String) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.intelligent_report_error_answer, errors) {


    override fun convert(helper: BaseViewHolder?, item: String?) {
        if (type == "text") {
            helper!!.setText(R.id.tv_answer, item)
        } else if (type == "image") {
            Glide.with(mContext).load(item).into(helper!!.getView(R.id.iv_answer))
        }

    }
}