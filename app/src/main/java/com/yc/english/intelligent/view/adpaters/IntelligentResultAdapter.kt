package com.yc.english.intelligent.view.adpaters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R
import com.yc.english.intelligent.model.domain.QuestionInfo

/**
 * Created by zhangkai on 2017/12/1.
 */
class IntelligentResultAdapter : BaseQuickAdapter<QuestionInfo, BaseViewHolder>(R.layout.intelligent_item_result) {
    override fun convert(helper: BaseViewHolder?, item: QuestionInfo?) {
        helper?.setText(R.id.tv_index, (helper?.adapterPosition + 1).toString())

        if(item!!.right){
            helper?.setBackgroundRes(R.id.tv_index, R.drawable.intelligent_answer_right)
        } else {
            helper?.setBackgroundRes(R.id.tv_index, R.drawable.intelligent_answer_error)
        }
    }
}