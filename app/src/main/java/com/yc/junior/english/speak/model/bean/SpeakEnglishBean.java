package com.yc.junior.english.speak.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by admin on 2017/10/16.
 */

public class SpeakEnglishBean implements Parcelable {

    private String id;

    @JSONField(name = "read_id")
    private String readId;

    @JSONField(name = "means")
    private String cnSentence;//中文句子

    @JSONField(name = "name")
    private String enSentence;//英文句子

    private boolean isShowSpeak;

    private boolean isShowResult;

    private boolean speakResult;

    private String percent;

    public String getCnSentence() {
        return cnSentence;
    }

    public void setCnSentence(String cnSentence) {
        this.cnSentence = cnSentence;
    }

    public String getEnSentence() {
        return enSentence;
    }

    public void setEnSentence(String enSentence) {
        this.enSentence = enSentence;
    }

    public boolean isShowSpeak() {
        return isShowSpeak;
    }

    public void setShowSpeak(boolean showSpeak) {
        isShowSpeak = showSpeak;
    }

    public boolean isShowResult() {
        return isShowResult;
    }

    public void setShowResult(boolean showResult) {
        isShowResult = showResult;
    }

    public boolean isSpeakResult() {
        return speakResult;
    }

    public void setSpeakResult(boolean speakResult) {
        this.speakResult = speakResult;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public SpeakEnglishBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.readId);
        dest.writeString(this.cnSentence);
        dest.writeString(this.enSentence);
        dest.writeByte(this.isShowSpeak ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowResult ? (byte) 1 : (byte) 0);
        dest.writeByte(this.speakResult ? (byte) 1 : (byte) 0);
        dest.writeString(this.percent);
    }

    protected SpeakEnglishBean(Parcel in) {
        this.id = in.readString();
        this.readId = in.readString();
        this.cnSentence = in.readString();
        this.enSentence = in.readString();
        this.isShowSpeak = in.readByte() != 0;
        this.isShowResult = in.readByte() != 0;
        this.speakResult = in.readByte() != 0;
        this.percent = in.readString();
    }

    public static final Creator<SpeakEnglishBean> CREATOR = new Creator<SpeakEnglishBean>() {
        @Override
        public SpeakEnglishBean createFromParcel(Parcel source) {
            return new SpeakEnglishBean(source);
        }

        @Override
        public SpeakEnglishBean[] newArray(int size) {
            return new SpeakEnglishBean[size];
        }
    };
}
