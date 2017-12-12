package com.yc.english.intelligent.view.adpaters

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper


/**
 * Created by zhangkai on 2017/12/1.
 */
class IntelligentHandInAdapter : BaseMultiItemQuickAdapter<QuestionInfoWrapper.QuestionInfo, BaseViewHolder> {

    var index = 0

    constructor(data: List<QuestionInfoWrapper.QuestionInfo>) : super(data) {
        addItemType(0, R.layout.intelligent_item_hand_in_title)
        addItemType(1, R.layout.intelligent_item_hand_in)
    }


    override fun convert(helper: BaseViewHolder?, item: QuestionInfoWrapper.QuestionInfo?) {
        when (helper?.itemViewType) {
            0 -> {
                helper?.setText(R.id.tv_title, item?.title)
                index = 0
            }
            1 -> {
                helper?.setText(R.id.tv_index, (++index).toString())
                helper?.setText(R.id.tv_answer, item?.userAnswer)
            }
        }
    }

}