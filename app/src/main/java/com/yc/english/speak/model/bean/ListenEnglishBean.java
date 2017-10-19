package com.yc.english.speak.model.bean;

import java.io.Serializable;

/**
 * Created by admin on 2017/10/16.
 */

public class ListenEnglishBean implements Serializable {
    
    private String cnSentence;//中文句子

    private String enSentence;//英文句子

    private boolean isShowSpeak;

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
}
