package com.yc.english.speak.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/10/16.
 */

@Entity
public class QuestionInfoBean implements Parcelable, MultiItemEntity {

    public static final int MAIN_QUESTION = 1;

    public static final int OPTION_QUESTION = 2;

    private int itemType;

    @Id
    private String id;

    @JSONField(name = "read_id")
    private String readId;

    @JSONField(name = "means")
    private String cnSentence;//中文句子

    @JSONField(name = "name")
    private String enSentence;//英文句子

    private String title;

    private boolean isShowSpeak;

    private boolean isShowResult;

    private boolean speakResult;

    private String percent = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuestionInfoBean() {
    }

    public QuestionInfoBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.id);
        dest.writeString(this.readId);
        dest.writeString(this.cnSentence);
        dest.writeString(this.enSentence);
        dest.writeString(this.title);
        dest.writeByte(this.isShowSpeak ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShowResult ? (byte) 1 : (byte) 0);
        dest.writeByte(this.speakResult ? (byte) 1 : (byte) 0);
        dest.writeString(this.percent);
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getReadId() {
        return this.readId;
    }

    public void setReadId(String readId) {
        this.readId = readId;
    }

    public boolean getIsShowSpeak() {
        return this.isShowSpeak;
    }

    public void setIsShowSpeak(boolean isShowSpeak) {
        this.isShowSpeak = isShowSpeak;
    }

    public boolean getIsShowResult() {
        return this.isShowResult;
    }

    public void setIsShowResult(boolean isShowResult) {
        this.isShowResult = isShowResult;
    }

    public boolean getSpeakResult() {
        return this.speakResult;
    }

    protected QuestionInfoBean(Parcel in) {
        this.itemType = in.readInt();
        this.id = in.readString();
        this.readId = in.readString();
        this.cnSentence = in.readString();
        this.enSentence = in.readString();
        this.title = in.readString();
        this.isShowSpeak = in.readByte() != 0;
        this.isShowResult = in.readByte() != 0;
        this.speakResult = in.readByte() != 0;
        this.percent = in.readString();
    }

    @Generated(hash = 1819355084)
    public QuestionInfoBean(int itemType, String id, String readId, String cnSentence,
            String enSentence, String title, boolean isShowSpeak, boolean isShowResult,
            boolean speakResult, String percent) {
        this.itemType = itemType;
        this.id = id;
        this.readId = readId;
        this.cnSentence = cnSentence;
        this.enSentence = enSentence;
        this.title = title;
        this.isShowSpeak = isShowSpeak;
        this.isShowResult = isShowResult;
        this.speakResult = speakResult;
        this.percent = percent;
    }

    public static final Creator<QuestionInfoBean> CREATOR = new Creator<QuestionInfoBean>() {
        @Override
        public QuestionInfoBean createFromParcel(Parcel source) {
            return new QuestionInfoBean(source);
        }

        @Override
        public QuestionInfoBean[] newArray(int size) {
            return new QuestionInfoBean[size];
        }
    };
}
