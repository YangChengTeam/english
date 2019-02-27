package com.yc.soundmark.study.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanglin  on 2018/10/30 16:59.
 * 句子
 */
public class SentenceInfo implements Parcelable {
    /**
     * id : 1
     * vid : 1
     * sentence : Would you like coff#ee# or t#ea#?
     * en : 您想喝咖啡还是茶？
     * sort : 1
     * mp3 : https://yb.bshu.com/uploads/20180813/31fe2d19cb70620c5332324db52986ee.mp3
     */

    private String id;
    private String vid;
    private String sentence;
    private String en;
    private String sort;
    private String mp3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.vid);
        dest.writeString(this.sentence);
        dest.writeString(this.en);
        dest.writeString(this.sort);
        dest.writeString(this.mp3);
    }

    public SentenceInfo() {
    }

    protected SentenceInfo(Parcel in) {
        this.id = in.readString();
        this.vid = in.readString();
        this.sentence = in.readString();
        this.en = in.readString();
        this.sort = in.readString();
        this.mp3 = in.readString();
    }

    public static final Creator<SentenceInfo> CREATOR = new Creator<SentenceInfo>() {
        @Override
        public SentenceInfo createFromParcel(Parcel source) {
            return new SentenceInfo(source);
        }

        @Override
        public SentenceInfo[] newArray(int size) {
            return new SentenceInfo[size];
        }
    };
}
