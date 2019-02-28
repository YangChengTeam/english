package com.yc.junior.english.intelligent.view.adpaters

import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.junior.english.R
import com.yc.junior.english.base.helper.GlideHelper
import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper
import yc.com.blankj.utilcode.util.SPUtils

/**
 * Created by zhangkai on 2017/12/4.
 */
class IntelligentOptionsAdapter(val questionInfo: QuestionInfoWrapper.QuestionInfo?, var isResult: Boolean) :
        BaseQuickAdapter<String,
                BaseViewHolder>(R.layout
                .intelligent_item_options) {

    override fun convert(helper: BaseViewHolder?, item: String?) {


        if ((questionInfo?.options?.type ?: "text") == "text") {
            helper?.getView<TextView>(R.id.tv_answer_text)?.visibility = View.VISIBLE
            helper?.getView<ImageView>(R.id.iv_answer_image)?.visibility = View.GONE
            font(item!!, helper?.getView<TextView>(R.id.tv_answer_text))
            helper?.setText(R.id.tv_answer_text, item)
        } else {
            helper?.getView<TextView>(R.id.tv_answer_text)?.visibility = View.GONE
            helper?.getView<ImageView>(R.id.iv_answer_image)?.visibility = View.VISIBLE
            GlideHelper.imageView(mContext, helper?.getView<ImageView>(R.id.iv_answer_image), item, R.mipmap.default_book)
        }

        if (TextUtils.isEmpty(questionInfo?.userAnswer) && isResult) {
            questionInfo?.userAnswer = SPUtils.getInstance().getString("userAnswer${questionInfo?.id}", "")
        }

        val cAnswer = (65 + helper?.adapterPosition!!).toChar().toString()
        helper.setText(R.id.tv_answer, cAnswer)


        if (!isResult) {
            helper.getView<TextView>(R.id.tv_answer).isSelected = (questionInfo?.userAnswer == cAnswer)
        } else {
            if (questionInfo?.userAnswer == cAnswer) {
                if (questionInfo.userAnswer != "" && questionInfo.userAnswer == questionInfo.answer) {
                    helper.getView<TextView>(R.id.tv_answer).setTextColor(Color.WHITE)
                    helper.setBackgroundRes(R.id.tv_answer, R.drawable.intelligent_answer_right)
                } else {
                    helper.getView<TextView>(R.id.tv_answer).setTextColor(Color.WHITE)
                    helper.setBackgroundRes(R.id.tv_answer, R.drawable.intelligent_answer_error)
                }
            } else {
                helper.getView<TextView>(R.id.tv_answer).isSelected = false
            }
        }
    }


    private fun font(text: String, textView: TextView?) {
        if (text.contains("**")) {
            textView?.setTypeface(null, Typeface.ITALIC)
            textView?.text = text.replace("**", "")
        }
        textView?.text = text
    }
}