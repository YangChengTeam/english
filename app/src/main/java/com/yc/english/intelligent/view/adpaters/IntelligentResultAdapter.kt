package com.yc.english.intelligent.view.adpaters

import android.graphics.Color
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hwangjr.rxbus.RxBus
import com.yc.english.R
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper
import com.yc.english.intelligent.view.activitys.IntelligentQuestionsActivity
import com.yc.english.main.model.domain.Constant

/**
 * Created by zhangkai on 2017/12/1.
 */
class IntelligentResultAdapter : BaseMultiItemQuickAdapter<QuestionInfoWrapper.QuestionInfo, BaseViewHolder> {
    var index = 0
    var rightCount = 0
    var errorCount = 0

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
                helper?.getView<TextView>(R.id.tv_answer).setTextColor(Color.WHITE)
                helper?.getView<TextView>(R.id.tv_answer).text = item?.userAnswer ?: SPUtils.getInstance().getString("userAnswer${item?.id}", "")
                if (item?.userAnswer != "" && item?.userAnswer == item?.answer) {
                    rightCount++
                    helper?.setBackgroundRes(R.id.tv_answer, R.drawable.intelligent_answer_right)
                } else {
                    errorCount++
                    helper?.setBackgroundRes(R.id.tv_answer, R.drawable.intelligent_answer_error)
                }
            }
        }

        if (data.size - 1 == helper?.adapterPosition) {
            RxBus.get().post(Constant.RESULT_IN, "${item?.id}$")
        }
    }
}