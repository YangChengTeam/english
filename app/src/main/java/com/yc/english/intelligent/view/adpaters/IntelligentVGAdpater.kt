package com.yc.english.intelligent.view.adpaters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yc.english.R
import com.yc.english.intelligent.model.domain.VGInfo

/**
 * Created by zhangkai on 2017/11/29.
 */

class IntelligentVGAdpater : BaseQuickAdapter<VGInfo, BaseViewHolder>(R.layout.intelligent_item_vg_view) {

    override fun convert(helper: BaseViewHolder?, item: VGInfo?) {
        helper?.setText(R.id.tv_title, item?.name)
    }
}