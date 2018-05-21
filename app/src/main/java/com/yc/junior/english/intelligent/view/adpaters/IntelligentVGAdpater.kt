package com.yc.junior.english.intelligent.view.adpaters

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.junior.english.R
import com.yc.junior.english.intelligent.model.domain.VGInfoWarpper

/**
 * Created by zhangkai on 2017/11/29.
 */

class IntelligentVGAdpater : BaseQuickAdapter<VGInfoWarpper.VGInfo, BaseViewHolder>(R.layout.intelligent_item_vg_view) {

    var defaultInfo: VGInfoWarpper.VGInfo? = null

    override fun convert(helper: BaseViewHolder?, item: VGInfoWarpper.VGInfo?) {
        if (item?.id == defaultInfo?.id) {
            helper?.getView<TextView>(R.id.tv_title)?.isSelected = true
        } else if (defaultInfo?.id == -1 && (item?.grade == defaultInfo?.grade && item?.partType == defaultInfo?.partType)) {
            helper?.getView<TextView>(R.id.tv_title)?.isSelected = true
        } else {
            helper?.getView<TextView>(R.id.tv_title)?.isSelected = false
        }

        helper?.setText(R.id.tv_title, item?.name ?: item?.title ?: "")
    }
}