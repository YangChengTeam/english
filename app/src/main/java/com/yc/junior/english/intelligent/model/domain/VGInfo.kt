package com.yc.junior.english.intelligent.model.domain

import com.alibaba.fastjson.annotation.JSONField

/**
 * Created by zhangkai on 2017/11/29.
 */
class VGInfoWarpper {
    var list: List<VGInfo>? = null

    class VGInfo {
        var id: Int = 0
        @JSONField(name = "version_id")
        var versionId: Int? = null
        @JSONField(name = "version_name")
        var name: String? = null

        var grade: Int = 0
        var title: String? = null

        @JSONField(name = "part_type")
        var partType: Int = 0

        var isSelect = false

        var alias: String? = null

        override fun toString(): String {
            return "VGInfo(id=$id, versionId=$versionId, name=$name, grade=$grade, title=$title, partType=$partType, isSelect=$isSelect, alias=$alias)"
        }


    }
}
