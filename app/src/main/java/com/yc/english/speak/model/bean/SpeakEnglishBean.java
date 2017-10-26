package com.yc.english.speak.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by admin on 2017/10/16.
 */

public class SpeakEnglishBean implements Serializable {

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
}
