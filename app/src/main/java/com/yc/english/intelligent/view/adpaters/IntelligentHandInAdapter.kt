package com.yc.english.intelligent.view.adpaters

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R
import com.yc.english.intelligent.model.domain.QuestionInfo

/**
 * Created by zhangkai on 2017/12/1.
 */
class IntelligentHandInAdapter : BaseQuickAdapter<QuestionInfo, BaseViewHolder>(R.layout.intelligent_item_hand_in) {

    override fun convert(helper: BaseViewHolder?, item: QuestionInfo?) {
        helper?.setText(R.id.tv_index, (helper?.adapterPosition + 1).toString())
        helper?.setText(R.id.tv_answer, item?.answer)
    }
}