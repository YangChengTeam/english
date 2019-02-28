package com.yc.junior.english.intelligent.model.domain

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
        var simpleName: String = ""
        var pid: Int = 0
        var book_id: Int = 0
        @JSONField(name = "unit_finish_detail") var unit_finish_detail: ComleteInfo? = null
    }

    class ComleteInfo {
        var read: Int = -1
        var vocabulary: Int = -1
        var grammar: Int = -1
        var writing: Int = -1
        var hearing: Int = -1
        var oracy: Int = -1
    }

    class ComleteItemInfo(var key: String, var value: String, var isComplete: Int) {

    }
}

