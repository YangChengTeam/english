package com.yc.english.intelligent.model.domain

import com.alibaba.fastjson.annotation.JSONField

/**
 * Created by zhangkai on 2017/12/4.
 */

class UnitInfoWrapper {
    var list: List<UnitInfo>? = null

    class UnitInfo {
        var id: Int = 0
        var name: String = ""
        @JSONField(name = "simple_name")
        var simpleName: String? = ""
        var pid: Int = 0
        var book_id: Int = 0
        @JSONField(name = "unit_finish_detail")
        var complete: ComleteInfo? = null
    }

    class ComleteInfo {
        var read: Int = 0
        var vocabulary: Int = 0
        var grammar: Int = 0
        var writing: Int = 0
        var hearing: Int = 0
        var oracy: Int = 0
    }
}

