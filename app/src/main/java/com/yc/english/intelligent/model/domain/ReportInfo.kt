package com.yc.english.intelligent.model.domain

/**
 * Created by zhangkai on 2017/12/13.
 */

class ReportInfo {
    var id: Int = 0
    var read: Int = 0
    var vocabulary = 0
    var grammar: Int = 0
    var writing: Int = 0
    var hearing: Int = 0
    var oracy: Int = 0
    var desp: String = ""
    var score: Int = 0
    var error_grammar: List<String>? = null
    var err_tips: List<ReportErrorInfo>? = null

    var grammar_right: Int = 0
    var hearing_right: Int = 0
    var oracy_right: Int = 0
    var read_right: Int = 0
    var vocabulary_right: Int = 0
    var writing_right: Int = 0
    var total: Int = 0

    fun getRightSum(): Int {
        return grammar_right + hearing_right + oracy_right + read_right + vocabulary_right + writing_right
    }

    fun getErrorSum(): Int {
        return total - getRightSum()
    }

}