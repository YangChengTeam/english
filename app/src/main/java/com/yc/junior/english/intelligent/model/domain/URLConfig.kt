package com.yc.junior.english.intelligent.model.domain

import com.yc.junior.english.base.model.Config

/**
 * Created by zhangkai on 2017/8/1.
 */

object URLConfig {
    val DEBUG = Config.DEBUG

    private val baseUrl = "http://en.wk2.com/api/"
    private val debugBaseUrl = "http://en.qqtn.com/api/"

    val GET_VERSION = getBaseUrl() + "topic/get_version"
    val GET_GRADE = getBaseUrl() + "topic/get_grade"
    val GET_UNIT = getBaseUrl() + "topic/get_unit"
    val GET_QUESTION = getBaseUrl() + "topic/topic_list"
    val UPDATE_ANSWERS = getBaseUrl() + "topic/answer_list"
    val UNIT_REPORT = getBaseUrl() + "userTest/report"
    val UNIT_PLAN = getBaseUrl() + "userTest/plan"
    val UNIT_PLAN_DETAIL = getBaseUrl() + "userTest/planDetail"
    val REMOVE_ANSWER = getBaseUrl() + "topic/remove_answer"


    fun getBaseUrl(): String {
        return if (DEBUG) debugBaseUrl else baseUrl
    }

}
