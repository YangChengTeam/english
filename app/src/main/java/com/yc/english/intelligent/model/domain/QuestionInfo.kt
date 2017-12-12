package com.yc.english.intelligent.model.domain

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by zhangkai on 2017/12/1.
 */

class QuestionInfoWrapper {
    var list: List<QuestionInfo>? = null

    class QuestionInfo() : MultiItemEntity, Parcelable {
        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeInt(count)
            dest?.writeString(title)
            dest?.writeString(answer)
            dest?.writeString(userAnswer)
            dest?.writeInt(actIndex)
            dest?.writeInt(frgIndex)
            dest?.writeString(type)
        }

        override fun describeContents(): Int {
            return 0
        }

        var id = ""

        @JSONField(name = "kpoint_type")
        var type = ""
        var pid: Int = 0
        var title: String? = null
        @JSONField(name = "desp")
        var desc: String? = null
        @JSONField(name = "voice_text")
        var voiceText: String? = null
        @JSONField(name = "voice_url")
        var voiceUrl: String? = null
        var answer: String? = null
        var analysis: String? = null
        var difficult: String? = ""
        @JSONField(name = "child_num")
        var count: Int = 0
        @JSONField(name = "user_answer")
        var userAnswer: String? = null
        var options: Option? = null
        var data: List<QuestionInfo>? = null
        var actIndex: Int = 0
        var frgIndex: Int = 0

        constructor(parcel: Parcel) : this() {
            count = parcel.readInt()
            title = parcel.readString()
            answer = parcel.readString()
            userAnswer = parcel.readString()
            actIndex = parcel.readInt()
            frgIndex = parcel.readInt()
            type = parcel.readString()
        }

        override fun getItemType(): Int {
            return if (count > 0) 0 else 1
        }

        companion object CREATOR : Parcelable.Creator<QuestionInfo> {
            override fun createFromParcel(parcel: Parcel): QuestionInfo {
                return QuestionInfo(parcel)
            }

            override fun newArray(size: Int): Array<QuestionInfo?> {
                return arrayOfNulls(size)
            }
        }

    }

    class Option {
        var type: String = "text"
        var options: List<String>? = null
    }

}